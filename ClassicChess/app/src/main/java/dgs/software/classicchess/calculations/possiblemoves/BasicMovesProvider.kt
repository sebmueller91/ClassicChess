package dgs.software.classicchess.calculations.possiblemoves

import android.util.Log
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.MutableGame
import dgs.software.classicchess.model.Player
import dgs.software.classicchess.model.moves.MoveAndCapturePiece
import dgs.software.classicchess.model.moves.MovePiece
import dgs.software.classicchess.model.moves.RevertableMove
import dgs.software.classicchess.utils.getPlayerOrNull

private val TAG = "BasicMovesProvider"

sealed class BasicMovesProvider() {
    abstract fun calculateBasicMoves(
        mutableGame: MutableGame,
        position: Coordinate
    ): List<RevertableMove>

    object PawnBasicMovesProvider : BasicMovesProvider() {
        override fun calculateBasicMoves(
            mutableGame: MutableGame,
            position: Coordinate
        ): List<RevertableMove> {
            val player = mutableGame.getPlayerOrNull(position, TAG) ?: return listOf()
            val moveDirection = if (player == Player.BLACK) 1 else -1
            var possibleMoves = mutableListOf<RevertableMove>()

            // Move 1 forward
            var destination = position.copy(position.row + moveDirection)
            possibleMoves.addMoveIfValid(mutableGame, position, destination) {
                mutableGame.board.get(destination) == null
            }

            // Move 2 forward
            if ((player == Player.WHITE && position.row == 6 && mutableGame.board.get(
                    position.copy(
                        row = 5
                    )
                ) == null)
                || (player == Player.BLACK && position.row == 1 && mutableGame.board.get(
                    position.copy(
                        row = 2
                    )
                ) == null)
            ) {
                destination = position.copy(position.row + (moveDirection * 2))
                possibleMoves.addMoveIfValid(mutableGame, position, destination) {
                    mutableGame.board.get(position.copy(position.row + moveDirection)) == null &&
                            mutableGame.board.get(destination) == null
                }
            }

            // Hit diagonal left and right
            destination = position.copy(position.row + moveDirection, position.column + 1)
            possibleMoves.addMoveIfValid(mutableGame, position, destination) {
                val piece = mutableGame.board.get(destination)
                !(piece == null) && player == piece.player.opponent()
            }
            destination = position.copy(position.row + moveDirection, position.column - 1)
            possibleMoves.addMoveIfValid(mutableGame, position, destination) {
                val piece = mutableGame.board.get(destination)
                !(piece == null) && player == piece.player.opponent()
            }

            return possibleMoves
        }
    }

    object RookBasicMovesProvider : BasicMovesProvider() {
        override fun calculateBasicMoves(
            mutableGame: MutableGame,
            position: Coordinate
        ): List<RevertableMove> {
            var possibleMoves = mutableListOf<RevertableMove>()
            possibleMoves.addCellsOnStraightLines(mutableGame, position)
            return possibleMoves
        }
    }

    object KnightBasicMovesProvider : BasicMovesProvider() {
        override fun calculateBasicMoves(
            mutableGame: MutableGame,
            position: Coordinate
        ): List<RevertableMove> {
            var possibleMoves = mutableListOf<RevertableMove>()

            possibleMoves.addMoveIfValid(
                mutableGame,
                position,
                position.copy(position.row + 2, position.column + 1)
            )
            possibleMoves.addMoveIfValid(
                mutableGame,
                position,
                position.copy(position.row + 2, position.column - 1)
            )
            possibleMoves.addMoveIfValid(
                mutableGame,
                position,
                position.copy(position.row - 2, position.column + 1)
            )
            possibleMoves.addMoveIfValid(
                mutableGame,
                position,
                position.copy(position.row - 2, position.column - 1)
            )
            possibleMoves.addMoveIfValid(
                mutableGame,
                position,
                position.copy(position.row + 1, position.column + 2)
            )
            possibleMoves.addMoveIfValid(
                mutableGame,
                position,
                position.copy(position.row - 1, position.column + 2)
            )
            possibleMoves.addMoveIfValid(
                mutableGame,
                position,
                position.copy(position.row + 1, position.column - 2)
            )
            possibleMoves.addMoveIfValid(
                mutableGame,
                position,
                position.copy(position.row - 1, position.column - 2)
            )

            return possibleMoves
        }
    }

