package dgs.software.classicchess.use_cases

import dgs.software.classicchess.model.Game
import dgs.software.classicchess.model.moves.RevertableMove
import dgs.software.classicchess.model.toGame
import dgs.software.classicchess.model.toMutableGame

class ExecuteMoveUseCase {
    fun execute(game: Game, revertableMove: RevertableMove): Game {
        val mutableGame = game.toMutableGame()
        mutableGame.executeMove(revertableMove)
        return mutableGame.toGame()
    }
}