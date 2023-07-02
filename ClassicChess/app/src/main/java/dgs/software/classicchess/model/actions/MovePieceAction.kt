package dgs.software.classicchess.model.actions

import android.util.Log
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.MutableGame

private const val TAG = "MovePieceAction"

data class MovePieceAction(
    val fromPos: Coordinate,
    val toPos: Coordinate,
) : RevertableAction(){
    override fun execute(mutableGame: MutableGame) {
        super.execute(mutableGame)
        if (mutableGame.board.get(toPos) != null) {
            Log.e(TAG, "Attempting to move piece into non-empty cell (execute)")
            return
        }
        if (mutableGame.board.get(fromPos) == null) {
            Log.e(TAG, "Attempting to move empty piece from $fromPos to $toPos")
            return
        }
        mutableGame.board.set(toPos, mutableGame.board.get(fromPos))
        mutableGame.board.set(fromPos, null)
    }

    override fun rollback(mutableGame: MutableGame) {
        super.rollback(mutableGame)
        if (mutableGame.board.get(fromPos) != null) {
            Log.e(TAG, "Attempting to move piece into non-empty cell (rollback)")
        }
        mutableGame.board.set(fromPos, mutableGame.board.get(toPos))
        mutableGame.board.set(toPos, null)
    }
}