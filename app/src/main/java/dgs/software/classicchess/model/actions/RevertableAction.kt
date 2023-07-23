package dgs.software.classicchess.model.actions

import dgs.software.classicchess.logger.Logger
import dgs.software.classicchess.logger.LoggerFactory
import dgs.software.classicchess.model.MutableGame
import org.koin.androidx.compose.get
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private const val TAG = "RevertableAction"

abstract class RevertableAction(
    open var isExecuted: Boolean = false,
    protected val logger: Logger = LoggerFactory().create()
) {
    open fun execute(mutableGame: MutableGame) {
        if (isExecuted) {
            logger.e(TAG, "RevertableAction execute() called despite already executed")
        }
        isExecuted = true
    }

    open fun rollback(mutableGame: MutableGame) {
        if (!isExecuted) {
            logger.e(TAG, "RevertableAction rollback() called despite not executed")
        }
        isExecuted = false
    }

    abstract fun deepCopy(): RevertableAction
}