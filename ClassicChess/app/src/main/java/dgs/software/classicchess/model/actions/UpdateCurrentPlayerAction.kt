package dgs.software.classicchess.model.actions

import dgs.software.classicchess.model.Game
import dgs.software.classicchess.model.Player

data class UpdateCurrentPlayerAction(
    val game: Game
) : RevertableAction(){
    lateinit var previousState: Player

    override fun execute() {
        super.execute()
        previousState = game.currentPlayer
        game.updateCurrentPlayer(previousState.opponent())
    }

    override fun rollback() {
        super.rollback()
        game.updateCurrentPlayer(previousState)
    }
}