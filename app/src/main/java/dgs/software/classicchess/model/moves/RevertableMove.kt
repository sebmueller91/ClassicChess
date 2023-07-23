package dgs.software.classicchess.model.moves

import dgs.software.classicchess.logger.AndroidLogger
import dgs.software.classicchess.logger.Logger
import dgs.software.classicchess.logger.LoggerFactory
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.MutableGame
import dgs.software.classicchess.model.actions.RevertableAction

private const val TAG = "Move"

abstract class RevertableMove(
    open val fromPos: Coordinate,
    open val toPos: Coordinate,
    open var isExecuted: Boolean = false,
    open val actions: MutableList<RevertableAction> = mutableListOf(),
    protected val logger: Logger = LoggerFactory().create()
) {
    open fun execute(mutableGame: MutableGame) {
        if (isExecuted) {
            logger.e(TAG, "Move execute() called despite already executed")
        }
        isExecuted = true

        for (i in 0 until actions.size) {
            actions[i].execute(mutableGame)
        }
    }

    fun rollback(mutableGame: MutableGame) {
        if (!isExecuted) {
            logger.e(TAG, "Move rollback() called despite not executed")
        }
        isExecuted = false

        for (i in actions.size - 1 downTo 0) {
            actions[i].rollback(mutableGame)
        }
    }

    abstract fun deepCopy(): RevertableMove
}