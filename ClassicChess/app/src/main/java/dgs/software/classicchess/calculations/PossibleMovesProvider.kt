package dgs.software.classicchess.calculations

import android.util.Log
import dgs.software.classicchess.model.Cell
import dgs.software.classicchess.model.Cell.Piece
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.Game
import dgs.software.classicchess.model.Type
import dgs.software.classicchess.model.moves.MoveAndCapturePiece
import dgs.software.classicchess.model.moves.MovePiece
import dgs.software.classicchess.model.moves.RevertableMove

private val TAG = "PossibleMovesProvider"

class PossibleMovesProvider(
    val game: Game
) {
    fun getUnfilteredMoves(piece: Piece): List<Coordinate> {
        return when (piece.type) {
            Type.PAWN -> piece.getUnfilteredMovesForPawn()
            Type.ROOK -> piece.getUnfilteredMovesForRook()
            Type.KNIGHT -> piece.getUnfilteredMovesForKnight()
            Type.BISHOP -> piece.getUnfilteredMovesForBishop()
            Type.KING -> piece.getUnfilteredMovesForKing()
            Type.QUEEN -> piece.getUnfilteredMovesForQueen()
        }
    }

    fun Piece.getUnfilteredMovesForPawn(): List<Coordinate> {
        throw java.lang.Exception("Not implemented")
    }

    private fun Piece.getUnfilteredMovesForQueen(): List<Coordinate> {
        throw java.lang.Exception("Not implemented")
    }

    private fun Piece.getUnfilteredMovesForKing(): List<Coordinate> {
        throw java.lang.Exception("Not implemented")
    }

    private fun Piece.getUnfilteredMovesForBishop(): List<Coordinate> {
        throw java.lang.Exception("Not implemented")
    }

    private fun Piece.getUnfilteredMovesForKnight(): List<Coordinate> {
        throw java.lang.Exception("Not implemented")
    }

    private fun Piece.getUnfilteredMovesForRook(): List<Coordinate> {
        throw java.lang.Exception("Not implemented")
    }

    private fun MutableList<RevertableMove>.addMoveIfValid(
        fromPos: Coordinate,
        toPos: Coordinate,
        conditionToAdd: (Coordinate) -> Boolean = { true }
    ) {
        val moves = mutableListOf<RevertableMove>()
        if (!fromPos.isValid()) {
            Log.e(TAG, "Tried to move an invalid cell $fromPos")
            return
        }
        if (game.get(fromPos) is Cell.Empty) {
            Log.e(TAG, "Tried to move empty cell $fromPos")
            return
        }
        if (!toPos.isValid()
            || !conditionToAdd(toPos)
        ) {
            return
        }

        val move = if (game.get(toPos) is Cell.Empty) { // Non-capture move
            MovePiece(fromPos, toPos, game)
        } else { // Capture move
            if (game.getAsPiece(fromPos).player != game.getAsPiece(toPos).player) {
                MoveAndCapturePiece(fromPos, toPos, game) 
            } else {
                return // fromPos and toPos are both the same player
            }
        }

        if (!contains(move)) {
            add(move)
        }
    }
}