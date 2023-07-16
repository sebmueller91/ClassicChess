package dgs.software.classicchess.calculations.ai.params

import dgs.software.classicchess.model.Piece
import dgs.software.classicchess.model.Type

class SearchParametersHard : SearchParameters() {
    override val searchDepth: Int
        get() = 4

    override fun getSquareTable(piece: Piece, isEndgame: Boolean): Array<IntArray> {
        return if (piece.type == Type.KING && isEndgame) {
            kingEndgameSquareTable
        } else {
            requireNotNull(squareTables[piece.type]) {
                "No square table found for piece type ${piece.type}"
            }
        }
    }

    override val squareTables = mapOf<Type, Array<IntArray>>(
        Type.PAWN to arrayOf(
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(50, 50, 50, 50, 50, 50, 50, 50),
            intArrayOf(10, 10, 20, 30, 30, 20, 10, 10),
            intArrayOf(5, 5, 10, 25, 25, 10, 5, 5),
            intArrayOf(0, 0, 0, 20, 20, 0, 0, 0),
            intArrayOf(5, -5, -10, 0, 0, -10, -5, 5),
            intArrayOf(5, 10, 10, -20, -20, 10, 10, 5),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0)
        ),
        Type.KNIGHT to arrayOf(
            intArrayOf(-50,-40,-30,-30,-30,-30,-40,-50),
            intArrayOf(-40,-20,  0,  0,  0,  0,-20,-40),
            intArrayOf(-30,  0, 10, 15, 15, 10,  0,-30),
            intArrayOf(-30,  5, 15, 20, 20, 15,  5,-30),
            intArrayOf(-30,  0, 15, 20, 20, 15,  0,-30),
            intArrayOf(-30,  5, 10, 15, 15, 10,  5,-30),
            intArrayOf(-40,-20,  0,  5,  5,  0,-20,-40),
            intArrayOf(-50,-40,-30,-30,-30,-30,-40,-50)
        ),
        Type.BISHOP to arrayOf(
            intArrayOf(-20,-10,-10,-10,-10,-10,-10,-20),
            intArrayOf(-10,  0,  0,  0,  0,  0,  0,-10),
            intArrayOf(-10,  0,  5, 10, 10,  5,  0,-10),
            intArrayOf(-10,  5,  5, 10, 10,  5,  5,-10),
            intArrayOf(-10,  0, 10, 10, 10, 10,  0,-10),
            intArrayOf(-10, 10, 10, 10, 10, 10, 10,-10),
            intArrayOf(-10,  5,  0,  0,  0,  0,  5,-10),
            intArrayOf(-20,-10,-10,-10,-10,-10,-10,-20)
        ),
        Type.ROOK to arrayOf(
            intArrayOf(0,  0,  0,  0,  0,  0,  0,  0),
            intArrayOf(5, 10, 10, 10, 10, 10, 10,  5),
            intArrayOf(-5,  0,  0,  0,  0,  0,  0, -5),
            intArrayOf(-5,  0,  0,  0,  0,  0,  0, -5),
            intArrayOf(-5,  0,  0,  0,  0,  0,  0, -5),
            intArrayOf(-5,  0,  0,  0,  0,  0,  0, -5),
            intArrayOf(-5,  0,  0,  0,  0,  0,  0, -5),
            intArrayOf(0,  0,  0,  5,  5,  0,  0,  0)
        ),
        Type.QUEEN to arrayOf(
            intArrayOf(-20,-10,-10, -5, -5,-10,-10,-20),
            intArrayOf(-10,  0,  0,  0,  0,  0,  0,-10),
            intArrayOf(-10,  0,  5,  5,  5,  5,  0,-10),
            intArrayOf(-5,  0,  5,  5,  5,  5,  0, -5),
            intArrayOf(0,  0,  5,  5,  5,  5,  0, -5),
            intArrayOf(-10,  5,  5,  5,  5,  5,  0,-10),
            intArrayOf(-10,  0,  5,  0,  0,  0,  0,-10),
            intArrayOf(-20,-10,-10, -5, -5,-10,-10,-20)
        ),
        Type.KING to arrayOf(
            intArrayOf(-30,-40,-40,-50,-50,-40,-40,-30),
            intArrayOf(-30,-40,-40,-50,-50,-40,-40,-30),
            intArrayOf(-30,-40,-40,-50,-50,-40,-40,-30),
            intArrayOf(-30,-40,-40,-50,-50,-40,-40,-30),
            intArrayOf(-20,-30,-30,-40,-40,-30,-30,-20),
            intArrayOf(-10,-20,-20,-20,-20,-20,-20,-10),
            intArrayOf(20, 20,  0,  0,  0,  0, 20, 20),
            intArrayOf(20, 30, 10,  0,  0, 10, 30, 20)
        )
    )

    private val kingEndgameSquareTable = arrayOf(
        intArrayOf(-50,-40,-30,-20,-20,-30,-40,-50),
        intArrayOf(-30,-20,-10,  0,  0,-10,-20,-30),
        intArrayOf(-30,-10, 20, 30, 30, 20,-10,-30),
        intArrayOf(-30,-10, 30, 40, 40, 30,-10,-30),
        intArrayOf(-30,-10, 30, 40, 40, 30,-10,-30),
        intArrayOf(-30,-10, 20, 30, 30, 20,-10,-30),
        intArrayOf(-30,-30,  0,  0,  0,  0,-30,-30),
        intArrayOf(-50,-30,-30,-30,-30,-30,-30,-50)
    )
}