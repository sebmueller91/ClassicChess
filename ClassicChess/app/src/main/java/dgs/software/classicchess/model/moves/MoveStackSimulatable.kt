package dgs.software.classicchess.model.moves

import android.util.Log
import dgs.software.classicchess.model.Coordinate

private val TAG = "MoveStackSimulatable"

data class MoveStackSimulatable(
    private val moveStack: MoveStack = MoveStack(),
    private val simulatedMoveStack: MoveStack = MoveStack()
) {
    fun resetMoveStack() {
        moveStack.resetMoveStack()
        simulatedMoveStack.resetMoveStack()
    }

    fun executeMove(move: RevertableMove, simulateExecution: Boolean = false) {
        Log.d(TAG, "Execute move $move.toString(), simulated: $simulateExecution")
        if (simulateExecution) {
            simulatedMoveStack.executeMove(move)
        } else {
            if (simulatedMoveStack.doneActionsOnStack()) {
                Log.e(TAG, "Executing move while simulated moves on stack, this should not happen!")
                simulatedMoveStack.resetMoveStack()
            }
            moveStack.executeMove(move)
        }
    }

    fun rollbackSimulatedMoves() {
        simulatedMoveStack.resetMoveStack()
    }

    fun rollbackLastMove(): Boolean {
        if (!doneActionsOnStack()) {
            Log.d(
                TAG,
                "Attempted to rollback last move which was not possible"
            )
            return false
        }
        if (simulatedMoveStack.doneActionsOnStack()) {
            Log.e(
                TAG,
                "Rolling back move while simulated moveStack is not empty, this should not happen!"
            )
            simulatedMoveStack.rollbackLastMove()
        } else {
            moveStack.rollbackLastMove()
        }
        return true
    }

    fun redoNextMove(): Boolean {
        if (!undoneActionsOnStack()) {
            Log.d(
                TAG,
                "Attempted to redo next move which was not possible"
            )
            return false
        }
        if (simulatedMoveStack.doneActionsOnStack()) {
            Log.e(TAG, "Redoing move while simulated moveStack is not empty, this should not happen!")
        }
        if (simulatedMoveStack.undoneActionsOnStack()) {
            simulatedMoveStack.redoNextMove()
        } else {
            moveStack.redoNextMove()
        }
        return true
    }

    fun doneActionsOnStack(): Boolean {
        return simulatedMoveStack.doneActionsOnStack() || moveStack.doneActionsOnStack()
    }

    fun undoneActionsOnStack(): Boolean {
        return simulatedMoveStack.undoneActionsOnStack() || moveStack.undoneActionsOnStack()
    }

    fun lastMoveWas(fromPos: Coordinate, toPos: Coordinate): Boolean {
        if (!doneActionsOnStack()) {
            return false
        }
        return if (simulatedMoveStack.doneActionsOnStack()) {
            simulatedMoveStack.lastMoveWas(fromPos, toPos)
        } else {
            moveStack.lastMoveWas(fromPos, toPos)
        }
    }

    fun anyMoveExecuted(): Boolean {
        return simulatedMoveStack.anyMoveExecuted() || moveStack.anyMoveExecuted()
    }
}