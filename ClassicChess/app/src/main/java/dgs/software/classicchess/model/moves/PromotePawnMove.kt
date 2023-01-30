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
    override val getGame: () -> Game
) : RevertableMove(fromPos, toPos, getGame) {
    init {
        // TODO: Log any other than allowed types
        actions.add(RemovePieceAction(toPos, getGame))
        actions.add(MovePieceAction(fromPos, toPos, getGame))
        actions.add(ReplacePieceAction(toPos, type, getGame))
        actions.add(UpdateCurrentPlayerAction(getGame))
    }

    fun execute() {
        execute(false)
    }

    override fun execute(simulate: Boolean) {
        if (getGame().get(fromPos) is Cell.Empty) {
            Log.e(TAG,"Attempting to execute move from empty position from ${fromPos} to ${toPos}")
            return
        }
        if (getGame().get(toPos) !is Cell.Empty) {
            Log.e(TAG,"Attempting to execute move to non-empty cell from ${fromPos} to ${toPos}")
            return
        }

        super.execute(simulate)
    }

    override fun rollback() {
        super.rollback()
    }
}