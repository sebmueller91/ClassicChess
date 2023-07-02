package dgs.software.classicchess.model.actions

import dgs.software.classicchess.model.MutableGame
import dgs.software.classicchess.model.Player

class UpdateCurrentPlayerAction() : RevertableAction(){
    lateinit var previousState: Player

    override fun execute(mutableGame: MutableGame) {
        super.execute(mutableGame)
        previousState = mutableGame.currentPlayer
        mutableGame.updateCurrentPlayer(previousState.opponent())
    }

    override fun rollback(mutableGame: MutableGame) {
        super.rollback(mutableGame)
        mutableGame.updateCurrentPlayer(previousState)
    }
}