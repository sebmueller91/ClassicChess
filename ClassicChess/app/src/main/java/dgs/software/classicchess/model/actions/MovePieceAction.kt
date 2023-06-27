package dgs.software.classicchess.model.actions

import android.util.Log
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
        if (getGame().board.get(toPos) != null) {
            Log.e(TAG, "Attempting to move piece into non-empty cell (execute)")
            return
        }
        if (getGame().board.get(fromPos) == null) {
            Log.e(TAG, "Attempting to move empty piece from $fromPos to $toPos")
            return
        }
        getGame().board.set(toPos, getGame().board.get(fromPos))
        getGame().board.set(fromPos, null)
    }

    override fun rollback() {
        super.rollback()
        if (getGame().board.get(fromPos) != null) {
            Log.e(TAG, "Attempting to move piece into non-empty cell (rollback)")
        }
        getGame().board.set(fromPos, getGame().board.get(toPos))
        getGame().board.set(toPos, null)
    }
}