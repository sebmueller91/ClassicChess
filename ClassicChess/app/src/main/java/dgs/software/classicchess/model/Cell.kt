package dgs.software.classicchess.model

sealed class Cell(
) {
    data class Piece(
        val type: Type,
        val player: Player
    ) : Cell() {}

    class Empty(
    ) : Cell() {}
}

