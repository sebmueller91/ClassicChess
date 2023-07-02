package dgs.software.classicchess.model.moves.move_stack

import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.moves.RevertableMove

private val TAG = "ImmutableMoveStack"

data class ImmutableMoveStack(
    val moves: List<RevertableMove> = listOf(),
    val iteratorIndex: Int = -1
) {
    fun doneActionsOnStack(): Boolean {
        return iteratorIndex >= 0
    }

    fun undoneActionsOnStack(): Boolean {
        return iteratorIndex < moves.size - 1
    }

    fun anyMoveExecuted(): Boolean {
        return moves.any()
    }
}

fun ImmutableMoveStack.toSimulatableMoveStack(): SimulatableMoveStack {
    return SimulatableMoveStack(
        moveStack = MoveStack(
            moves = moves.toMutableList(),
            iteratorIndex = iteratorIndex
        )
    )
}