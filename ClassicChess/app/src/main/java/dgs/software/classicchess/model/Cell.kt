package dgs.software.classicchess.model

sealed class Cell(
) {
    data class Piece(
        val type: Type,
        val player: Player,
        var isMoved: Boolean = false
    ) : Cell()

    object Empty: Cell()

    fun isPlayer(player: Player) : Boolean {
        return !(this is Empty) && (this as Piece).player == player
    }
}

