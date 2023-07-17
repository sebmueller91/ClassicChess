package dgs.software.classicchess.model.moves

import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.MutableGame
import dgs.software.classicchess.model.actions.MovePieceAction
import dgs.software.classicchess.model.actions.RevertableAction
import dgs.software.classicchess.model.actions.SetIsMovedAction
import dgs.software.classicchess.model.actions.UpdateCurrentPlayerAction

private val TAG = "MovePiece"

class MovePiece(
    override val fromPos: Coordinate,
    override val toPos: Coordinate,
    override var isExecuted: Boolean = false,
    override val actions: MutableList<RevertableAction> = mutableListOf()
) : RevertableMove(fromPos, toPos, isExecuted, actions) {
    init {
        if (actions.isEmpty()) {
            actions.add(SetIsMovedAction(fromPos))
            actions.add(MovePieceAction(fromPos, toPos))
            actions.add(UpdateCurrentPlayerAction())
        }
    }

    override fun execute(mutableGame: MutableGame) {
        if (mutableGame.board.get(fromPos) == null) {
            logger.e(TAG,"Attempting to execute move from empty position from ${fromPos} to ${toPos}")
            return
        }
        if (mutableGame.board.get(toPos) != null) {
            logger.e(TAG,"Attempting to execute move to non-empty cell from ${fromPos} to ${toPos}")
            return
        }

        super.execute(mutableGame)
    }

    override fun deepCopy(): MovePiece {
        return MovePiece(
            fromPos = fromPos.copy(),
            toPos = toPos.copy(),
            isExecuted = isExecuted,
            actions = actions.map {it.deepCopy()}.toMutableList()
        )
    }
}