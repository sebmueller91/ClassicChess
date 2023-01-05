package dgs.software.classicchess.model

import dgs.software.classicchess.model.moves.MoveStack
import dgs.software.classicchess.model.moves.RevertableMove

data class Game(
    private val board: Board = Board(),
    private val moveStack: MoveStack = MoveStack(),
    private var currentPlayer: Player = Player.WHITE

) {
    fun getBoard() : Board {
        return board
    }

    fun getCurrentPlayer() : Player {
        return currentPlayer
    }

    fun setCurrentPlayer(player: Player) {
        currentPlayer = player
    }

    fun get(coordinate: Coordinate) : Cell {
        return board.get(coordinate)
    }

    fun getAsPiece(coordinate: Coordinate) : Cell.Piece {
        return board.get(coordinate) as Cell.Piece
    }

    fun executeMove(move: RevertableMove, simulateExecution: Boolean = false) {
        moveStack.executeMove(move, simulateExecution)
    }

    fun rollbackSimulatedMoves() {
        moveStack.rollbackSimulatedMoves()
    }
}