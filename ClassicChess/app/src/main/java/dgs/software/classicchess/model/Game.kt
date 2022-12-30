package dgs.software.classicchess.model

data class Game(
    private val board: Board = Board(),
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
}