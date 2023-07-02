package dgs.software.classicchess.model.moves

import android.util.Log
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.MutableGame
import dgs.software.classicchess.model.Type
import dgs.software.classicchess.model.actions.*

private val TAG = "PromotePawnMove"

data class PromotePawnMove(
    override val fromPos: Coordinate,
    override val toPos: Coordinate,
    val type: Type,
    val captureAndMove: Boolean = false
) : RevertableMove(fromPos, toPos) {
    init {
        // TODO: Log any other than allowed types
        if (captureAndMove) {
            actions.add(RemovePieceAction(toPos))
        }
        actions.add(MovePieceAction(fromPos, toPos))
        actions.add(ReplacePieceAction(toPos, type))
        actions.add(UpdateCurrentPlayerAction())
    }

    override fun execute(mutableGame: MutableGame) {
        if (mutableGame.board.get(fromPos) == null) {
            Log.e(TAG, "Attempting to execute move from empty position from ${fromPos} to ${toPos}")
            return
        }
        if (!captureAndMove && mutableGame.board.get(toPos) != null) {
            Log.e(TAG, "Attempting to execute move to non-empty cell from ${fromPos} to ${toPos} without captureAndMove")
            return
        }
        if (captureAndMove && mutableGame.board.get(toPos) == null) {
            Log.e(TAG, "Attempting to execute move to empty cell from ${fromPos} to ${toPos} with captureAndMove")
            return
        }

        super.execute(mutableGame)
    }
}