package dgs.software.classicchess.model

sealed class Cell(
) {
    data class Piece(
        val type: Type,
        val player: Player,
        var isMoved: Boolean = false
    ) : Cell() {}

    class Empty(
    ) : Cell() {}
}

