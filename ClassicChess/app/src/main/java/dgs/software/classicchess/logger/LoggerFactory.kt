package dgs.software.classicchess.logger

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LoggerFactory : KoinComponent {
    private val logger: Logger by inject()

    fun create(): Logger {
        return logger
    }
}