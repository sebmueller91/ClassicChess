package dgs.software.classicchess.ui.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import dgs.software.classicchess.ClassicChessApplication
import dgs.software.classicchess.calculations.possiblemoves.DefaultBoardStatusProvider
import dgs.software.classicchess.calculations.possiblemoves.DefaultGameStatusProvider
import dgs.software.classicchess.calculations.possiblemoves.DefaultPossibleMovesProvider
import dgs.software.classicchess.model.*
import dgs.software.classicchess.model.moves.PromotePawnMove
import dgs.software.classicchess.model.moves.RevertableMove

private const val TAG = "LocalGameViewModel"

class LocalGameViewModel : ViewModel() {
    var forceBoardRecomposition by mutableStateOf(false) // TODO: Try to get rid of

    var game: Game by mutableStateOf(Game())
        private set

    var selectedCoordinate: Coordinate? by mutableStateOf(null)
        private set

    val selectedCell: Cell?
        get() = selectedCoordinate?.let { game.get(it) }

    var selectedPawnPromotionPosition: Coordinate? = null
        private set

    var possibleMovesForSelectedPiece by mutableStateOf(mutableListOf<RevertableMove>())
        private set

    var kingInCheck: Coordinate? by mutableStateOf(null)

    var playerWon: Player? by mutableStateOf(null)

    var playerStalemate: Player? by mutableStateOf(null)

    var boardDisplayedInverted by mutableStateOf(false)
        private set

    var requestPawnPromotionInput by mutableStateOf(false)
        private set

    private val possibleMovesProvider = DefaultPossibleMovesProvider(game)
    private val boardStatusProvider = DefaultBoardStatusProvider(game)
    private val gameStatusProvider = DefaultGameStatusProvider(game)

    fun cellSelected(coordinate: Coordinate) {
        // TODO: Add Log statements
        selectedCoordinate = coordinate
        val clickedMove = possibleMovesForSelectedPiece.filter { it.toPos == coordinate }


        if (clickedMove.any()) {
            if (clickedMove.first() is PromotePawnMove) {
                selectedPawnPromotionPosition = clickedMove.first().toPos
                requestPawnPromotionInput = true
            } else {
                game.executeMove(clickedMove.first())
                selectedCoordinate = null
                possibleMovesForSelectedPiece.clear()
            }
        } else if (selectedCell is Cell.Empty) {
            possibleMovesForSelectedPiece.clear()
        } else if ((selectedCell as Cell.Piece).player == game.currentPlayer) {
            possibleMovesForSelectedPiece?.clear()
            possibleMovesForSelectedPiece.addAll(
                possibleMovesProvider.getPossibleMoves(coordinate)
            )
        } else {
            possibleMovesForSelectedPiece?.clear()
        }

        updateBoard()
    }

    fun promotePawn(type: Type) {
        if (selectedPawnPromotionPosition == null) {
            Log.e(TAG, "Tried to promote pawn while position is not set")
            return
        }
        val clickedMove =
            possibleMovesForSelectedPiece.filter {
                it.toPos == selectedPawnPromotionPosition
                        && (it as PromotePawnMove).type == type
            }

        if (!clickedMove.any()) {
            Log.e(TAG, "promotePawn() expects at least 1 move")
            return
        }

        game.executeMove(clickedMove.first())
        selectedCoordinate = null
        possibleMovesForSelectedPiece.clear()
        requestPawnPromotionInput = false
        selectedPawnPromotionPosition = null

        updateBoard()
    }

    fun dismissPromotePawn() {
        selectedPawnPromotionPosition = null
        requestPawnPromotionInput = false
        possibleMovesForSelectedPiece.clear()

    }

    fun invertBoardDisplayDirection() {
        boardDisplayedInverted = !boardDisplayedInverted
    }

    fun canResetGame(): Boolean {
        return game.anyMoveExecuted()
    }

    fun resetGame() {
        selectedCoordinate = null
        kingInCheck = null
        possibleMovesForSelectedPiece.clear()
        game.reset()
        forceBoardRecomposition = !forceBoardRecomposition
        updateBoard()
    }

    fun undoLastMove() {
        game.undoLastMove()
        possibleMovesForSelectedPiece?.clear()
        selectedCoordinate = null
        updateBoard()
    }

    fun redoNextMove() {
        game.redoNextMove()
        possibleMovesForSelectedPiece?.clear()
        selectedCoordinate = null
        updateBoard()
    }

    // returns true if it is a cell with light background, false otherwise
    fun getCellBackgroundType(rowIndex: Int, colIndex: Int): Boolean {
        return (rowIndex % 2 == 0 && colIndex % 2 == 0) || (rowIndex % 2 != 0 && colIndex % 2 != 0)
    }

    private fun updateBoard() {
        updateKingInCheck()
        updatePlayerWon()
    }

    private fun updateKingInCheck() {
        kingInCheck = null
        Player.values().forEach { player ->
            val positionOfKing = boardStatusProvider.getPositionOfKing(player)
            val cellsInCheck = boardStatusProvider.getCellsInCheck(player)
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

    private fun updateStalemate() {
        if (gameStatusProvider.isStalemate(Player.WHITE)) {
            playerStalemate = Player.BLACK
        } else if (gameStatusProvider.isStalemate(Player.BLACK)) {
            playerStalemate = Player.WHITE
        }
    }

    init {
        game = Game()
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