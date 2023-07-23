package dgs.software.classicchess.model.actions

import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.MutableGame
import dgs.software.classicchess.model.Piece
import dgs.software.classicchess.model.Type

private const val TAG = "ReplacePieceAction"

data class ReplacePieceAction(
    val position: Coordinate,
    val type: Type,
    override var isExecuted: Boolean = false
) : RevertableAction(isExecuted){
    private var oldPiece: Piece? = null

    override fun execute(mutableGame: MutableGame) {
        super.execute(mutableGame)
        val piece = mutableGame.board.get(position)
        if (piece == null) {
            logger.e(TAG, "Attempting to prmote a empty cell (execute)")
            return
        }
        oldPiece = piece
        oldPiece?.let {
            mutableGame.board.set(position, Piece(type, it.player, true))
        }
    }

    override fun rollback(mutableGame: MutableGame) {
        super.rollback(mutableGame)
        if (mutableGame.board.get(position) == null) {
            logger.e(TAG, "Attempting to rollback pawn promotion on empty cell (rollback)")
        }
        mutableGame.board.set(position, oldPiece)
    }

    override fun deepCopy(): ReplacePieceAction {
        val copy = ReplacePieceAction(
            position = position,
            type = type,
            isExecuted = isExecuted
        )
        oldPiece?.let {
            copy.oldPiece = it.copy()
        }
        return copy
    }
}