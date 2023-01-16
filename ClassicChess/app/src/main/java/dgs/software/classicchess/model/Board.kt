package dgs.software.classicchess.model

data class Board(
    private val grid: MutableList<MutableList<Cell>> = MutableList(8) { MutableList(8) { Cell.Empty(Coordinate(-1, -1)) } }
) {
    init {
        initializeWithDefaultSetup()
    }

    fun get(coordinate: Coordinate): Cell {
        return get(coordinate.row, coordinate.column)
    }

    fun get(row: Int, col: Int): Cell {
        return grid[row][col]
    }

    fun set(coordinate: Coordinate, cell: Cell) {
        grid.get(coordinate.row).set(coordinate.column, cell)
        cell.coordinate = coordinate
    }

    fun set(row: Int, col: Int, cell: Cell) {
        grid.get(row).set(col,cell)
        cell.coordinate = Coordinate(row,col)
    }

    private fun createPiece(row: Int, col: Int, type: Type, player: Player) {
        grid[row][col] = Cell.Piece(type, player, Coordinate(row, col))
    }

    private fun initializeWithDefaultSetup() {
        for (i in 0 until 8) {
            for (j in 0 until 8) {
                set(i, j, Cell.Empty(Coordinate(i, j)))
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
}