package dgs.software.classicchess.use_cases

import dgs.software.classicchess.calculations.possiblemoves.PossibleMovesProvider
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.Game
import dgs.software.classicchess.model.moves.RevertableMove
import dgs.software.classicchess.model.toMutableGame

class GetPossibleMovesUseCase(
    private val possibleMovesProvider: PossibleMovesProvider
) {
    fun execute(game: Game, position: Coordinate): List<RevertableMove> {
        val mutableGame = game.toMutableGame()
        return possibleMovesProvider.getPossibleMoves(mutableGame, position)
    }
}