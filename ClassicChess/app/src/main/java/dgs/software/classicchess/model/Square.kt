package dgs.software.classicchess.model

sealed class Square(
) { // TODO: Think about better name
    data class Piece(
        val type: Type,
        val player: Player
    ) : Square() {}

    class Empty(
    ) : Square() {}
}

