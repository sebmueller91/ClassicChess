package dgs.software.classicchess.model

sealed class Cell(
) {
    fun isPlayer(player: Player) : Boolean {
        return !(this is Empty) && (this as Piece).player == player
    }

    data class Piece(
        val type: Type,
        val player: Player,
        var isMoved: Boolean = false,
        var coordinate: Coordinate = Coordinate(-1,-1)
    ) : Cell() {
    }

    class Empty(
    ) : Cell() {}
}

