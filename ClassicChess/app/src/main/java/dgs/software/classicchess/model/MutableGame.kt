package dgs.software.classicchess.model

import dgs.software.classicchess.calculations.possiblemoves.BoardStatusProvider
import dgs.software.classicchess.calculations.possiblemoves.GameStatusProvider
import dgs.software.classicchess.model.moves.RevertableMove
import dgs.software.classicchess.model.moves.move_stack.SimulatableMoveStack
import dgs.software.classicchess.utils.random64BitNumber

private const val TAG = "Game"

data class MutableGame(
    val board: Board = Board(),
    var simulatableMoveStack: SimulatableMoveStack = SimulatableMoveStack(),
    var currentPlayer: Player = Player.WHITE
) {
    private val zobristTable = Array(8) { Array(8) { Array(2) { LongArray(12) { random64BitNumber() } } } }
    private val currentPlayerZobristNumber = random64BitNumber()

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

    fun zobristHash(): Long {
        var hash = 0L
        for (i in 0 until 8) {
            for (j in 0 until 8) {
                val piece = board.get(i, j)
                if (piece != null) {
                    val pieceTypeIndex = piece.type.ordinal + piece.player.index * 6
                    val movedIndex = if (piece.isMoved) 1 else 0
                    hash = hash xor zobristTable[i][j][movedIndex][pieceTypeIndex]
                }
            }
        }
        if (currentPlayer == Player.BLACK) {
            hash = hash xor currentPlayerZobristNumber
        }
        return hash
    }

    fun deepCopy() = MutableGame(
        board = board.deepCopy(),
        simulatableMoveStack = simulatableMoveStack.deepCopy(),
        currentPlayer = currentPlayer
    )
}

fun MutableGame.toGame(): Game {
    return Game(
        board = this.board.deepCopy(),
        immutableMoveStack = this.simulatableMoveStack.toImmutableMoveStack(),
        currentPlayer = this.currentPlayer
    )
}