    object BishopBasicMovesProvider : BasicMovesProvider() {
        override fun calculateBasicMoves(
            mutableGame: MutableGame,
            position: Coordinate
        ): List<RevertableMove> {
            var possibleMoves = mutableListOf<RevertableMove>()
            possibleMoves.addCellsOnDiagonalLines(mutableGame, position)
            return possibleMoves
        }
    }

    object QueenBasicMovesProvider: BasicMovesProvider() {
        override fun calculateBasicMoves(
            mutableGame: MutableGame,
            position: Coordinate
        ): List<RevertableMove> {
            var possibleMoves = mutableListOf<RevertableMove>()
            possibleMoves.addCellsOnStraightLines(mutableGame, position)
            possibleMoves.addCellsOnDiagonalLines(mutableGame, position)
            possibleMoves.addNeighborCells(mutableGame, position)
            return possibleMoves
        }
    }

    object KingBasicMovesProvider: BasicMovesProvider() {
        override fun calculateBasicMoves(
            mutableGame: MutableGame,
            position: Coordinate
        ): List<RevertableMove> {
            var possibleMoves = mutableListOf<RevertableMove>()
            possibleMoves.addNeighborCells(mutableGame, position)
            return possibleMoves
        }
    }

    protected fun MutableList<RevertableMove>.addNeighborCells(mutableGame: MutableGame, fromPos: Coordinate) {
        val r = fromPos.row
        val c = fromPos.column
        addMoveIfValid(mutableGame, fromPos, fromPos.copy(row = r + 1, column = c - 1))
        addMoveIfValid(mutableGame, fromPos, fromPos.copy(row = r + 1, column = c))
        addMoveIfValid(mutableGame, fromPos, fromPos.copy(row = r + 1, column = c + 1))
        addMoveIfValid(mutableGame, fromPos, fromPos.copy(row = r, column = c - 1))
        addMoveIfValid(mutableGame, fromPos, fromPos.copy(row = r, column = c + 1))
        addMoveIfValid(mutableGame, fromPos, fromPos.copy(row = r - 1, column = c - 1))
        addMoveIfValid(mutableGame, fromPos, fromPos.copy(row = r - 1, column = c))
        addMoveIfValid(mutableGame, fromPos, fromPos.copy(row = r - 1, column = c + 1))
    }

    protected fun MutableList<RevertableMove>.addCellsOnStraightLines(mutableGame: MutableGame, fromPos: Coordinate) {
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
                if (!addMoveAndCheckIfToContinue(mutableGame, fromPos, toPos)) {
                    break
                }
            }
        }
    }

    protected fun MutableList<RevertableMove>.addCellsOnDiagonalLines(mutableGame: MutableGame, fromPos: Coordinate) {
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
                if (!addMoveAndCheckIfToContinue(mutableGame, fromPos, toPos)) {
                    break
                }
            }
        }
    }

    protected fun MutableList<RevertableMove>.addMoveAndCheckIfToContinue(
        mutableGame: MutableGame,
        fromPos: Coordinate,
        toPos: Coordinate
    ): Boolean {
        if (!toPos.isValid()) {
            return false
        }
        val fromPosPiece = mutableGame.board.get(fromPos)
        if (fromPosPiece == null) {
            Log.e(TAG, "Tried to move an invalid cell $fromPos")
            return false
        }

        val toPosPiece =
            mutableGame.board.get(toPos) ?: return addMoveIfValid(mutableGame, fromPos, toPos)

        if (fromPosPiece.player != toPosPiece.player) {
            addMoveIfValid(mutableGame, fromPos, toPos)
        }

        return false
    }

    protected fun MutableList<RevertableMove>.addMoveIfValid(
        mutableGame: MutableGame,
        fromPos: Coordinate,
        toPos: Coordinate,
        conditionToAdd: (Coordinate) -> Boolean = { true }
    ): Boolean {
        if (!fromPos.isValid()) {
            Log.e(TAG, "Tried to move an invalid cell $fromPos")
            return false
        }
        val fromPosPiece = mutableGame.board.get(fromPos)
        if (fromPosPiece == null) {
            Log.e(TAG, "Tried to move empty cell $fromPos")
            return false
        }
        if (!toPos.isValid()
            || !conditionToAdd(toPos)
        ) {
            return false
        }

        val toPosPiece = mutableGame.board.get(toPos)
        val move = if (toPosPiece == null) { // Non-capture move
            MovePiece(fromPos, toPos)
        } else { // Capture move
            if (fromPosPiece.player != toPosPiece.player) {
                MoveAndCapturePiece(fromPos, toPos)
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