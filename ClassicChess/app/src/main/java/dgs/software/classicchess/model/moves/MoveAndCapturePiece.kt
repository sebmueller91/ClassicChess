package dgs.software.classicchess.model.moves

import android.util.Log
import dgs.software.classicchess.model.Cell
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.Game
import dgs.software.classicchess.model.actions.CapturePieceAction
import dgs.software.classicchess.model.actions.MovePieceAction
import dgs.software.classicchess.model.actions.SetIsMovedAction
import dgs.software.classicchess.model.actions.UpdateCurrentPlayerAction

private val TAG = "MoveAndCapturePiece"

data class MoveAndCapturePiece(
    override val fromPos: Coordinate,
    override val toPos: Coordinate,
    override val getGame: () -> Game
) : RevertableMove(fromPos, toPos, getGame) {
    init {
        actions.add(SetIsMovedAction(fromPos, getGame))
        actions.add(CapturePieceAction(toPos, getGame))
        actions.add(MovePieceAction(fromPos, toPos, getGame))
        actions.add(UpdateCurrentPlayerAction(getGame))
    }

    fun execute() {
        execute(false)
    }

    override fun execute(simulate: Boolean) {
        if (getGame().get(fromPos) is Cell.Empty) {
            Log.e(TAG, "Attempting to execute move from empty position from ${fromPos} to ${toPos}")
            return
        }
        if (!(getGame().get(toPos) is Cell.Piece)
            || getGame().getAsPiece(fromPos).player == getGame().getAsPiece(toPos).player
        ) {
            Log.e(
                TAG,
                "Attempting to execute move to cell which is not the opposing player - from ${fromPos} to ${toPos}"
            )
            return
        }

        super.execute(simulate)
    }

    override fun rollback() {
        super.rollback()
    }
}