package dgs.software.classicchess.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dgs.software.classicchess.ClassicChessApplication
import dgs.software.classicchess.calculations.possiblemoves.DefaultGameStatusProvider
import dgs.software.classicchess.calculations.possiblemoves.DefaultPossibleMovesProvider
import dgs.software.classicchess.model.*
import dgs.software.classicchess.model.moves.RevertableMove

private const val TAG = "LocalGameViewModel"

class LocalGameViewModel : ViewModel() {
    var forceBoardRecomposition by mutableStateOf(false)

    var gameUiState: Game by mutableStateOf(Game())
        private set

    var selectedCell: Cell? by mutableStateOf(null)
        private set

    var possibleMovesForSelectedPiece by mutableStateOf(mutableListOf<RevertableMove>())
        private set

    var kingInCheck: Coordinate? by mutableStateOf(null)

    var playerWon: Player? by mutableStateOf(null)

    var boardDisplayedInverted by mutableStateOf(false)
        private set

    private val possibleMovesProvider = DefaultPossibleMovesProvider(gameUiState)
    private val gameStatusProvider = DefaultGameStatusProvider(gameUiState)

    fun cellSelected(coordinate: Coordinate) {
        // TODO: Add Log statements
        selectedCell = gameUiState.get(coordinate)
        val clickedMove = possibleMovesForSelectedPiece.filter { it.toPos == coordinate }
        possibleMovesForSelectedPiece.clear()

        if (clickedMove.any()) {
            gameUiState.executeMove(clickedMove.first())
            selectedCell = null
            possibleMovesForSelectedPiece.clear()
        }
        else if (selectedCell is Cell.Empty) {
            // Do nothing
        }
        else if ((selectedCell as Cell.Piece).player == gameUiState.currentPlayer) {
            possibleMovesForSelectedPiece.addAll(
                possibleMovesProvider.getPossibleMoves(
                    (selectedCell as Cell.Piece).coordinate
                )
            )
        }

        updateKingInCheck()
        updatePlayerWon()
    }

    fun invertBoardDisplayDirection() {
        boardDisplayedInverted = !boardDisplayedInverted
    }

    fun canResetGame(): Boolean {
        return gameUiState.anyMoveExecuted()
    }

    fun resetGame() {
        selectedCell = null
        kingInCheck = null
        possibleMovesForSelectedPiece.clear()
        gameUiState.reset()
        forceBoardRecomposition = !forceBoardRecomposition
    }

    fun undoLastMove() {
        gameUiState.undoLastMove()
        updateKingInCheck()
    }

    fun redoNextMove() {
        gameUiState.redoNextMove()
        updateKingInCheck()
    }

    // returns true if it is a cell with light background, false otherwise
    fun getCellBackgroundType(rowIndex: Int, colIndex: Int): Boolean {
        return (rowIndex % 2 == 0 && colIndex % 2 == 0) || (rowIndex % 2 != 0 && colIndex % 2 != 0)
    }

    private fun updateKingInCheck() {
        kingInCheck = null
        Player.values().forEach { player ->
            val positionOfKing = gameStatusProvider.getPositionOfKing(player)
            val cellsInCheck = gameStatusProvider.getCellsInCheck(player)
            if (cellsInCheck[positionOfKing.row][positionOfKing.column]) {
                kingInCheck = positionOfKing
            }
        }
    }

    private fun updatePlayerWon() {
        if (gameStatusProvider.isCheckmate(Player.WHITE)) {
            playerWon = Player.BLACK
        } else if (gameStatusProvider.isCheckmate(Player.BLACK)) {
            playerWon = Player.WHITE
        }
    }

    init {
        gameUiState = Game()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ClassicChessApplication)
                LocalGameViewModel()
            }
        }
    }
}