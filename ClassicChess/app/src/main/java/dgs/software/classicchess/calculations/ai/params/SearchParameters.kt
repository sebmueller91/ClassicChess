package dgs.software.classicchess.calculations.ai.params

import dgs.software.classicchess.model.Piece
import dgs.software.classicchess.model.Type

abstract class SearchParameters {
    abstract val searchDepth: Int

    protected val pieceWeights: Map<Type, Int> = mapOf(
        Type.PAWN to 100,
        Type.KNIGHT to 320,
        Type.BISHOP to 330,
        Type.ROOK to 500,
        Type.QUEEN to 900,
        Type.KING to 20000
    )

    protected abstract val squareTables: Map<Type, Array<IntArray>>

    fun getWeight(piece: Piece): Int {
        return requireNotNull(pieceWeights[piece.type]) {
            "No weight found for piece type ${piece.type}"
        }
    }

    abstract fun getSquareTable(piece: Piece, isEndgame: Boolean): Array<IntArray>
}

