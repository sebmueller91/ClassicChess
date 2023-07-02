package dgs.software.classicchess.model.actions

import android.util.Log
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.MutableGame

private val TAG = "SetIsMovedAction"

data class SetIsMovedAction(
    val position: Coordinate,
) : RevertableAction() {
    private var previousState = false

    override fun execute(mutableGame: MutableGame) {
        super.execute(mutableGame)

        val piece = mutableGame.board.get(position)
        if (piece == null) {
            Log.e(TAG,"Attempting to set isMoved of empty cell ${position}")
            return
        }
        previousState = piece.isMoved
    }

    override fun rollback(mutableGame: MutableGame) {
        super.rollback(mutableGame)

        val piece = mutableGame.board.get(position)
        when (piece) {
            null -> Log.e(TAG,"Attempting to rollback isMoved of empty cell ${position}")
            else -> piece.isMoved = previousState
        }
    }
}