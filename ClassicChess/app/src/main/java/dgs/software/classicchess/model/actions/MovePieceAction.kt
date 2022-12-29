package dgs.software.classicchess.model.actions

import android.util.Log
import dgs.software.classicchess.model.Board
import dgs.software.classicchess.model.Cell
import dgs.software.classicchess.model.Coordinate

private const val TAG = "MovePieceAction"

data class MovePieceAction(
    val board: Board,
    val fromPos: Coordinate,
    val toPos: Coordinate
) : RevertableAction(){
    override fun execute() {
        super.execute()
        if (!(board.get(toPos) is Cell.Empty)) {
            Log.e(TAG, "Attempting to move piece into non-empty cell (execute)")
        }
        board.set(toPos, board.get(fromPos))
        board.set(fromPos, Cell.Empty())
    }

    override fun rollback() {
        super.rollback()
        if (!(board.get(fromPos) is Cell.Empty)) {
            Log.e(TAG, "Attempting to move piece into non-empty cell (rollback)")
        }
        board.set(fromPos, board.get(toPos))
        board.set(toPos, Cell.Empty())
    }
}