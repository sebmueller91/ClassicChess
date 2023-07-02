package dgs.software.classicchess.model.moves

import android.util.Log
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.MutableGame
import dgs.software.classicchess.model.actions.MovePieceAction
import dgs.software.classicchess.model.actions.SetIsMovedAction
import dgs.software.classicchess.model.actions.UpdateCurrentPlayerAction

private val TAG = "CastlingMove"

data class CastlingMove(
    val kingFromPos: Coordinate,
    val kingToPos: Coordinate,
    val rookFromPos: Coordinate,
    val rookToPos: Coordinate
) : RevertableMove(fromPos = kingFromPos, toPos = kingToPos) {
    init {
        actions.add(SetIsMovedAction(kingFromPos))
        actions.add(SetIsMovedAction(rookFromPos))
        actions.add(MovePieceAction(kingFromPos, kingToPos))
        actions.add(MovePieceAction(rookFromPos, rookToPos))
        actions.add(UpdateCurrentPlayerAction())
    }

    override fun execute(mutableGame: MutableGame) {
        if (mutableGame.board.get(kingFromPos) == null || mutableGame.board.get(rookFromPos) == null) {
            Log.e(TAG,"Attempting to execute castling from empty position ${kingFromPos}, ${rookFromPos}")
            return
        }
        if (mutableGame.board.get(kingToPos) != null || mutableGame.board.get(rookToPos) != null) {
            Log.e(TAG,"Attempting to execute castling with non empty destination ${kingToPos}, ${kingToPos}")
            return
        }

        super.execute(mutableGame)
    }
}