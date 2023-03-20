package dgs.software.classicchess.model.moves

import android.util.Log
import dgs.software.classicchess.model.Cell
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.Game
import dgs.software.classicchess.model.actions.MovePieceAction
import dgs.software.classicchess.model.actions.SetIsMovedAction
import dgs.software.classicchess.model.actions.UpdateCurrentPlayerAction

private val TAG = "MovePiece"

data class MovePiece(
    override val fromPos: Coordinate,
    override val toPos: Coordinate,
    override val getGame: () -> Game
) : RevertableMove(fromPos, toPos, getGame) {
    init {
        actions.add(SetIsMovedAction(fromPos, getGame))
        actions.add(MovePieceAction(fromPos, toPos, getGame))
        actions.add(UpdateCurrentPlayerAction(getGame))
    }

    override fun execute() {
        if (getGame().get(fromPos) is Cell.Empty) {
            Log.e(TAG,"Attempting to execute move from empty position from ${fromPos} to ${toPos}")
            return
        }
        if (getGame().get(toPos) !is Cell.Empty) {
            Log.e(TAG,"Attempting to execute move to non-empty cell from ${fromPos} to ${toPos}")
            return
        }

        super.execute()
    }

    override fun rollback() {
        super.rollback()
    }
}