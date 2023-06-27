package dgs.software.classicchess.model.moves

import android.util.Log
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.Game
import dgs.software.classicchess.model.actions.MovePieceAction
import dgs.software.classicchess.model.actions.SetIsMovedAction
import dgs.software.classicchess.model.actions.UpdateCurrentPlayerAction

private val TAG = "CastlingMove"

data class CastlingMove(
    val kingFromPos: Coordinate,
    val kingToPos: Coordinate,
    val rookFromPos: Coordinate,
    val rookToPos: Coordinate,
    override val getGame: () -> Game
) : RevertableMove(fromPos = kingFromPos, toPos = kingToPos, getGame) {
    init {
        actions.add(SetIsMovedAction(kingFromPos, getGame))
        actions.add(SetIsMovedAction(rookFromPos, getGame))
        actions.add(MovePieceAction(kingFromPos, kingToPos, getGame))
        actions.add(MovePieceAction(rookFromPos, rookToPos, getGame))
        actions.add(UpdateCurrentPlayerAction(getGame))
    }

    override fun execute() {
        if (getGame().board.get(kingFromPos) == null || getGame().board.get(rookFromPos) == null) {
            Log.e(TAG,"Attempting to execute castling from empty position ${kingFromPos}, ${rookFromPos}")
            return
        }
        if (getGame().board.get(kingToPos) != null || getGame().board.get(rookToPos) != null) {
            Log.e(TAG,"Attempting to execute castling with non empty destination ${kingToPos}, ${kingToPos}")
            return
        }

        super.execute()
    }

    override fun rollback() {
        super.rollback()
    }
}