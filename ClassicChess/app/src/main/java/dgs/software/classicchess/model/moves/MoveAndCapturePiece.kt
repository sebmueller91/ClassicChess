package dgs.software.classicchess.model.moves

import android.util.Log
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.Game
import dgs.software.classicchess.model.actions.RemovePieceAction
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
        actions.add(RemovePieceAction(toPos, getGame))
        actions.add(MovePieceAction(fromPos, toPos, getGame))
        actions.add(UpdateCurrentPlayerAction(getGame))
    }

    override fun execute() {
        val fromPosPiece = getGame().board.get(fromPos)
        val toPosPiece = getGame().board.get(toPos)
        if (fromPosPiece == null) {
            Log.e(TAG, "Attempting to execute move from empty position from ${fromPos} to ${toPos}")
            return
        }
        if (toPosPiece == null
            || fromPosPiece.player == toPosPiece.player
        ) {
            Log.e(
                TAG,
                "Attempting to execute move to cell which is not the opposing player - from ${fromPos} to ${toPos}"
            )
            return
        }

        super.execute()
    }

    override fun rollback() {
        super.rollback()
    }
}