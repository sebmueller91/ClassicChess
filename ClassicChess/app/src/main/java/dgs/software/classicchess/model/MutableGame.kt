package dgs.software.classicchess.model

import dgs.software.classicchess.calculations.possiblemoves.BoardStatusProvider
import dgs.software.classicchess.calculations.possiblemoves.GameStatusProvider
import dgs.software.classicchess.model.moves.RevertableMove
import dgs.software.classicchess.model.moves.move_stack.SimulatableMoveStack

private const val TAG = "Game"

data class MutableGame(
    val board: Board = Board(),
    var simulatableMoveStack: SimulatableMoveStack = SimulatableMoveStack(),
    var currentPlayer: Player = Player.WHITE
) {
    val boardStatus: BoardStatusProvider
        get() = BoardStatusProvider(this)

    val gameStatus: GameStatusProvider
        get() = GameStatusProvider(this)

    fun undoPreviousMove() {
        simulatableMoveStack.rollbackLastMove(this)
    }

    fun redoNextMove() {
        simulatableMoveStack.redoNextMove(this)
    }

    fun executeMove(move: RevertableMove, simulateExecution: Boolean = false) { // TODO: Throw out simulate execution
        simulatableMoveStack.executeMove(this, move, simulateExecution)
    }

    fun rollbackAndDeleteLastMove(mutableGame: MutableGame) {
        simulatableMoveStack.rollbackAndDeleteLastMove(mutableGame)
    }

    fun rollbackSimulatedMoves() {
        simulatableMoveStack.rollbackSimulatedMoves(this)
    }

    fun updateCurrentPlayer(player: Player) {
        currentPlayer = player
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Game

        if (board != other.board) return false
        if (currentPlayer != other.currentPlayer) return false
        if (!simulatableMoveStack.equals(other)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = board.hashCode()
        result = 31 * result + currentPlayer.hashCode()
        result = 31 * result + simulatableMoveStack.hashCode()
        return result
    }
}

fun MutableGame.toGame(): Game {
    return Game(
        board = this.board,
        immutableMoveStack = this.simulatableMoveStack.toImmutableMoveStack(),
        currentPlayer = this.currentPlayer
    )
}