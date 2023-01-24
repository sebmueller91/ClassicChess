package dgs.software.classicchess.model

data class Coordinate(
    val row: Int,
    val column: Int
) {
    fun isValid(): Boolean {
        return (row in 0..7 && column in 0..7)
    }

    operator fun plus(other: Coordinate): Coordinate {
        return Coordinate(row + other.row, column + other.column)
    }

    fun up(): Coordinate {
        return this.copy(row = row - 1)
    }

    fun down(): Coordinate {
        return this.copy(row = row + 1)
    }

    fun left(): Coordinate {
        return this.copy(column = column - 1)
    }

    fun right(): Coordinate {
        return this.copy(column = column + 1)
    }
}