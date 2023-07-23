package dgs.software.classicchess.model

import dgs.software.classicchess.model.moves.move_stack.ImmutableMoveStack
import dgs.software.classicchess.model.moves.move_stack.toSimulatableMoveStack

data class Game(
    val board: Board = Board(),
    val immutableMoveStack: ImmutableMoveStack = ImmutableMoveStack(),
    val currentPlayer: Player = Player.WHITE
) {

    val canUndoMove: Boolean
        get() = immutableMoveStack.doneActionsOnStack()

    val canRedoMove: Boolean
        get() = immutableMoveStack.undoneActionsOnStack()

    fun anyMoveExecuted(): Boolean {
        return immutableMoveStack.anyMoveExecuted()
    }
}

fun Game.toMutableGame(): MutableGame {
    return MutableGame(
        board = this.board.deepCopy(),
        simulatableMoveStack = this.immutableMoveStack.toSimulatableMoveStack(),
        currentPlayer = this.currentPlayer
    )
}