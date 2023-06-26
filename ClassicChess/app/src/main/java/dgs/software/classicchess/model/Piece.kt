package dgs.software.classicchess.model

data class Piece(
    val type: Type,
    val player: Player,
    var isMoved: Boolean = false
)

