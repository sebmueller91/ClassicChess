package dgs.software.classicchess.model

class Board {
    private lateinit var grid: Array<Array<Square>>

    init {
        grid = Array(8) {Array (8)  { Square.Empty()} }
    }

    fun get(row: Int, col: Int) : Square {
        return grid[row][col]
    }

    fun set(row: Int, col: Int, square: Square) {
        grid[row][col] = square
    }

    private fun initializeWithDefaultSetup() {
        set(0,0, Square.Piece(Type.ROOK, Player.BLACK));
        set(0,1, Square.Piece(Type.KNIGHT, Player.BLACK));
        set(0,2, Square.Piece(Type.BISHOP, Player.BLACK));
        set(0,3, Square.Piece(Type.QUEEN, Player.BLACK));
        set(0,4, Square.Piece(Type.KING, Player.BLACK));
        set(0,5, Square.Piece(Type.BISHOP, Player.BLACK));
        set(0,6, Square.Piece(Type.KNIGHT, Player.BLACK));
        set(0,7, Square.Piece(Type.ROOK, Player.BLACK));
        set(1,0, Square.Piece(Type.PAWN, Player.BLACK));
        set(1,1, Square.Piece(Type.PAWN, Player.BLACK));
        set(1,2, Square.Piece(Type.PAWN, Player.BLACK));
        set(1,3, Square.Piece(Type.PAWN, Player.BLACK));
        set(1,4, Square.Piece(Type.PAWN, Player.BLACK));
        set(1,5, Square.Piece(Type.PAWN, Player.BLACK));
        set(1,6, Square.Piece(Type.PAWN, Player.BLACK));
        set(1,7, Square.Piece(Type.PAWN, Player.BLACK));

        set(6,0, Square.Piece(Type.PAWN, Player.BLACK));
        set(6,1, Square.Piece(Type.PAWN, Player.BLACK));
        set(6,2, Square.Piece(Type.PAWN, Player.BLACK));
        set(6,3, Square.Piece(Type.PAWN, Player.BLACK));
        set(6,4, Square.Piece(Type.PAWN, Player.BLACK));
        set(6,5, Square.Piece(Type.PAWN, Player.BLACK));
        set(6,6, Square.Piece(Type.PAWN, Player.BLACK));
        set(6,7, Square.Piece(Type.PAWN, Player.BLACK));
        set(7,0, Square.Piece(Type.ROOK, Player.BLACK));
        set(7,1, Square.Piece(Type.KNIGHT, Player.BLACK));
        set(7,2, Square.Piece(Type.BISHOP, Player.BLACK));
        set(7,3, Square.Piece(Type.QUEEN, Player.BLACK));
        set(7,4, Square.Piece(Type.KING, Player.BLACK));
        set(7,5, Square.Piece(Type.BISHOP, Player.BLACK));
        set(7,6, Square.Piece(Type.KNIGHT, Player.BLACK));
        set(7,7, Square.Piece(Type.ROOK, Player.BLACK));
    }
}