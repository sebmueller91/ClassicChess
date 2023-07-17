package dgs.software.classicchess.model.actions

import dgs.software.classicchess.model.MutableGame
import dgs.software.classicchess.model.Player

class UpdateCurrentPlayerAction(
    override var isExecuted: Boolean = false
) : RevertableAction(isExecuted){
    var previousState: Player? = null

    override fun execute(mutableGame: MutableGame) {
        super.execute(mutableGame)
        previousState = mutableGame.currentPlayer
        previousState?.let {
            mutableGame.updateCurrentPlayer(it.opponent())
        }
    }

    override fun rollback(mutableGame: MutableGame) {
        super.rollback(mutableGame)
        previousState?.let {
            mutableGame.updateCurrentPlayer(it)
        }
    }

    override fun deepCopy(): UpdateCurrentPlayerAction {
        val copy = UpdateCurrentPlayerAction(
            isExecuted = isExecuted
        )
        previousState?.let {
            copy.previousState = it
        }
        return copy
    }
}