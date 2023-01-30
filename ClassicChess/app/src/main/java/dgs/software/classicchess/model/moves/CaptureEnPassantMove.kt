package dgs.software.classicchess.model.moves

import android.util.Log
import dgs.software.classicchess.model.Cell
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.Game
import dgs.software.classicchess.model.actions.RemovePieceAction
import dgs.software.classicchess.model.actions.MovePieceAction
import dgs.software.classicchess.model.actions.SetIsMovedAction
import dgs.software.classicchess.model.actions.UpdateCurrentPlayerAction

private val TAG = "CaptureEnPassantMove"

data class CaptureEnPassantMove(
    override val fromPos: Coordinate,
    override val toPos: Coordinate,
    val capturePiecePos: Coordinate,
    override val getGame: () -> Game
) : RevertableMove(fromPos, toPos, getGame) {
    init {
        actions.add(SetIsMovedAction(fromPos, getGame))
        actions.add(RemovePieceAction(capturePiecePos, getGame))
        actions.add(MovePieceAction(fromPos, toPos, getGame))
        actions.add(UpdateCurrentPlayerAction(getGame))
    }

    fun execute() {
        execute(false)
    }

    override fun execute(simulate: Boolean) {
        if (getGame().get(fromPos) is Cell.Empty) {
            Log.e(TAG,"Attempting to execute en-passant move from empty position from ${fromPos} to ${toPos}")
            return
        }
        if (getGame().get(toPos) !is Cell.Empty) {
            Log.e(TAG,"Attempting to execute en-passant move to non-empty cell from ${fromPos} to ${toPos}")
            return
        }
        if (getGame().get(capturePiecePos) is Cell.Empty
            || getGame().getAsPiece(capturePiecePos).player == getGame().getAsPiece(fromPos).player) {
            Log.e(TAG,"Attempting to execute en-passant and capture empty or non-player cell cell at ${capturePiecePos}")
            return
        }

        super.execute(simulate)
    }

    override fun rollback() {
        super.rollback()
    }
}