package dgs.software.classicchess.model.actions

import android.util.Log
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.MutableGame
import dgs.software.classicchess.model.Piece
import dgs.software.classicchess.model.Type

private const val TAG = "ReplacePieceAction"

data class ReplacePieceAction(
    val position: Coordinate,
    val type: Type,
) : RevertableAction(){
    lateinit var oldPiece: Piece

    override fun execute(mutableGame: MutableGame) {
        super.execute(mutableGame)
        val piece = mutableGame.board.get(position)
        if (piece == null) {
            Log.e(TAG, "Attempting to prmote a empty cell (execute)")
            return
        }
        oldPiece = piece
        mutableGame.board.set(position, Piece(type, oldPiece.player, true))
    }

    override fun rollback(mutableGame: MutableGame) {
        super.rollback(mutableGame)
        if (mutableGame.board.get(position) == null) {
            Log.e(TAG, "Attempting to rollback pawn promotion on empty cell (rollback)")
        }
        mutableGame.board.set(position, oldPiece)
    }
}