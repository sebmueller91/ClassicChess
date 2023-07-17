package dgs.software.classicchess.model.moves

import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.MutableGame
import dgs.software.classicchess.model.Type
import dgs.software.classicchess.model.actions.*

private val TAG = "MoveAndCapturePiece"

data class MoveAndCapturePiece(
    override val fromPos: Coordinate,
    override val toPos: Coordinate,
    override var isExecuted: Boolean = false,
    override val actions: MutableList<RevertableAction> = mutableListOf()
) : RevertableMove(fromPos, toPos, isExecuted, actions) {
    init {
        if (actions.isEmpty()) {
            actions.add(SetIsMovedAction(fromPos))
            actions.add(RemovePieceAction(toPos))
            actions.add(MovePieceAction(fromPos, toPos))
            actions.add(UpdateCurrentPlayerAction())
        }
    }

    override fun execute(mutableGame: MutableGame) {
        val fromPosPiece = mutableGame.board.get(fromPos)
        val toPosPiece = mutableGame.board.get(toPos)
        if (fromPosPiece == null) {
            logger.e(TAG, "Attempting to execute move from empty position from ${fromPos} to ${toPos}")
            return
        }
        if (toPosPiece == null
            || fromPosPiece.player == toPosPiece.player
        ) {
            logger.e(
                TAG,
                "Attempting to execute move to cell which is not the opposing player - from ${fromPos} to ${toPos}"
            )
            return
        }

        super.execute(mutableGame)
    }

    override fun deepCopy(): MoveAndCapturePiece {
        return MoveAndCapturePiece(
            fromPos = fromPos.copy(),
            toPos = toPos.copy(),
            isExecuted = isExecuted,
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