package dgs.software.classicchess.calculations.possiblemoves

import android.util.Log
import dgs.software.classicchess.model.Cell
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.Game
import dgs.software.classicchess.model.Type
import dgs.software.classicchess.model.moves.RevertableMove

private val TAG = "PossibleMovesProvider"

class PossibleMovesProvider(
    val game: Game
) {
    private val basicPossibleMovesProvider = BasicMovesProvider(game)

    fun getMoves(position: Coordinate): List<RevertableMove> {
        if (game.get(position) is Cell.Empty) {
            Log.e(TAG, "Tried to calculate moves for an empty cell at pos $position")
            return listOf<RevertableMove>()
        }
        val piece = game.getAsPiece(position)
        return when (piece.type) {
            Type.PAWN -> piece.getMovesForPawn(position)
            Type.ROOK -> piece.getMovesForRook(position)
            Type.KNIGHT -> piece.getMovesForKnight(position)
            Type.BISHOP -> piece.getMovesForBishop(position)
            Type.QUEEN -> piece.getMovesForQueen(position)
            Type.KING -> piece.getMovesForKing(position)
        }
    }

    private fun Cell.Piece.getMovesForPawn(position: Coordinate): List<RevertableMove> {
        throw Exception("Not Implemented")
    }

    private fun Cell.Piece.getMovesForRook(position: Coordinate): List<RevertableMove> {
        return basicPossibleMovesProvider.getBasicMoves(position)
    }

    private fun Cell.Piece.getMovesForKnight(position: Coordinate): List<RevertableMove> {
        return basicPossibleMovesProvider.getBasicMoves(position)
    }

    private fun Cell.Piece.getMovesForBishop(position: Coordinate): List<RevertableMove> {
        return basicPossibleMovesProvider.getBasicMoves(position)
    }

    private fun Cell.Piece.getMovesForQueen(position: Coordinate): List<RevertableMove> {
        return basicPossibleMovesProvider.getBasicMoves(position)
    }

    private fun Cell.Piece.getMovesForKing(position: Coordinate): List<RevertableMove> {
        throw Exception("Not Implemented")
    }
}


