package dgs.software.classicchess.model.moves

import android.util.Log
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.MutableGame
import dgs.software.classicchess.model.Type
import dgs.software.classicchess.model.actions.RemovePieceAction
import dgs.software.classicchess.model.actions.MovePieceAction
import dgs.software.classicchess.model.actions.SetIsMovedAction
import dgs.software.classicchess.model.actions.UpdateCurrentPlayerAction

private val TAG = "CaptureEnPassantMove"

data class CaptureEnPassantMove(
    override val fromPos: Coordinate,
    override val toPos: Coordinate,
    val capturePiecePos: Coordinate
) : RevertableMove(fromPos, toPos) {
    init {
        actions.add(SetIsMovedAction(fromPos))
        actions.add(RemovePieceAction(capturePiecePos))
        actions.add(MovePieceAction(fromPos, toPos))
        actions.add(UpdateCurrentPlayerAction())
    }

    override fun execute(mutableGame: MutableGame) {
        val fromPosPiece = mutableGame.board.get(fromPos)
        if (fromPosPiece == null) {
            Log.e(TAG,"Attempting to execute en-passant move from empty position from ${fromPos} to ${toPos}")
            return
        }
        if (mutableGame.board.get(toPos) != null) {
            Log.e(TAG,"Attempting to execute en-passant move to non-empty cell from ${fromPos} to ${toPos}")
            return
        }
        mutableGame.board.get(capturePiecePos)?.let {
            if (it.player == fromPosPiece.player) {
                Log.e(TAG,"Attempting to execute en-passant and capture non-player cell cell at ${capturePiecePos}")
                return
            }
            super.execute(mutableGame)
        }
    }

    fun getTypeOfPieceToCapture(mutableGame: MutableGame): Type {
        val piece = mutableGame.board.get(toPos)
        if (piece == null || piece.player == mutableGame.board.get(fromPos)?.player) {
            Log.e(TAG, "Tried to get captured piece of empty pos $toPos")
            return Type.PAWN
        }
        return piece.type
    }
}