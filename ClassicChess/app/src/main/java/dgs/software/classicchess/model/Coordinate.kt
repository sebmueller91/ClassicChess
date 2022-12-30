package dgs.software.classicchess.model

data class Coordinate(
    val row: Int,
    val column: Int
) {
    fun isValid() : Boolean {
        return (row in 0..7 && column in 0..7)
    }
}