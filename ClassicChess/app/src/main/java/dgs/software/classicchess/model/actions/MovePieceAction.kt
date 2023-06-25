package dgs.software.classicchess.model.actions

import android.util.Log
import dgs.software.classicchess.model.Board
import dgs.software.classicchess.model.Cell
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.Game

private const val TAG = "MovePieceAction"

data class MovePieceAction(
    val fromPos: Coordinate,
    val toPos: Coordinate,
    val getGame: () -> Game
) : RevertableAction(){
    override fun execute() {
        super.execute()
        if (!(getGame().get(toPos) is Cell.Empty)) {
            Log.e(TAG, "Attempting to move piece into non-empty cell (execute)")
        }
        getGame().set(toPos, getGame().get(fromPos) as Cell.Piece)
        getGame().set(fromPos, Cell.Empty)
    }

    override fun rollback() {
        super.rollback()
        if (!(getGame().get(fromPos) is Cell.Empty)) {
            Log.e(TAG, "Attempting to move piece into non-empty cell (rollback)")
        }
        getGame().set(fromPos, getGame().get(toPos))
        getGame().set(toPos, Cell.Empty)
    }
}