package dgs.software.classicchess.model.actions

import android.util.Log
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.Game

private val TAG = "SetIsMovedAction"

data class SetIsMovedAction(
    val position: Coordinate,
    val getGame: () -> Game
) : RevertableAction() {
    private var previousState = false

    override fun execute() {
        super.execute()

        val piece = getGame().board.get(position)
        if (piece == null) {
            Log.e(TAG,"Attempting to set isMoved of empty cell ${position}")
            return
        }
        previousState = piece.isMoved
    }

    override fun rollback() {
        super.rollback()

        val piece = getGame().board.get(position)
        when (piece) {
            null -> Log.e(TAG,"Attempting to rollback isMoved of empty cell ${position}")
            else -> piece.isMoved = previousState
        }
    }
}