package dgs.software.classicchess.model.moves

import android.util.Log
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.MutableGame
import dgs.software.classicchess.model.actions.MovePieceAction
import dgs.software.classicchess.model.actions.SetIsMovedAction
import dgs.software.classicchess.model.actions.UpdateCurrentPlayerAction

private val TAG = "MovePiece"

data class MovePiece(
    override val fromPos: Coordinate,
    override val toPos: Coordinate
) : RevertableMove(fromPos, toPos) {
    init {
        actions.add(SetIsMovedAction(fromPos))
        actions.add(MovePieceAction(fromPos, toPos))
        actions.add(UpdateCurrentPlayerAction())
    }

    override fun execute(mutableGame: MutableGame) {
        if (mutableGame.board.get(fromPos) == null) {
            Log.e(TAG,"Attempting to execute move from empty position from ${fromPos} to ${toPos}")
            return
        }
        if (mutableGame.board.get(toPos) != null) {
            Log.e(TAG,"Attempting to execute move to non-empty cell from ${fromPos} to ${toPos}")
            return
        }

        super.execute(mutableGame)
    }
}