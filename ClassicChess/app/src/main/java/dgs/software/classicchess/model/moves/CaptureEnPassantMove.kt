package dgs.software.classicchess.model.moves

import android.util.Log
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

    override fun execute() {
        val fromPosPiece = getGame().board.get(fromPos)
        if (fromPosPiece == null) {
            Log.e(TAG,"Attempting to execute en-passant move from empty position from ${fromPos} to ${toPos}")
            return
        }
        if (getGame().board.get(toPos) != null) {
            Log.e(TAG,"Attempting to execute en-passant move to non-empty cell from ${fromPos} to ${toPos}")
            return
        }
        getGame().board.get(capturePiecePos)?.let {
            if (it.player == fromPosPiece.player) {
                Log.e(TAG,"Attempting to execute en-passant and capture non-player cell cell at ${capturePiecePos}")
                return
            }
            super.execute()
        }
    }

    override fun rollback() {
        super.rollback()
    }
}