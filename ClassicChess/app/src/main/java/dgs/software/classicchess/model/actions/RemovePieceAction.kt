package dgs.software.classicchess.model.actions

import android.util.Log
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.MutableGame
import dgs.software.classicchess.model.Piece

private const val TAG = "RemovePieceAction"

data class RemovePieceAction(
    val coordinate: Coordinate,
) : RevertableAction() {
    lateinit var capturedPiece: Piece

    override fun execute(mutableGame: MutableGame) {
        super.execute(mutableGame)
        val piece = mutableGame.board.get(coordinate)
        if (piece == null) {
            Log.e(TAG, "Attempted to capture empty cell $coordinate.")
            return
        }
        capturedPiece = piece
        mutableGame.board.set(coordinate, null)
    }

    override fun rollback(mutableGame: MutableGame) {
        super.rollback(mutableGame)
        mutableGame.board.set(coordinate, capturedPiece)
    }
}