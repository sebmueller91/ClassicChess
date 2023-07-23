package dgs.software.classicchess.calculations.ai.params

import dgs.software.classicchess.model.Piece
import dgs.software.classicchess.model.Type

class SearchParametersEasy : SearchParameters() {
    override val searchDepth: Int
        get() = 2

    private val squareTables = mapOf<Type, Array<IntArray>>(
        Type.PAWN to Array(8) { IntArray(8) { 0 } },
        Type.KNIGHT to Array(8) { IntArray(8) { 0 } },
        Type.BISHOP to Array(8) { IntArray(8) { 0 } },
        Type.ROOK to Array(8) { IntArray(8) { 0 } },
        Type.QUEEN to Array(8) { IntArray(8) { 0 } },
        Type.KING to Array(8) { IntArray(8) { 0 } }
    )

    override fun getSquareTable(piece: Piece, isEndgame: Boolean): Array<IntArray> {
        return Array(8) { IntArray(8) { 0 } }
    }
}