package dgs.software.classicchess.model.actions

import android.util.Log
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.Game
import dgs.software.classicchess.model.Piece
import dgs.software.classicchess.model.Type

private const val TAG = "ReplacePieceAction"

data class ReplacePieceAction(
    val position: Coordinate,
    val type: Type,
    val getGame: () -> Game
) : RevertableAction(){
    lateinit var oldPiece: Piece

    override fun execute() {
        super.execute()
        val piece = getGame().board.get(position)
        if (piece == null) {
            Log.e(TAG, "Attempting to prmote a empty cell (execute)")
            return
        }
        oldPiece = piece
        getGame().board.set(position, Piece(type, oldPiece.player, true))
    }

    override fun rollback() {
        super.rollback()
        if (getGame().board.get(position) == null) {
            Log.e(TAG, "Attempting to rollback pawn promotion on empty cell (rollback)")
        }
        getGame().board.set(position, oldPiece)
    }
}