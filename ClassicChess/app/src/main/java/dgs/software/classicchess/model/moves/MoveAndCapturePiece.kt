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
    val game: Game
) : RevertableMove(fromPos, toPos, game.getBoard()) {
    init {
        actions.add(SetIsMovedAction(board, fromPos))
        actions.add(CapturePieceAction(board, toPos))
        actions.add(MovePieceAction(board, fromPos, toPos))
        actions.add(UpdateCurrentPlayerAction(game))
    }

    override fun execute() {
        if (board.get(fromPos) is Cell.Empty) {
            Log.e(TAG, "Attempting to execute move from empty position from ${fromPos} to ${toPos}")
            return
        }
        if (!(board.get(toPos) is Cell.Piece)
            || game.getAsPiece(fromPos).player == game.getAsPiece(toPos).player
        ) {
            Log.e(TAG, "Attempting to execute move to cell which is not the opposing player - from ${fromPos} to ${toPos}")
            return
        }

        super.execute()
    }

    override fun rollback() {
        super.rollback()
    }
}