package dgs.software.classicchess.model

enum class Player(val index: Int) {
    BLACK(0),
    WHITE(1);

    fun opponent() : Player {
        return if (this == BLACK) WHITE else BLACK
    }
}