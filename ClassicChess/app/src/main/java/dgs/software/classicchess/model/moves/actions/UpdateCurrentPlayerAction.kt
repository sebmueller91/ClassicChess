package dgs.software.classicchess.model.moves.actions

import dgs.software.classicchess.model.Game
import dgs.software.classicchess.model.Player

data class UpdateCurrentPlayerAction(
    val game: Game
) : RevertableAction(){
    lateinit var previousState: Player

    override fun execute() {
        super.execute()
        previousState = game.getCurrentPlayer()
    }

    override fun rollback() {
        super.rollback()
        game.setCurrentPlayer(previousState)
    }
}