package dgs.software.classicchess.model.moves

import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.MutableGame
import dgs.software.classicchess.model.actions.MovePieceAction
import dgs.software.classicchess.model.actions.RevertableAction
import dgs.software.classicchess.model.actions.SetIsMovedAction
import dgs.software.classicchess.model.actions.UpdateCurrentPlayerAction

private val TAG = "CastlingMove"

data class CastlingMove(
    val kingFromPos: Coordinate,
    val kingToPos: Coordinate,
    val rookFromPos: Coordinate,
    val rookToPos: Coordinate,
    override var isExecuted: Boolean = false,
    override val actions: MutableList<RevertableAction> = mutableListOf()
) : RevertableMove(fromPos = kingFromPos, toPos = kingToPos, isExecuted = isExecuted, actions = actions) {
    init {
        if (actions.isEmpty()) {
            actions.add(SetIsMovedAction(kingFromPos))
            actions.add(SetIsMovedAction(rookFromPos))
            actions.add(MovePieceAction(kingFromPos, kingToPos))
            actions.add(MovePieceAction(rookFromPos, rookToPos))
            actions.add(UpdateCurrentPlayerAction())
        }
    }

    override fun execute(mutableGame: MutableGame) {
        if (mutableGame.board.get(kingFromPos) == null || mutableGame.board.get(rookFromPos) == null) {
            logger.e(TAG,"Attempting to execute castling from empty position ${kingFromPos}, ${rookFromPos}")
            return
        }
        if (mutableGame.board.get(kingToPos) != null || mutableGame.board.get(rookToPos) != null) {
            logger.e(TAG,"Attempting to execute castling with non empty destination ${kingToPos}, ${kingToPos}")
            return
        }

        super.execute(mutableGame)
    }

    override fun deepCopy(): CastlingMove {
        return CastlingMove(
            kingFromPos = kingFromPos.copy(),
            kingToPos = kingToPos.copy(),
            rookFromPos = rookFromPos.copy(),
            rookToPos = rookToPos.copy(),
            isExecuted = isExecuted,
            actions = actions.map {it.deepCopy()}.toMutableList()
        )
    }
}