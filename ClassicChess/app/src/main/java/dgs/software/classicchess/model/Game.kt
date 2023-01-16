package dgs.software.classicchess.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dgs.software.classicchess.model.moves.MoveStack
import dgs.software.classicchess.model.moves.RevertableMove

private const val TAG = "Game"

data class Game(
    private val board: Board = Board(),
    private val moveStack: MoveStack = MoveStack(),
   // private var currentPlayer: Player = Player.WHITE

) {
    var currentPlayer by mutableStateOf(Player.WHITE)
        private set

    fun updateCurrentPlayer(player: Player) {
        currentPlayer = player
    }

    val canUndoMove: Boolean
        get() = moveStack.doneActionsOnStack()

    val canRedoMove: Boolean
        get() = moveStack.undoneActionsOnStack()

    fun getBoard() : Board {
        return board
    }

//    val curPlayer: Player
//        get() = currentPlayer
//
//    fun getCurrentPlayer() : Player {
//        return currentPlayer
//    }
//
//    fun setCurrentPlayer(player: Player) {
//        currentPlayer = player
//    }

    fun get(coordinate: Coordinate) : Cell {
        return board.get(coordinate)
    }

    fun set(coordinate: Coordinate, cell: Cell) {
        board.set(coordinate,cell)
    }

    fun getAsPiece(coordinate: Coordinate) : Cell.Piece {
        if (board.get(coordinate) is Cell.Empty) {
            Log.e(TAG, "Tried to get empty coordinate $coordinate as Piece")
        }
        return board.get(coordinate) as Cell.Piece
    }

    fun executeMove(move: RevertableMove, simulateExecution: Boolean = false) {
        moveStack.executeMove(move, simulateExecution)
    }

    fun rollbackSimulatedMoves() {
        moveStack.rollbackSimulatedMoves()
    }
}