package dgs.software.classicchess.model

import dgs.software.classicchess.model.moves.RevertableMove
import dgs.software.classicchess.model.moves.move_stack.SimulatableMoveStack

private const val TAG = "Game"

data class MutableGame(
    val board: Board = Board(),
    var simulatableMoveStack: SimulatableMoveStack = SimulatableMoveStack(),
    var currentPlayer: Player = Player.WHITE
) {
    fun undoPreviousMove() {
        simulatableMoveStack.rollbackLastMove(this)
    }

    fun redoNextMove() {
        simulatableMoveStack.redoNextMove(this)
    }

    fun executeMove(move: RevertableMove, simulateExecution: Boolean = false) {
        simulatableMoveStack.executeMove(this, move, simulateExecution)
    }

    fun rollbackSimulatedMoves() {
        simulatableMoveStack.rollbackSimulatedMoves(this)
    }

    fun updateCurrentPlayer(player: Player) {
        currentPlayer = player
    }
}

fun MutableGame.toGame(): Game {
    return Game(
        board = this.board,
        immutableMoveStack = this.simulatableMoveStack.toImmutableMoveStack(),
        currentPlayer = this.currentPlayer
    )
}