package dgs.software.classicchess.model.actions

import android.util.Log
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.Game
import dgs.software.classicchess.model.Piece

private const val TAG = "RemovePieceAction"

data class RemovePieceAction(
    val coordinate: Coordinate,
    val getGame: () -> Game
) : RevertableAction() {
    lateinit var capturedPiece: Piece

    override fun execute() {
        super.execute()
        val piece = getGame().board.get(coordinate)
        if (piece == null) {
            Log.e(TAG, "Attempted to capture empty cell $coordinate.")
            return
        }
        capturedPiece = piece
        getGame().board.set(coordinate, null)
    }

    override fun rollback() {
        super.rollback()
        getGame().board.set(coordinate, capturedPiece)
    }
}