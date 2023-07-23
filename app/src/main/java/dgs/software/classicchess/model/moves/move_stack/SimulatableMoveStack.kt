package dgs.software.classicchess.model.moves.move_stack

import dgs.software.classicchess.logger.AndroidLogger
import dgs.software.classicchess.logger.Logger
import dgs.software.classicchess.logger.LoggerFactory
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.Game
import dgs.software.classicchess.model.MutableGame
import dgs.software.classicchess.model.moves.RevertableMove

private val TAG = "SimulatableMoveStack"

data class SimulatableMoveStack(
    val moveStack: MoveStack = MoveStack(),
    val simulatedMoveStack: MoveStack = MoveStack(),
    private val logger: Logger = LoggerFactory().create()
) {
    fun executeMove(mutableGame: MutableGame, move: RevertableMove, simulateExecution: Boolean = false) {
        logger.d(TAG, "Execute move $move.toString(), simulated: $simulateExecution")
        if (simulateExecution) {
            simulatedMoveStack.executeMove(mutableGame, move)
        } else {
            if (simulatedMoveStack.doneActionsOnStack()) {
                logger.e(TAG, "Executing move while simulated moves on stack, this should not happen!")
                simulatedMoveStack.resetMoveStack(mutableGame)
            }
            moveStack.executeMove(mutableGame, move)
        }
    }

    fun rollbackSimulatedMoves(mutableGame: MutableGame) {
        simulatedMoveStack.resetMoveStack(mutableGame)
    }

    fun rollbackLastMove(mutableGame: MutableGame): Boolean {
        if (!doneActionsOnStack()) {
            logger.d(
                TAG,
                "Attempted to rollback last move which was not possible"
            )
            return false
        }
        if (simulatedMoveStack.doneActionsOnStack()) {
            logger.e(
                TAG,
                "Rolling back move while simulated moveStack is not empty, this should not happen!"
            )
            simulatedMoveStack.rollbackLastMove(mutableGame)
        } else {
            moveStack.rollbackLastMove(mutableGame)
        }
        return true
    }

    fun rollbackAndDeleteLastMove(mutableGame: MutableGame): Boolean {
        return moveStack.rollbackAndDeleteLastMove(mutableGame)
    }

    fun redoNextMove(mutableGame: MutableGame): Boolean {
        if (!undoneActionsOnStack()) {
            logger.d(
                TAG,
                "Attempted to redo next move which was not possible"
            )
            return false
        }
        if (simulatedMoveStack.doneActionsOnStack()) {
            logger.e(TAG, "Redoing move while simulated moveStack is not empty, this should not happen!")
        }
        if (simulatedMoveStack.undoneActionsOnStack()) {
            simulatedMoveStack.redoNextMove(mutableGame)
        } else {
            moveStack.redoNextMove(mutableGame)
        }
        return true
    }

    fun doneActionsOnStack(): Boolean {
        return simulatedMoveStack.doneActionsOnStack() || moveStack.doneActionsOnStack()
    }

    fun undoneActionsOnStack(): Boolean {
        return simulatedMoveStack.undoneActionsOnStack() || moveStack.undoneActionsOnStack()
    }

    fun lastMoveWas(fromPos: Coordinate, toPos: Coordinate): Boolean {
        if (!doneActionsOnStack()) {
            return false
        }
        return if (simulatedMoveStack.doneActionsOnStack()) {
            simulatedMoveStack.lastMoveWas(fromPos, toPos)
        } else {
            moveStack.lastMoveWas(fromPos, toPos)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Game

        if (moveStack.moves.last() != other.immutableMoveStack.moves.last()) return false

        return true
    }

    override fun hashCode(): Int {
        if (moveStack.moves.isEmpty()) {
            return 0
        }
        return moveStack.moves.last().hashCode()
    }

    fun toImmutableMoveStack(): ImmutableMoveStack {
        return moveStack.toImmutableMoveStack()
    }

    fun deepCopy() = SimulatableMoveStack(
        moveStack = moveStack.deepCopy(),
        simulatedMoveStack = simulatedMoveStack.deepCopy()
    )
}