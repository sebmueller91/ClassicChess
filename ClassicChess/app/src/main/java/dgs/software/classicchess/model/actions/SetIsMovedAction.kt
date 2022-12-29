package dgs.software.classicchess.model.actions

import android.util.Log
import dgs.software.classicchess.model.Board
import dgs.software.classicchess.model.Cell
import dgs.software.classicchess.model.Coordinate

private val TAG = "SetIsMovedAction"

data class SetIsMovedAction(
    val board: Board,
    val position: Coordinate
) : RevertableAction() {
    private var previousState = false

    override fun execute() {
        super.execute()

        if (board.get(position) is Cell.Empty) {
            Log.e(TAG,"Attempting to set isMoved of empty cell ${position}")
            return
        }

        val cell = board.get(position)
        when (cell) {
            is Cell.Empty -> Log.e(TAG,"Attempting to set isMoved of empty cell ${position}")
            is Cell.Piece -> previousState = cell.isMoved
        }
    }

    override fun rollback() {
        super.rollback()

        val cell = board.get(position)
        when (cell) {
            is Cell.Empty -> Log.e(TAG,"Attempting to rollback isMoved of empty cell ${position}")
            is Cell.Piece -> cell.isMoved = previousState
        }
    }
}