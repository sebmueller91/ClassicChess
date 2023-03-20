package dgs.software.classicchess.model.moves

import android.util.Log
import dgs.software.classicchess.model.Cell
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.Game
import dgs.software.classicchess.model.Type
import dgs.software.classicchess.model.actions.*

private val TAG = "PromotePawnMove"

data class PromotePawnMove(
    override val fromPos: Coordinate,
    override val toPos: Coordinate,
    val type: Type,
    override val getGame: () -> Game,
    val captureAndMove: Boolean = false
) : RevertableMove(fromPos, toPos, getGame) {
    init {
        // TODO: Log any other than allowed types
        if (captureAndMove) {
            actions.add(RemovePieceAction(toPos, getGame))
        }
        actions.add(MovePieceAction(fromPos, toPos, getGame))
        actions.add(ReplacePieceAction(toPos, type, getGame))
        actions.add(UpdateCurrentPlayerAction(getGame))
    }

    override fun execute() {
        if (getGame().get(fromPos) is Cell.Empty) {
            Log.e(TAG, "Attempting to execute move from empty position from ${fromPos} to ${toPos}")
        if (!captureAndMove && getGame().get(toPos) !is Cell.Empty) {
            Log.e(TAG, "Attempting to execute move to non-empty cell from ${fromPos} to ${toPos} without captureAndMove")
            return
        }
        if (captureAndMove && getGame().get(toPos) is Cell.Empty) {
            Log.e(TAG, "Attempting to execute move to empty cell from ${fromPos} to ${toPos} with captureAndMove")
            return
        }

        super.execute()
    }

    override fun rollback() {
        super.rollback()
    }
}