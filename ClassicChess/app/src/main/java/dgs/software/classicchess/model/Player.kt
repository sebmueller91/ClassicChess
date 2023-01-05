package dgs.software.classicchess.model

enum class Player {
    BLACK,
    WHITE;

    fun opponent() : Player {
        return if (this == BLACK) WHITE else BLACK
    }
}