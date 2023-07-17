package dgs.software.classicchess.model

import java.util.*

class Board(
    val board: Array<Array<Piece?>> = Array(8) { Array(8) { null } },
    initializeWithDefaults: Boolean = true
) {
    init {
        if (initializeWithDefaults) {
            initializeWithDefaultSetup()
        }
    }
    fun get(coordinate: Coordinate): Piece? {
        return get(coordinate.row, coordinate.column)
    }

    fun get(row: Int, col: Int): Piece? {
        return board[row][col]
    }

    fun set(coordinate: Coordinate, piece: Piece?) {
        set(coordinate.row, coordinate.column, piece)
    }

    fun set(row: Int, col: Int, piece: Piece?) {
        board[row][col] = piece
    }

    fun isPlayer(coordinate: Coordinate, player: Player): Boolean {
        return isPlayer(coordinate.row, coordinate.column, player)
    }

    fun isPlayer(row: Int, col: Int, player: Player): Boolean {
        return board[row][col]?.player == player
    }

    private fun createPiece(row: Int, col: Int, type: Type, player: Player) {
        board[row][col] = Piece(type, player)
    }

    private fun initializeWithDefaultSetup() {
        for (i in 0 until 8) {
            for (j in 0 until 8) {
                set(i, j, null)
            }
        }

        createPiece(0, 0, Type.ROOK, Player.BLACK);
        createPiece(0, 1, Type.KNIGHT, Player.BLACK);
        createPiece(0, 2, Type.BISHOP, Player.BLACK);
        createPiece(0, 3, Type.QUEEN, Player.BLACK);
        createPiece(0, 4, Type.KING, Player.BLACK);
        createPiece(0, 5, Type.BISHOP, Player.BLACK);
        createPiece(0, 6, Type.KNIGHT, Player.BLACK);
        createPiece(0, 7, Type.ROOK, Player.BLACK);
        createPiece(1, 0, Type.PAWN, Player.BLACK);
        createPiece(1, 1, Type.PAWN, Player.BLACK);
        createPiece(1, 2, Type.PAWN, Player.BLACK);
        createPiece(1, 3, Type.PAWN, Player.BLACK);
        createPiece(1, 4, Type.PAWN, Player.BLACK);
        createPiece(1, 5, Type.PAWN, Player.BLACK);
        createPiece(1, 6, Type.PAWN, Player.BLACK);
        createPiece(1, 7, Type.PAWN, Player.BLACK);

        createPiece(6, 0, Type.PAWN, Player.WHITE);
        createPiece(6, 1, Type.PAWN, Player.WHITE);
        createPiece(6, 2, Type.PAWN, Player.WHITE);
        createPiece(6, 3, Type.PAWN, Player.WHITE);
        createPiece(6, 4, Type.PAWN, Player.WHITE);
        createPiece(6, 5, Type.PAWN, Player.WHITE);
        createPiece(6, 6, Type.PAWN, Player.WHITE);
        createPiece(6, 7, Type.PAWN, Player.WHITE);
        createPiece(7, 0, Type.ROOK, Player.WHITE);
        createPiece(7, 1, Type.KNIGHT, Player.WHITE);
        createPiece(7, 2, Type.BISHOP, Player.WHITE);
        createPiece(7, 3, Type.QUEEN, Player.WHITE);
        createPiece(7, 4, Type.KING, Player.WHITE);
        createPiece(7, 5, Type.BISHOP, Player.WHITE);
        createPiece(7, 6, Type.KNIGHT, Player.WHITE);
        createPiece(7, 7, Type.ROOK, Player.WHITE);
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Board

        if (!board.contentDeepEquals(other.board)) return false

        return true
    }

    override fun hashCode(): Int {
        return board.contentDeepHashCode()
    }

    fun deepCopy() = Board(
        board = board.map { it.clone() }.toTypedArray(),
        initializeWithDefaults = false
    )
}