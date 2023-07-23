package dgs.software.classicchess.model.actions

import dgs.software.classicchess.logger.AndroidLogger
import dgs.software.classicchess.logger.Logger
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.MutableGame
import org.koin.androidx.compose.get

private const val TAG = "MovePieceAction"

data class MovePieceAction(
    val fromPos: Coordinate,
    val toPos: Coordinate,
    override var isExecuted: Boolean = false
) : RevertableAction(isExecuted) {
    override fun execute(mutableGame: MutableGame) {
        super.execute(mutableGame)
        if (mutableGame.board.get(toPos) != null) {
            logger.e(TAG, "Attempting to move piece into non-empty cell (execute)")
            return
        }
        if (mutableGame.board.get(fromPos) == null) {
            logger.e(TAG, "Attempting to move empty piece from $fromPos to $toPos")
            return
        }
        mutableGame.board.set(toPos, mutableGame.board.get(fromPos))
        mutableGame.board.set(fromPos, null)
    }

    override fun rollback(mutableGame: MutableGame) {
        super.rollback(mutableGame)
        if (mutableGame.board.get(fromPos) != null) {
            logger.e(TAG, "Attempting to move piece into non-empty cell (rollback)")
        }
        mutableGame.board.set(fromPos, mutableGame.board.get(toPos))
        mutableGame.board.set(toPos, null)
    }

    override fun deepCopy(): MovePieceAction {
        return MovePieceAction(
            fromPos = fromPos,
            toPos = toPos,
            isExecuted = isExecuted
        )
    }
}