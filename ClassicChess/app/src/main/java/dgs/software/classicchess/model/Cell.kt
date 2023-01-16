package dgs.software.classicchess.model

sealed class Cell(
    open var coordinate: Coordinate
) {
    fun isPlayer(player: Player) : Boolean {
        return !(this is Empty) && (this as Piece).player == player
    }

    data class Piece(
        val type: Type,
        val player: Player,
        override var coordinate: Coordinate = Coordinate(-1,-1),
        var isMoved: Boolean = false
    ) : Cell(coordinate) {
    }

    data class Empty(
        override var coordinate: Coordinate = Coordinate(-1,-1),
    ) : Cell(coordinate) {}
}

