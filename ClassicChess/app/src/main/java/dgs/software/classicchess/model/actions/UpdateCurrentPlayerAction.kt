package dgs.software.classicchess.model.actions

import dgs.software.classicchess.model.Game
import dgs.software.classicchess.model.Player

data class UpdateCurrentPlayerAction(
    val getGame: () -> Game
) : RevertableAction(){
    lateinit var previousState: Player

    override fun execute() {
        super.execute()
        previousState = getGame().currentPlayer
        getGame().updateCurrentPlayer(previousState.opponent())
    }

    override fun rollback() {
        super.rollback()
        getGame().updateCurrentPlayer(previousState)
    }
}