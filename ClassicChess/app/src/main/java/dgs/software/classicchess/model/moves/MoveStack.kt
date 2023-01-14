package dgs.software.classicchess.model.moves

import android.util.Log

private val TAG = "MoveStack"

data class MoveStack(
    val moves: MutableList<RevertableMove> = mutableListOf<RevertableMove>()
) {
    var iteratorIndex = -1

    fun ResetMoveStack() {
        moves.clear()
    }

    fun executeMove(move: RevertableMove, simulateExecution: Boolean = false) {
        Log.d(TAG, "Execute move $move.toString(), simulated: $simulateExecution")
        deleteElementsAfterIndex(iteratorIndex)
        rollbackSimulatedMoves()
        move.execute(simulateExecution)
        moves.add(move)
        iteratorIndex = moves.count() - 1
    }

    fun rollbackSimulatedMoves() {
        var count = 0
        while (iteratorIndex >= 0 && moves[iteratorIndex].isSimulated) {
            moves[iteratorIndex].rollback()
            iteratorIndex--
            count++
        }
        if (count > 0) {
            Log.d(TAG, "Rolled back ${count} simulated moves")
        }
        deleteElementsAfterIndex(iteratorIndex)
    }

    fun rollbackLastMove(): Boolean {
        if (!doneActionsOnStack()) {
            Log.d(
                TAG,
                "Attempted to rollback last move at index ${iteratorIndex}/${moves.size} which was not possible"
            )
            return false
        }
        moves[iteratorIndex].rollback()
        iteratorIndex--
        return true
    }

    fun redoNextMove(): Boolean {
        if (!undoneActionsOnStack()) {
            Log.d(
                TAG,
                "Attempted to redo next move at index ${iteratorIndex}/${moves.size} which was not possible"
            )
            return false
        }
        iteratorIndex++
        moves[iteratorIndex].execute()
        return true
    }

    fun doneActionsOnStack(): Boolean {
        return iteratorIndex >= 0
    }

    fun undoneActionsOnStack(): Boolean {
        return iteratorIndex < moves.size - 1
    }

    private fun deleteElementsAfterIndex(index: Int) {
        if (index <= 0 || index >= moves.size) {
            return;
        }
        while (moves.size > index + 1) {
            moves.removeLast()
        }
    }
}