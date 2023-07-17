package dgs.software.classicchess.model

import dgs.software.classicchess.calculations.possiblemoves.BasicMovesProvider
import dgs.software.classicchess.calculations.possiblemoves.PossibleMovesProvider

data class Piece(
    val type: Type,
    val player: Player,
    var isMoved: Boolean = false,
    val basicMoves: BasicMovesProvider = when(type) {
        Type.PAWN -> BasicMovesProvider.PawnBasicMovesProvider()
        Type.BISHOP -> BasicMovesProvider.BishopBasicMovesProvider()
        Type.KNIGHT -> BasicMovesProvider.KnightBasicMovesProvider()
        Type.ROOK -> BasicMovesProvider.RookBasicMovesProvider()
        Type.QUEEN -> BasicMovesProvider.QueenBasicMovesProvider()
        Type.KING -> BasicMovesProvider.KingBasicMovesProvider()
    },
    val moves: PossibleMovesProvider = when(type) {
        Type.PAWN -> PossibleMovesProvider.PawnMovesProvider()
        Type.BISHOP -> PossibleMovesProvider.BishopMovesProvider()
        Type.KNIGHT -> PossibleMovesProvider.KnightMovesProvider()
        Type.ROOK -> PossibleMovesProvider.RookMovesProvider()
        Type.QUEEN -> PossibleMovesProvider.QueenMovesProvider()
        Type.KING -> PossibleMovesProvider.KingMovesProvider()
    }
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Piece

        if (type != other.type) return false
        if (player != other.player) return false
        if (isMoved != other.isMoved) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + player.hashCode()
        result = 31 * result + isMoved.hashCode()
        return result
    }
}

