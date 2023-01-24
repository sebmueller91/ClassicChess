package dgs.software.classicchess.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dgs.software.classicchess.model.moves.MoveStack
import dgs.software.classicchess.model.moves.RevertableMove

private const val TAG = "Game"

data class Game(
    val board: Board = Board(),
    val moveStack: MoveStack = MoveStack(),
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

    fun get(coordinate: Coordinate): Cell {
        return board.get(coordinate)
    }

    fun anyMoveExecuted(): Boolean {
        return moveStack.moves.any()
    }

    fun set(coordinate: Coordinate, cell: Cell) {
        board.set(coordinate, cell)
    }

    fun undoLastMove() {
        moveStack.rollbackLastMove()
    }

    fun redoNextMove() {

        moveStack.redoNextMove()
    }

    fun getAsPiece(coordinate: Coordinate): Cell.Piece {
        if (board.get(coordinate) is Cell.Empty) {
            Log.e(TAG, "Tried to get empty coordinate $coordinate as Piece")
        }
        return board.get(coordinate) as Cell.Piece
    }

    fun isPlayer(coordinate: Coordinate, player: Player): Boolean {
        return get(coordinate) !is Cell.Empty && getAsPiece(coordinate).player == player
    }

    fun executeMove(move: RevertableMove, simulateExecution: Boolean = false) {
        moveStack.executeMove(move, simulateExecution)
    }

    fun rollbackSimulatedMoves() {
        moveStack.rollbackSimulatedMoves()
    }

    fun reset() {
        board.reset()
        moveStack.resetMoveStack()
        currentPlayer = Player.WHITE
    }
}