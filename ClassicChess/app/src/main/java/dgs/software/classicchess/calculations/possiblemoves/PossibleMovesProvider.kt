package dgs.software.classicchess.calculations.possiblemoves

import android.util.Log
import dgs.software.classicchess.model.*
import dgs.software.classicchess.model.moves.RevertableMove

private val TAG = "PossibleMovesProvider"

interface PossibleMovesProvider {
    fun getPossibleMoves(position: Coordinate): List<RevertableMove>
}

class DefaultPossibleMovesProvider(
    private val game: Game,
    private val basicPossibleMovesProvider: BasicMovesProvider = DefaultBasicMovesProvider(game),
    private val gameStatusProvider: GameStatusProvider = DefaultGameStatusProvider(game)
) : PossibleMovesProvider {
    override fun getPossibleMoves(position: Coordinate): List<RevertableMove> {
        if (game.get(position) is Cell.Empty) {
            Log.e(TAG, "Tried to calculate moves for an empty cell at pos $position")
            return listOf<RevertableMove>()
        }
        val piece = game.getAsPiece(position)
        val possibleMoves = when (piece.type) {
            Type.PAWN -> piece.getMovesForPawn(position)
            Type.ROOK -> piece.getMovesForRook(position)
            Type.KNIGHT -> piece.getMovesForKnight(position)
            Type.BISHOP -> piece.getMovesForBishop(position)
            Type.QUEEN -> piece.getMovesForQueen(position)
            Type.KING -> piece.getMovesForKing(position)
        }

        // TODO: Filter moves that leave king in check
        return possibleMoves
    }

    private fun Cell.Piece.getMovesForPawn(position: Coordinate): List<RevertableMove> {
        // TODO: Add complex moves
        return basicPossibleMovesProvider.getBasicMoves(position)
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
        // TODO: Add complex moves
        return basicPossibleMovesProvider.getBasicMoves(position)
    }

    private fun filterMovesThatLeaveKingInCheck(moves: List<RevertableMove>, player: Player) : List<RevertableMove> {
        val filteredMoves = mutableListOf<RevertableMove>()
        moves.forEach { move ->
            game.executeMove(move, true)
            if (!gameStatusProvider.kingIsInCheck(player)) {
                filteredMoves.add(move)
            }
            game.rollbackSimulatedMoves()
        }
        return filteredMoves
    }
}


