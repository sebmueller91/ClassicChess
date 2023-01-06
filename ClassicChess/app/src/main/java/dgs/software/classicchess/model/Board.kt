package dgs.software.classicchess.model

class Board {
    private lateinit var grid: Array<Array<Cell>>

    init {
        grid = Array(8) {Array (8)  { Cell.Empty()} }
        initializeWithDefaultSetup()
    }

    fun get(coordinate: Coordinate) : Cell {
        return get(coordinate.row, coordinate.column)
    }

    fun get(row: Int, col: Int) : Cell {
        return grid[row][col]
    }
    
    fun set(coordinate: Coordinate, square: Cell) {
        set(coordinate.row, coordinate.column, square)
        if (square is Cell.Piece) {
            square.coordinate = coordinate
        }
    }

    fun set(row: Int, col: Int, square: Cell) {
        grid[row][col] = square
    }

    private fun initializeWithDefaultSetup() {
        set(0,0, Cell.Piece(Type.ROOK, Player.BLACK));
        set(0,1, Cell.Piece(Type.KNIGHT, Player.BLACK));
        set(0,2, Cell.Piece(Type.BISHOP, Player.BLACK));
        set(0,3, Cell.Piece(Type.QUEEN, Player.BLACK));
        set(0,4, Cell.Piece(Type.KING, Player.BLACK));
        set(0,5, Cell.Piece(Type.BISHOP, Player.BLACK));
        set(0,6, Cell.Piece(Type.KNIGHT, Player.BLACK));
        set(0,7, Cell.Piece(Type.ROOK, Player.BLACK));
        set(1,0, Cell.Piece(Type.PAWN, Player.BLACK));
        set(1,1, Cell.Piece(Type.PAWN, Player.BLACK));
        set(1,2, Cell.Piece(Type.PAWN, Player.BLACK));
        set(1,3, Cell.Piece(Type.PAWN, Player.BLACK));
        set(1,4, Cell.Piece(Type.PAWN, Player.BLACK));
        set(1,5, Cell.Piece(Type.PAWN, Player.BLACK));
        set(1,6, Cell.Piece(Type.PAWN, Player.BLACK));
        set(1,7, Cell.Piece(Type.PAWN, Player.BLACK));

        set(6,0, Cell.Piece(Type.PAWN, Player.WHITE));
        set(6,1, Cell.Piece(Type.PAWN, Player.WHITE));
        set(6,2, Cell.Piece(Type.PAWN, Player.WHITE));
        set(6,3, Cell.Piece(Type.PAWN, Player.WHITE));
        set(6,4, Cell.Piece(Type.PAWN, Player.WHITE));
        set(6,5, Cell.Piece(Type.PAWN, Player.WHITE));
        set(6,6, Cell.Piece(Type.PAWN, Player.WHITE));
        set(6,7, Cell.Piece(Type.PAWN, Player.WHITE));
        set(7,0, Cell.Piece(Type.ROOK, Player.WHITE));
        set(7,1, Cell.Piece(Type.KNIGHT, Player.WHITE));
        set(7,2, Cell.Piece(Type.BISHOP, Player.WHITE));
        set(7,3, Cell.Piece(Type.QUEEN, Player.WHITE));
        set(7,4, Cell.Piece(Type.KING, Player.WHITE));
        set(7,5, Cell.Piece(Type.BISHOP, Player.WHITE));
        set(7,6, Cell.Piece(Type.KNIGHT, Player.WHITE));
        set(7,7, Cell.Piece(Type.ROOK, Player.WHITE));
    }
}