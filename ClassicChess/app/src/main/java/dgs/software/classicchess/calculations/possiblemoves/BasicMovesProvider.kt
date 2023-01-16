package dgs.software.classicchess.calculations.possiblemoves

import android.util.Log
import dgs.software.classicchess.model.*
import dgs.software.classicchess.model.Cell.Piece
import dgs.software.classicchess.model.moves.MoveAndCapturePiece
import dgs.software.classicchess.model.moves.MovePiece
import dgs.software.classicchess.model.moves.RevertableMove

private val TAG = "BasicMovesProvider"

interface BasicMovesProvider {
    fun getBasicMoves(position: Coordinate): List<RevertableMove>
}

class DefaultBasicMovesProvider(
    private val game: Game
) : BasicMovesProvider {
    override fun getBasicMoves(position: Coordinate): List<RevertableMove> {
        if (game.get(position) is Cell.Empty) {
            Log.e(TAG, "Tried to calculate basic moves for an empty cell at pos $position")
            return listOf<RevertableMove>()
        }
        val piece = game.getAsPiece(position)
        return when (piece.type) {
            Type.PAWN -> piece.getBasicMovesForPawn(position)
            Type.ROOK -> piece.getBasicMovesForRook(position)
            Type.KNIGHT -> piece.getBasicMovesForKnight(position)
            Type.BISHOP -> piece.getBasicMovesForBishop(position)
            Type.QUEEN -> piece.getBasicMovesForQueen(position)
            Type.KING -> piece.getBasicMovesForKing(position)
        }
    }

    private fun Piece.getBasicMovesForPawn(position: Coordinate): List<RevertableMove> {
        val player = game.getAsPiece(coordinate).player
        val moveDirection = if (player == Player.BLACK) 1 else -1
        val isInStartingPos = when (player) {
            Player.BLACK -> position.row == 1
            Player.WHITE -> position.row == 6
        }
        var possibleMoves = mutableListOf<RevertableMove>()

        // Move 1 forward
        var destination = position.copy(position.row + moveDirection)
        possibleMoves.addMoveIfValid(position, destination) { coord ->
            game.get(destination) is Cell.Empty
        }

        // Move 2 forward
        if ((player == Player.WHITE && position.row == 6 && game.get(position.copy(row = 5)) is Cell.Empty)
            || (player == Player.BLACK && position.row == 1 && game.get(position.copy(row = 2)) is Cell.Empty)
        ) {
            destination = position.copy(position.row + (moveDirection * 2))
            possibleMoves.addMoveIfValid(position, destination) { coord ->
                game.get(position.copy(position.row + moveDirection)) is Cell.Empty &&
                        game.get(destination) is Cell.Empty
            }
        }

        // Hit diagonal left and right
        destination = position.copy(position.row + moveDirection, position.column + 1)
        possibleMoves.addMoveIfValid(position, destination) { coord ->
            !(game.get(destination) is Cell.Empty)
                    && player == game.getAsPiece(destination).player.opponent()
        }
        destination = position.copy(position.row + moveDirection, position.column - 1)
        possibleMoves.addMoveIfValid(position, destination) { coord ->
            !(game.get(destination) is Cell.Empty)
                    && player == game.getAsPiece(destination).player.opponent()
        }

        // TOOD: Add en-passant

        return possibleMoves
    }

    private fun Piece.getBasicMovesForRook(position: Coordinate): List<RevertableMove> {
        var possibleMoves = mutableListOf<RevertableMove>()
        possibleMoves.addCellsOnStraightLines(position)
        return possibleMoves
    }

    private fun Piece.getBasicMovesForKnight(position: Coordinate): List<RevertableMove> {
        var possibleMoves = mutableListOf<RevertableMove>()

        possibleMoves.addMoveIfValid(position, position.copy(position.row + 2, position.column + 1))
        possibleMoves.addMoveIfValid(position, position.copy(position.row + 2, position.column - 1))
        possibleMoves.addMoveIfValid(position, position.copy(position.row - 2, position.column + 1))
        possibleMoves.addMoveIfValid(position, position.copy(position.row - 2, position.column - 1))
        possibleMoves.addMoveIfValid(position, position.copy(position.row + 1, position.column + 2))
        possibleMoves.addMoveIfValid(position, position.copy(position.row - 1, position.column + 2))
        possibleMoves.addMoveIfValid(position, position.copy(position.row + 1, position.column - 2))
        possibleMoves.addMoveIfValid(position, position.copy(position.row - 1, position.column - 2))

        return possibleMoves
    }

    private fun Piece.getBasicMovesForBishop(position: Coordinate): List<RevertableMove> {
        var possibleMoves = mutableListOf<RevertableMove>()
        possibleMoves.addCellsOnDiagonalLines(position)
        return possibleMoves
    }

    private fun Piece.getBasicMovesForQueen(position: Coordinate): List<RevertableMove> {
        var possibleMoves = mutableListOf<RevertableMove>()
        possibleMoves.addCellsOnStraightLines(position)
        possibleMoves.addCellsOnDiagonalLines(position)
        possibleMoves.addNeighborCells(position)
        return possibleMoves
    }

    private fun Piece.getBasicMovesForKing(position: Coordinate): List<RevertableMove> {
        var possibleMoves = mutableListOf<RevertableMove>()
        possibleMoves.addNeighborCells(position)
        return possibleMoves
    }

    private fun MutableList<RevertableMove>.addNeighborCells(fromPos: Coordinate) {
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
        val directions =
            listOf(
                Coordinate(1, 0),
                Coordinate(-1, 0),
                Coordinate(0, 1),
                Coordinate(0, -1)
            )

        directions.forEach { direction ->
            var toPos = fromPos
            for (i in 0..6) {
                toPos += direction
                if (!addMoveAndCheckIfToContinue(fromPos, toPos)) {
                    break
                }
            }
        }
    }

    private fun MutableList<RevertableMove>.addCellsOnDiagonalLines(fromPos: Coordinate) {
        val directions =
            listOf(
                Coordinate(1, 1),
                Coordinate(-1, 1),
                Coordinate(1, -1),
                Coordinate(-1, -1)
            )

        directions.forEach { direction ->
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
        if (!toPos.isValid()) {
            return false
        }
        if (game.get(fromPos) is Cell.Empty) {
            Log.e(TAG, "Tried to move an invalid cell $fromPos")
            return false
        }

        if (game.get(toPos) is Cell.Empty){
            return addMoveIfValid(fromPos, toPos)
        }

        if (game.getAsPiece(fromPos).player != game.getAsPiece(toPos).player) {
            addMoveIfValid(fromPos, toPos)
        }

        return false
    }

    private fun MutableList<RevertableMove>.addMoveIfValid(
        fromPos: Coordinate,
        toPos: Coordinate,
        conditionToAdd: (Coordinate) -> Boolean = { true }
    ): Boolean {
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
            MovePiece(fromPos, toPos, { game })
        } else { // Capture move
            if (game.getAsPiece(fromPos).player != game.getAsPiece(toPos).player) {
                MoveAndCapturePiece(fromPos, toPos, {game})
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