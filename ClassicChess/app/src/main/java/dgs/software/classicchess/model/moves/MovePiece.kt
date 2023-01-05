package dgs.software.classicchess.model.moves

import android.util.Log
import dgs.software.classicchess.model.Board
import dgs.software.classicchess.model.Cell
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.Game
import dgs.software.classicchess.model.actions.MovePieceAction
import dgs.software.classicchess.model.actions.SetIsMovedAction
import dgs.software.classicchess.model.actions.UpdateCurrentPlayerAction

private val TAG = "MovePiece"

data class MovePiece(
    override val fromPos: Coordinate,
    override val toPos: Coordinate,
    val game: Game
) : RevertableMove(fromPos, toPos, game.getBoard()) {
    init {
        actions.add(SetIsMovedAction(board, fromPos))
        actions.add(MovePieceAction(board, fromPos, toPos))
        actions.add(UpdateCurrentPlayerAction(game))
    }

    fun execute() {
        execute(false)
    }

    override fun execute(simmulate: Boolean) {
        if (board.get(fromPos) is Cell.Empty) {
            Log.e(TAG,"Attempting to execute move from empty position from ${fromPos} to ${toPos}")
            return
        }
        if (!(board.get(toPos) is Cell.Empty)) {
            Log.e(TAG,"Attempting to execute move to non-empty cell from ${fromPos} to ${toPos}")
            return
        }

        super.execute(simmulate)
    }

    override fun rollback() {
        super.rollback()
    }
}