package dgs.software.classicchess.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dgs.software.classicchess.model.moves.MoveStackSimulatable
import dgs.software.classicchess.model.moves.RevertableMove

private const val TAG = "Game"

data class Game(
    val board: Board = Board(),
    val simulatableMoveStack: MoveStackSimulatable = MoveStackSimulatable(),
) {
    var currentPlayer by mutableStateOf(Player.WHITE)
        private set

    fun updateCurrentPlayer(player: Player) {
        currentPlayer = player
    }

    val canUndoMove: Boolean
        get() = simulatableMoveStack.doneActionsOnStack()

    val canRedoMove: Boolean
        get() = simulatableMoveStack.undoneActionsOnStack()

    fun anyMoveExecuted(): Boolean {
        return simulatableMoveStack.anyMoveExecuted()
    }

    fun undoLastMove() {
        simulatableMoveStack.rollbackLastMove()
    }

    fun redoNextMove() {

        simulatableMoveStack.redoNextMove()
    }

    fun executeMove(move: RevertableMove, simulateExecution: Boolean = false) {
        simulatableMoveStack.executeMove(move, simulateExecution)
    }

    fun rollbackSimulatedMoves() {
        simulatableMoveStack.rollbackSimulatedMoves()
    }

    fun reset() {
        board.reset()
        simulatableMoveStack.resetMoveStack()
        currentPlayer = Player.WHITE
    }
}