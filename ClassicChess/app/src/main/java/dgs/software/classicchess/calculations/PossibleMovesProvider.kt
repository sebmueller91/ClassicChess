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
    fun getUnfilteredMoves(position: Coordinate): List<RevertableMove> {
        if (game.get(position) is Cell.Empty) {
            Log.e(TAG, "Tried to calculate moves for an empty cell at pos $position")
            return listOf<RevertableMove>()
        }
        val piece = game.getAsPiece(position)
        return when (piece.type) {
            Type.PAWN -> piece.getUnfilteredMovesForPawn(position)
            Type.ROOK -> piece.getUnfilteredMovesForRook(position)
            Type.KNIGHT -> piece.getUnfilteredMovesForKnight(position)
            Type.BISHOP -> piece.getUnfilteredMovesForBishop(position)
            Type.KING -> piece.getUnfilteredMovesForKing(position)
            Type.QUEEN -> piece.getUnfilteredMovesForQueen(position)
        }
    }

    fun Piece.getUnfilteredMovesForPawn(position: Coordinate): List<RevertableMove> {
        throw java.lang.Exception("Not implemented")
    }

    private fun Piece.getUnfilteredMovesForQueen(position: Coordinate): List<RevertableMove> {
        var possibleMoves = mutableListOf<RevertableMove>()
        possibleMoves.addCellsOnStraightLines(position)
        possibleMoves.addCellsOnDiagonalLines(position)
        possibleMoves.addNeighborCells(position)
        return possibleMoves
    }

    private fun Piece.getUnfilteredMovesForKing(position: Coordinate): List<RevertableMove> {
        throw java.lang.Exception("Not implemented")
    }

    private fun Piece.getUnfilteredMovesForBishop(position: Coordinate): List<RevertableMove> {
        var possibleMoves = mutableListOf<RevertableMove>()
        possibleMoves.addCellsOnDiagonalLines(position)
        return possibleMoves
    }

    private fun Piece.getUnfilteredMovesForKnight(position: Coordinate): List<RevertableMove> {
        var possibleMoves = mutableListOf<RevertableMove>()

        possibleMoves.addMoveIfValid(position, position.copy(position.row+2,position.column+1))
        possibleMoves.addMoveIfValid(position, position.copy(position.row+2,position.column-1))
        possibleMoves.addMoveIfValid(position, position.copy(position.row-2,position.column+1))
        possibleMoves.addMoveIfValid(position, position.copy(position.row-2,position.column-1))
        possibleMoves.addMoveIfValid(position, position.copy(position.row+1,position.column+2))
        possibleMoves.addMoveIfValid(position, position.copy(position.row-1,position.column+2))
        possibleMoves.addMoveIfValid(position, position.copy(position.row+1,position.column-2))
        possibleMoves.addMoveIfValid(position, position.copy(position.row-1,position.column-2))

        return possibleMoves
    }

    private fun Piece.getUnfilteredMovesForRook(position: Coordinate): List<RevertableMove> {
        var possibleMoves = mutableListOf<RevertableMove>()
        possibleMoves.addCellsOnStraightLines(position)
        return possibleMoves
    }

    private fun MutableList<RevertableMove>.addNeighborCells(fromPos: Coordinate) {
        if (game.get(fromPos) is Cell.Empty) {
            Log.e(TAG, "Tried to add neighbor cells for empty cell $fromPos")
            return
        }
        val r = fromPos.row
        val c = fromPos.column
        addMoveIfValid(fromPos, fromPos.copy(row = r + 1, column = c - 1))
        addMoveIfValid(fromPos, fromPos.copy(row = r + 1, column = c))
        addMoveIfValid(fromPos, fromPos.copy(row = r + 1, column = c + 1))
        addMoveIfValid(fromPos, fromPos.copy(row = r, column = c - 1))
        addMoveIfValid(fromPos, fromPos.copy(row = r, column = c + 1))
        addMoveIfValid(fromPos, fromPos.copy(row = r - 1, column = c - 1))
        addMoveIfValid(fromPos, fromPos.copy(row = r - 1, column = c))
        addMoveIfValid(fromPos, fromPos.copy(row = r - 1, column = c + 1))
    }

    private fun MutableList<RevertableMove>.addCellsOnStraightLines(fromPos: Coordinate) {
        if (game.get(fromPos) is Cell.Empty) {
            Log.e(TAG, "Tried to add straight lines for empty cell $fromPos")
            return
        }

        for (r in fromPos.row..7) {
            if (!addMoveAndCheckIfToContinue(fromPos, fromPos.copy(row = r))) {
                break
            }
        }
        for (r in fromPos.row downTo 0) {
            if (!addMoveAndCheckIfToContinue(fromPos, fromPos.copy(row = r))) {
                break
            }
        }
        for (c in fromPos.column..7) {
            if (!addMoveAndCheckIfToContinue(fromPos, fromPos.copy(column = c))) {
                break
            }
        }
        for (c in fromPos.column downTo 0) {
            if (!addMoveAndCheckIfToContinue(fromPos, fromPos.copy(column = c))) {
                break
            }
        }
    }

    private fun MutableList<RevertableMove>.addCellsOnDiagonalLines(fromPos: Coordinate) {
        if (game.get(fromPos) is Cell.Empty) {
            Log.e(TAG, "Tried to add diagonal lines for empty cell $fromPos")
            return
        }

        val directions =
            listOf(
                Coordinate(1, 1),
                Coordinate(-1, 1),
                Coordinate(1, -1),
                Coordinate(-1, -1))

        directions.forEach{ direction ->
            var toPos = fromPos
            for (i in 0..6) {
                toPos += direction
                if (!addMoveAndCheckIfToContinue(fromPos, toPos)) {
                    break
                }
            }
        }
    }

    private fun MutableList<RevertableMove>.addMoveAndCheckIfToContinue(
        fromPos: Coordinate,
        toPos: Coordinate
    ): Boolean {
        if (!(game.get(toPos) is Cell.Empty)
            || game.getAsPiece(fromPos).player == game.getAsPiece(toPos).player
        ) {
            return false
        }
        if (!addMoveIfValid(fromPos, toPos)) {
            return false
        }
        return game.get(toPos) is Cell.Empty
    }

    private fun MutableList<RevertableMove>.addMoveIfValid(
        fromPos: Coordinate,
        toPos: Coordinate,
        conditionToAdd: (Coordinate) -> Boolean = { true }
    ) : Boolean {
        val moves = mutableListOf<RevertableMove>()
        if (!fromPos.isValid()) {
            Log.e(TAG, "Tried to move an invalid cell $fromPos")
            return false
        }
        if (game.get(fromPos) is Cell.Empty) {
            Log.e(TAG, "Tried to move empty cell $fromPos")
            return false
        }
        if (!toPos.isValid()
            || !conditionToAdd(toPos)
        ) {
            return false
        }

        val move = if (game.get(toPos) is Cell.Empty) { // Non-capture move
            MovePiece(fromPos, toPos, game)
        } else { // Capture move
            if (game.getAsPiece(fromPos).player != game.getAsPiece(toPos).player) {
                MoveAndCapturePiece(fromPos, toPos, game)
            } else {
                return false // fromPos and toPos are both the same player
            }
        }

        if (!contains(move)) {
            add(move)
        }
        return true
    }
}