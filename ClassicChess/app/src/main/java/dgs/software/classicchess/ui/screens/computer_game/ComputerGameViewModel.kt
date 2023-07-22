package dgs.software.classicchess.ui.screens.computer_game

import UserClickAction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dgs.software.classicchess.calculations.ai.AiMoveCalculator
import dgs.software.classicchess.calculations.ai.Difficulty
import dgs.software.classicchess.logger.Logger
import dgs.software.classicchess.logger.LoggerFactory
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.Player
import dgs.software.classicchess.model.Type
import dgs.software.classicchess.model.moves.PromotePawnMove
import dgs.software.classicchess.model.toMutableGame
import dgs.software.classicchess.ui.theme.White
import dgs.software.classicchess.use_cases.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "ComputerGameViewModel"

class ComputerGameViewModel(
    difficulty: Difficulty,
    private val logger: Logger = LoggerFactory().create()
) : ViewModel() {
    private var _uiState: MutableStateFlow<ComputerGameUiState> =
        MutableStateFlow(ComputerGameUiState(difficulty = difficulty))
    val uiState = _uiState.asStateFlow()
    private var aiMoveCalculator: AiMoveCalculator

    init {
        val computerPlayer = Player.values().toList().shuffled().first()
        _uiState.update { previousState ->
            previousState.copy(
                computerPlayer = computerPlayer,
                computeAiMove = computerPlayer == Player.WHITE
            )
        }
        aiMoveCalculator =  AiMoveCalculator(difficulty, uiState.value.computerPlayer)

        viewModelScope.launch {
            uiState.collect { state ->
                if (state.computeAiMove) {
                    val move = withContext(Dispatchers.Default) {
                        aiMoveCalculator.calculateAiMove(uiState.value.game.toMutableGame())
                    }
                    val game = ExecuteMoveUseCase().execute(uiState.value.game, move)
                    _uiState.update { previousState ->
                        previousState.copy(
                            game = game,
                            selectedCoordinate = null,
                            possibleMovesForSelectedPiece = listOf(),
                            computeAiMove = false,
                            lastComputerMove = move,
                            boardDisplayedInverted = computerPlayer == Player.WHITE
                        )
                    }
                }
            }
            updateBoard()
        }
    }

    fun cellSelected(clickedCoordinate: Coordinate) {
        when (val userClickAction =
            ResolveUserClickActionUseCase().executeAiGame(
                uiState.value.game,
                uiState.value.possibleMovesForSelectedPiece,
                clickedCoordinate,
                uiState.value.computerPlayer
            )) {
            UserClickAction.DisplayPossibleMovesOfPiece -> {
                val possibleMoves =
                    GetPossibleMovesUseCase().execute(uiState.value.game, clickedCoordinate)
                _uiState.update { previousState ->
                    previousState.copy(
                        selectedCoordinate = clickedCoordinate,
                        possibleMovesForSelectedPiece = possibleMoves,
                        lastComputerMove = null
                    )
                }
            }
            is UserClickAction.ExecutePromotePawnMove -> {
                _uiState.update { previousState ->
                    previousState.copy(
                        selectedPawnPromotionPosition = userClickAction.coordinate,
                        requestPawnPromotionInput = true,
                        lastComputerMove = null
                    )
                }
            }
            is UserClickAction.ExecuteRegularMove -> {
                val game =
                    ExecuteMoveUseCase().execute(uiState.value.game, userClickAction.revertableMove)
                _uiState.update { previousState ->
                    previousState.copy(
                        game = game,
                        selectedCoordinate = null,
                        possibleMovesForSelectedPiece = listOf(),
                        computeAiMove = true,
                        lastComputerMove = null
                    )
                }
            }
            UserClickAction.NoActionCellSelected,
            UserClickAction.OpponentCellSelected -> {
                _uiState.update { previousState ->
                    previousState.copy(
                        selectedCoordinate = clickedCoordinate,
                        possibleMovesForSelectedPiece = listOf(),
                        lastComputerMove = null
                    )
                }
            }
        }

        updateBoard()
    }

    fun promotePawn(type: Type) {
        if (uiState.value.selectedPawnPromotionPosition == null) {
            logger.e(TAG, "Tried to promote pawn while position is not set")
            return
        }
        val clickedMove =
            uiState.value.possibleMovesForSelectedPiece.filter {
                it.toPos == uiState.value.selectedPawnPromotionPosition
                        && (it as PromotePawnMove).type == type
            }

        if (!clickedMove.any()) {
            logger.e(TAG, "promotePawn() expects at least 1 move")
            return
        }

        val game = ExecuteMoveUseCase().execute(uiState.value.game, clickedMove.first())

        _uiState.update { previousState ->
            previousState.copy(
                game = game,
                selectedCoordinate = null,
                possibleMovesForSelectedPiece = listOf(),
                requestPawnPromotionInput = false,
                selectedPawnPromotionPosition = null,
                computeAiMove = true,
                lastComputerMove = null
            )
        }
        updateBoard()
    }

    fun dismissPromotePawn() {
        _uiState.update { previousState ->
            previousState.copy(
                selectedPawnPromotionPosition = null,
                requestPawnPromotionInput = false,
                possibleMovesForSelectedPiece = listOf(),
                lastComputerMove = null
            )
        }
    }

    fun invertBoardDisplayDirection() {
        _uiState.update { previousState ->
            previousState.copy(
                boardDisplayedInverted = !previousState.boardDisplayedInverted
            )
        }
    }

    fun startNewGame(difficulty: Difficulty) {
        val computerPlayer = Player.values().toList().shuffled().first()
        _uiState.update { previousState ->
            ComputerGameUiState(
                computerPlayer = computerPlayer,
                computeAiMove = computerPlayer == Player.WHITE,
                boardDisplayedInverted = computerPlayer == Player.WHITE,
                difficulty = difficulty
            )
        }
        aiMoveCalculator =  AiMoveCalculator(difficulty, uiState.value.computerPlayer)
    }

    fun undoPreviousMove() {
        val game = UndoPreviousMoveUseCase().execute(uiState.value.game)
        _uiState.update { previousState ->
            previousState.copy(
                game = game,
                possibleMovesForSelectedPiece = listOf(),
                selectedCoordinate = null,
                lastComputerMove = null
            )
        }
        updateBoard()
    }

    fun redoNextMove() {
        val game = RedoNextMoveUseCase().execute(uiState.value.game)
        _uiState.update { previousState ->
            previousState.copy(
                game = game,
                possibleMovesForSelectedPiece = listOf(),
                selectedCoordinate = null,
                lastComputerMove = null
            )
        }
        updateBoard()
    }

    fun resetPlayerWon() {
        _uiState.update { previousState ->
            previousState.copy(playerWon = null)
        }
    }

    fun resetPlayerStalemate() {
        _uiState.update { previousState ->
            previousState.copy(playerStalemate = null)
        }
    }

    private fun updateBoard() {
        val gameStatusInfo = UpdateGameStatusUseCase().execute(uiState.value.game)
        _uiState.update { previousState ->
            previousState.copy(
                kingInCheck = gameStatusInfo.kingInCheck,
                playerWon = gameStatusInfo.playerWon,
                playerStalemate = gameStatusInfo.playerStalemate,
                canRedoMove = uiState.value.game.canRedoMove,
                canUndoMove = uiState.value.game.canUndoMove
            )
        }
    }
}