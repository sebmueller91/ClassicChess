package dgs.software.classicchess.model.moves.move_stack

import android.util.Log
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.MutableGame
import dgs.software.classicchess.model.moves.RevertableMove

private val TAG = "MoveStack"

data class MoveStack(
    val moves: MutableList<RevertableMove> = mutableListOf(),
    var iteratorIndex: Int = -1
) {
    fun resetMoveStack(mutableGame: MutableGame) {
        while (iteratorIndex >= 0) {
            moves[iteratorIndex].rollback(mutableGame)
            iteratorIndex--
        }

        moves.clear()
        iteratorIndex = -1
    }

    fun executeMove(mutableGame: MutableGame, move: RevertableMove) {
        Log.d(TAG, "Execute move $move.toString()")
        moves.deleteElementsAfterIndex(iteratorIndex)
        move.execute(mutableGame)
        moves.add(move)
        iteratorIndex = moves.lastIndex
    }

    fun rollbackLastMove(mutableGame: MutableGame): Boolean {
        if (!doneActionsOnStack()) {
            Log.d(
                TAG,
                "Attempted to rollback last move at index ${iteratorIndex}/${moves.size} which was not possible"
            )
            return false
        }
        moves[iteratorIndex].rollback(mutableGame)
        iteratorIndex--
        return true
    }

    fun redoNextMove(mutableGame: MutableGame): Boolean {
        if (!undoneActionsOnStack()) {
            Log.d(
                TAG,
                "Attempted to redo next move at index ${iteratorIndex}/${moves.size} which was not possible"
            )
            return false
        }
        iteratorIndex++
        moves[iteratorIndex].execute(mutableGame)
        return true
    }

    fun doneActionsOnStack(): Boolean {
        return iteratorIndex >= 0
    }

    fun undoneActionsOnStack(): Boolean {
        return iteratorIndex < moves.size - 1
    }

    fun lastMoveWas(fromPos: Coordinate, toPos: Coordinate): Boolean {
        if (iteratorIndex == -1) {
            return false
        }
        val lastMove = moves[iteratorIndex]
        return lastMove.fromPos == fromPos && lastMove.toPos == toPos
    }

    fun anyMoveExecuted() : Boolean {
        return moves.any()
    }

    private fun MutableList<RevertableMove>.deleteElementsAfterIndex(index: Int) {
        if (index < -1 || index >= size) {
            Log.d(TAG,"Attempted to delete element at index $index which is not possible.")
            return;
        }
        while (moves.size - 1 > index) {
            moves.removeLast()
        }
    }
}

fun MoveStack.toImmutableMoveStack(): ImmutableMoveStack {
    return ImmutableMoveStack(
        moves = moves,
        iteratorIndex = iteratorIndex
    )
}