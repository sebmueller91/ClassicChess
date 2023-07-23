package dgs.software.classicchess.use_cases

import dgs.software.classicchess.logger.Logger
import dgs.software.classicchess.logger.LoggerFactory
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.Game
import dgs.software.classicchess.model.moves.RevertableMove
import dgs.software.classicchess.model.toMutableGame

private const val TAG = "GetPossibleMovesUseCase"

class GetPossibleMovesUseCase(
    private val logger: Logger = LoggerFactory().create()
) {
    fun execute(game: Game, position: Coordinate): List<RevertableMove> {
        val piece = game.board.get(position)
        if (piece == null) {
            logger.e(TAG, "Piece should not be null here - there is something wrong with the code!")
            return listOf()
        }

        return piece.moves.calculatePossibleMovesOfPiece(game.toMutableGame(), position)
    }
}