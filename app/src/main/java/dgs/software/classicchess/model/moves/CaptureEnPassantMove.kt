package dgs.software.classicchess.model.moves

import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.MutableGame
import dgs.software.classicchess.model.Type
import dgs.software.classicchess.model.actions.*

private val TAG = "CaptureEnPassantMove"

data class CaptureEnPassantMove(
    override val fromPos: Coordinate,
    override val toPos: Coordinate,
    override var isExecuted: Boolean = false,
    val capturePiecePos: Coordinate,
    override val actions: MutableList<RevertableAction> = mutableListOf()
) : RevertableMove(fromPos, toPos, isExecuted, actions) {
    init {
        if (actions.isEmpty()) {
            actions.add(SetIsMovedAction(fromPos))
            actions.add(RemovePieceAction(capturePiecePos))
            actions.add(MovePieceAction(fromPos, toPos))
            actions.add(UpdateCurrentPlayerAction())
        }
    }

    override fun execute(mutableGame: MutableGame) {
        val fromPosPiece = mutableGame.board.get(fromPos)
        if (fromPosPiece == null) {
            logger.e(TAG,"Attempting to execute en-passant move from empty position from ${fromPos} to ${toPos}")
            return
        }
        if (mutableGame.board.get(toPos) != null) {
            logger.e(TAG,"Attempting to execute en-passant move to non-empty cell from ${fromPos} to ${toPos}")
            return
        }
        mutableGame.board.get(capturePiecePos)?.let {
            if (it.player == fromPosPiece.player) {
                logger.e(TAG,"Attempting to execute en-passant and capture non-player cell cell at ${capturePiecePos}")
                return
            }
            super.execute(mutableGame)
        }
    }

    override fun deepCopy(): CaptureEnPassantMove {
        return CaptureEnPassantMove(
            fromPos = fromPos.copy(),
            toPos = toPos.copy(),
            isExecuted = isExecuted,
            capturePiecePos = capturePiecePos.copy(),
            actions = actions.map {it.deepCopy()}.toMutableList()
        )
    }

    fun getTypeOfPieceToCapture(mutableGame: MutableGame): Type {
        val piece = mutableGame.board.get(toPos)
        if (piece == null || piece.player == mutableGame.board.get(fromPos)?.player) {
            logger.e(TAG, "Tried to get captured piece of empty pos $toPos")
            return Type.PAWN
        }
        return piece.type
    }
}