package dgs.software.classicchess.calculations.possiblemoves

import android.util.Log
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.Game
import dgs.software.classicchess.model.Player
import dgs.software.classicchess.model.Type
import dgs.software.classicchess.model.moves.MoveAndCapturePiece
import dgs.software.classicchess.model.moves.MovePiece
import dgs.software.classicchess.model.moves.RevertableMove
import dgs.software.classicchess.utils.getPlayerOrNull

private val TAG = "BasicMovesProvider"

interface BasicMovesProvider {
    fun getBasicMoves(position: Coordinate): List<RevertableMove>
}

class DefaultBasicMovesProvider(
    private val game: Game
) : BasicMovesProvider {
    override fun getBasicMoves(position: Coordinate): List<RevertableMove> {
        val piece = game.board.get(position)
        if (piece == null) {
            Log.e(TAG, "Tried to calculate basic moves for an empty cell at pos $position")
            return listOf()
        }
        return when (piece.type) {
            Type.PAWN -> getBasicMovesForPawn(position)
            Type.ROOK -> getBasicMovesForRook(position)
            Type.KNIGHT -> getBasicMovesForKnight(position)
            Type.BISHOP -> getBasicMovesForBishop(position)
            Type.QUEEN -> getBasicMovesForQueen(position)
            Type.KING -> getBasicMovesForKing(position)
        }
    }

    private fun getBasicMovesForPawn(position: Coordinate): List<RevertableMove> {
        val player = game.getPlayerOrNull(position, TAG) ?: return listOf()
        val moveDirection = if (player == Player.BLACK) 1 else -1
        var possibleMoves = mutableListOf<RevertableMove>()

        // Move 1 forward
        var destination = position.copy(position.row + moveDirection)
        possibleMoves.addMoveIfValid(position, destination) { coord ->
            game.board.get(destination) == null
        }

        // Move 2 forward
        if ((player == Player.WHITE && position.row == 6 && game.board.get(position.copy(row = 5)) == null)
            || (player == Player.BLACK && position.row == 1 && game.board.get(position.copy(row = 2)) == null)
        ) {
            destination = position.copy(position.row + (moveDirection * 2))
            possibleMoves.addMoveIfValid(position, destination) { coord ->
                game.board.get(position.copy(position.row + moveDirection)) == null &&
                        game.board.get(destination) == null
            }
        }

        // Hit diagonal left and right
        destination = position.copy(position.row + moveDirection, position.column + 1)
        possibleMoves.addMoveIfValid(position, destination) { coord ->
            val piece = game.board.get(destination)
            !(piece == null) && player == piece.player.opponent()
        }
        destination = position.copy(position.row + moveDirection, position.column - 1)
        possibleMoves.addMoveIfValid(position, destination) { coord ->
            val piece = game.board.get(destination)
            !(piece == null) && player == piece.player.opponent()
        }

        return possibleMoves
    }

    private fun getBasicMovesForRook(position: Coordinate): List<RevertableMove> {
        var possibleMoves = mutableListOf<RevertableMove>()
        possibleMoves.addCellsOnStraightLines(position)
        return possibleMoves
    }

    private fun getBasicMovesForKnight(position: Coordinate): List<RevertableMove> {
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

    private fun getBasicMovesForBishop(position: Coordinate): List<RevertableMove> {
        var possibleMoves = mutableListOf<RevertableMove>()
        possibleMoves.addCellsOnDiagonalLines(position)
        return possibleMoves
    }

    private fun getBasicMovesForQueen(position: Coordinate): List<RevertableMove> {
        var possibleMoves = mutableListOf<RevertableMove>()
        possibleMoves.addCellsOnStraightLines(position)
        possibleMoves.addCellsOnDiagonalLines(position)
        possibleMoves.addNeighborCells(position)
        return possibleMoves
    }

    private fun getBasicMovesForKing(position: Coordinate): List<RevertableMove> {
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
        val fromPosPiece = game.board.get(fromPos)
        if (fromPosPiece == null) {
            Log.e(TAG, "Tried to move an invalid cell $fromPos")
            return false
        }

        val toPosPiece = game.board.get(toPos) ?: return addMoveIfValid(fromPos, toPos)

        if (fromPosPiece.player != toPosPiece.player) {
            addMoveIfValid(fromPos, toPos)
        }

        return false
    }

    private fun MutableList<RevertableMove>.addMoveIfValid(
        fromPos: Coordinate,
        toPos: Coordinate,
        conditionToAdd: (Coordinate) -> Boolean = { true }
    ): Boolean {
        if (!fromPos.isValid()) {
            Log.e(TAG, "Tried to move an invalid cell $fromPos")
            return false
        }
        val fromPosPiece = game.board.get(fromPos)
        if (fromPosPiece == null) {
            Log.e(TAG, "Tried to move empty cell $fromPos")
            return false
        }
        if (!toPos.isValid()
            || !conditionToAdd(toPos)
        ) {
            return false
        }

        val toPosPiece = game.board.get(toPos)
        val move = if (toPosPiece == null) { // Non-capture move
            MovePiece(fromPos, toPos, { game })
        } else { // Capture move
            if (fromPosPiece.player != toPosPiece.player) {
                MoveAndCapturePiece(fromPos, toPos, { game })
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