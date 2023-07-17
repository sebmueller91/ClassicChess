package dgs.software.classicchess.model.moves

import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.MutableGame
import dgs.software.classicchess.model.Type
import dgs.software.classicchess.model.actions.*

private val TAG = "PromotePawnMove"

class PromotePawnMove(
    override val fromPos: Coordinate,
    override val toPos: Coordinate,
    val type: Type,
    val captureAndMove: Boolean = false,
    override var isExecuted: Boolean = false,
    override val actions: MutableList<RevertableAction> = mutableListOf(),
) : RevertableMove(fromPos, toPos, isExecuted, actions) {
    init {
        if (actions.isEmpty()) {
            // TODO: Log any other than allowed types
            if (captureAndMove) {
                actions.add(RemovePieceAction(toPos))
            }
            actions.add(MovePieceAction(fromPos, toPos))
            actions.add(ReplacePieceAction(toPos, type))
            actions.add(UpdateCurrentPlayerAction())
        }
    }

    override fun execute(mutableGame: MutableGame) {
        if (mutableGame.board.get(fromPos) == null) {
            logger.e(TAG, "Attempting to execute move from empty position from ${fromPos} to ${toPos}")
            return
        }
        if (!captureAndMove && mutableGame.board.get(toPos) != null) {
            logger.e(TAG, "Attempting to execute move to non-empty cell from ${fromPos} to ${toPos} without captureAndMove")
            return
        }
        if (captureAndMove && mutableGame.board.get(toPos) == null) {
            logger.e(TAG, "Attempting to execute move to empty cell from ${fromPos} to ${toPos} with captureAndMove")
            return
        }

        super.execute(mutableGame)
    }

    override fun deepCopy(): PromotePawnMove {
        return PromotePawnMove(
            fromPos = fromPos.copy(),
            toPos = toPos.copy(),
            type = type,
            captureAndMove = captureAndMove,
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