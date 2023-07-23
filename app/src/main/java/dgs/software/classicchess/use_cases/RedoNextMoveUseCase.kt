package dgs.software.classicchess.use_cases

import dgs.software.classicchess.model.Game
import dgs.software.classicchess.model.toGame
import dgs.software.classicchess.model.toMutableGame

class RedoNextMoveUseCase {
    fun execute(game: Game): Game {
        val mutableGame = game.toMutableGame()
        mutableGame.redoNextMove()
        return mutableGame.toGame()
    }
}