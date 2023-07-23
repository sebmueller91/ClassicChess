package dgs.software.classicchess.model.actions

import dgs.software.classicchess.logger.AndroidLogger
import dgs.software.classicchess.logger.Logger
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.MutableGame
import dgs.software.classicchess.model.Piece

private const val TAG = "RemovePieceAction"

data class RemovePieceAction(
    val coordinate: Coordinate,
    override var isExecuted: Boolean = false
) : RevertableAction(isExecuted) {
    var capturedPiece: Piece? = null

    override fun execute(mutableGame: MutableGame) {
        super.execute(mutableGame)
        val piece = mutableGame.board.get(coordinate)
        if (piece == null) {
            logger.e(TAG, "Attempted to capture empty cell $coordinate.")
            return
        }
        capturedPiece = piece
        mutableGame.board.set(coordinate, null)
    }

    override fun rollback(mutableGame: MutableGame) {
        super.rollback(mutableGame)
        mutableGame.board.set(coordinate, capturedPiece)
    }

    override fun deepCopy(): RemovePieceAction {
        val copy = RemovePieceAction(
            coordinate = coordinate,
            isExecuted = isExecuted
        )
        capturedPiece?.let {
            copy.capturedPiece = it.copy()
        }
        return copy
    }
}