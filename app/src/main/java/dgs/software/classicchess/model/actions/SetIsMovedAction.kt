package dgs.software.classicchess.model.actions

import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.MutableGame

private val TAG = "SetIsMovedAction"

data class SetIsMovedAction(
    val position: Coordinate,
    override var isExecuted: Boolean = false
) : RevertableAction(isExecuted) {
    var previousState = false

    override fun execute(mutableGame: MutableGame) {
        super.execute(mutableGame)

        val piece = mutableGame.board.get(position)
        if (piece == null) {
            logger.e(TAG,"Attempting to set isMoved of empty cell ${position}")
            return
        }
        previousState = piece.isMoved
    }

    override fun rollback(mutableGame: MutableGame) {
        super.rollback(mutableGame)

        val piece = mutableGame.board.get(position)
        when (piece) {
            null -> logger.e(TAG,"Attempting to rollback isMoved of empty cell ${position}")
            else -> piece.isMoved = previousState
        }
    }

    override fun deepCopy(): SetIsMovedAction {
        val copy = SetIsMovedAction(
            position = position,
            isExecuted = isExecuted
        )
        previousState?.let {
            copy.previousState = previousState
        }
        return copy
    }
}