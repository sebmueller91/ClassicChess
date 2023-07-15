package dgs.software.classicchess.ui.screens.local_game

import android.util.Log
import androidx.lifecycle.ViewModel
import dgs.software.classicchess.model.*
import dgs.software.classicchess.model.moves.PromotePawnMove
import dgs.software.classicchess.use_cases.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

private const val TAG = "LocalGameViewModel"

class LocalGameViewModel : ViewModel() {
    var _uiState: MutableStateFlow<LocalGameUiState> = MutableStateFlow(LocalGameUiState())
    val uiState = _uiState.asStateFlow()

    fun cellSelected(clickedCoordinate: Coordinate) {

        when (val userClickAction =
            ResolveUserClickActionUseCase().execute(
                uiState.value.game,
                uiState.value.possibleMovesForSelectedPiece,
                clickedCoordinate
            )) {
            UserClickAction.DisplayPossibleMovesOfPiece -> {
                val possibleMoves =
                    GetPossibleMovesUseCase().execute(uiState.value.game, clickedCoordinate)
                _uiState.update { previousState ->
                    previousState.copy(
                        selectedCoordinate = clickedCoordinate,
                        possibleMovesForSelectedPiece = possibleMoves
                    )
                }
            }
            is UserClickAction.ExecutePromotePawnMove -> {
                _uiState.update { previousState ->
                    previousState.copy(
                        selectedPawnPromotionPosition = userClickAction.coordinate,
                        requestPawnPromotionInput = true
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
                        possibleMovesForSelectedPiece = listOf()
                    )
                }
            }
            UserClickAction.EmptyCellSelected,
            UserClickAction.OpponentCellSelected -> {
                _uiState.update { previousState ->
                    previousState.copy(
                        selectedCoordinate = clickedCoordinate,
                        possibleMovesForSelectedPiece = listOf()
                    )
                }
            }
        }

        updateBoard()
    }

    fun promotePawn(type: Type) {
        if (uiState.value.selectedPawnPromotionPosition == null) {
            Log.e(TAG, "Tried to promote pawn while position is not set")
            return
        }
        val clickedMove =
            uiState.value.possibleMovesForSelectedPiece.filter {
                it.toPos == uiState.value.selectedPawnPromotionPosition
                        && (it as PromotePawnMove).type == type
            }

        if (!clickedMove.any()) {
            Log.e(TAG, "promotePawn() expects at least 1 move")
            return
        }

        val game = ExecuteMoveUseCase().execute(uiState.value.game, clickedMove.first())

        _uiState.update { previousState ->
            previousState.copy(
                game = game,
                selectedCoordinate = null,
                possibleMovesForSelectedPiece = listOf(),
                requestPawnPromotionInput = false,
                selectedPawnPromotionPosition = null
            )
        }
        updateBoard()
    }

    fun dismissPromotePawn() {
        _uiState.update { previousState ->
            previousState.copy(
                selectedPawnPromotionPosition = null,
                requestPawnPromotionInput = false,
                possibleMovesForSelectedPiece = listOf()
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

    fun resetGame() {
        _uiState.update { previousState ->
            LocalGameUiState(boardDisplayedInverted = previousState.boardDisplayedInverted)
        }

        updateBoard()
    }

    fun undoPreviousMove() {
        val game = UndoPreviousMoveUseCase().execute(uiState.value.game)
        _uiState.update { previousState ->
            previousState.copy(
                game = game,
                possibleMovesForSelectedPiece = listOf(),
                selectedCoordinate = null
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
                selectedCoordinate = null
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
                canResetGame = uiState.value.game.anyMoveExecuted(),
                canRedoMove = uiState.value.game.canRedoMove,
                canUndoMove = uiState.value.game.canUndoMove
            )
        }
    }
}