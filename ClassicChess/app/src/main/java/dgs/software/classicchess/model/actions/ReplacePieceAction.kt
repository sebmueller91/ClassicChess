package dgs.software.classicchess.model.actions

import android.util.Log
import dgs.software.classicchess.model.Cell
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.Game
import dgs.software.classicchess.model.Type

private const val TAG = "ReplacePieceAction"

data class ReplacePieceAction(
    val position: Coordinate,
    val type: Type,
    val getGame: () -> Game
) : RevertableAction(){
    lateinit var oldPiece: Cell.Piece

    override fun execute() {
        super.execute()
        if (getGame().get(position) is Cell.Empty) {
            Log.e(TAG, "Attempting to prmote a empty cell (execute)")
        }
        oldPiece = getGame().getAsPiece(position)
        getGame().set(position, Cell.Piece(type, oldPiece.player, position, true))
    }

    override fun rollback() {
        super.rollback()
        if (getGame().get(position) is Cell.Empty) {
            Log.e(TAG, "Attempting to rollback pawn promotion on empty cell (rollback)")
        }
        getGame().set(position, oldPiece)
    }
}