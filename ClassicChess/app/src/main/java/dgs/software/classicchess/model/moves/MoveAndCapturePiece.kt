package dgs.software.classicchess.model.moves

import android.util.Log
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.MutableGame
import dgs.software.classicchess.model.actions.RemovePieceAction
import dgs.software.classicchess.model.actions.MovePieceAction
import dgs.software.classicchess.model.actions.SetIsMovedAction
import dgs.software.classicchess.model.actions.UpdateCurrentPlayerAction

private val TAG = "MoveAndCapturePiece"

data class MoveAndCapturePiece(
    override val fromPos: Coordinate,
    override val toPos: Coordinate
) : RevertableMove(fromPos, toPos) {
    init {
        actions.add(SetIsMovedAction(fromPos))
        actions.add(RemovePieceAction(toPos))
        actions.add(MovePieceAction(fromPos, toPos))
        actions.add(UpdateCurrentPlayerAction())
    }

    override fun execute(mutableGame: MutableGame) {
        val fromPosPiece = mutableGame.board.get(fromPos)
        val toPosPiece = mutableGame.board.get(toPos)
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

        super.execute(mutableGame)
    }
}