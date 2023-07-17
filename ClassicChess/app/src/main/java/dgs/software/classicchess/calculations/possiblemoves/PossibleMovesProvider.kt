package dgs.software.classicchess.calculations.possiblemoves

import dgs.software.classicchess.logger.Logger
import dgs.software.classicchess.logger.LoggerFactory
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.MutableGame
import dgs.software.classicchess.model.Player
import dgs.software.classicchess.model.Type
import dgs.software.classicchess.model.moves.CaptureEnPassantMove
import dgs.software.classicchess.model.moves.CastlingMove
import dgs.software.classicchess.model.moves.PromotePawnMove
import dgs.software.classicchess.model.moves.RevertableMove
import dgs.software.classicchess.utils.getPlayerOrNull

private val TAG = "PossibleMovesProvider"


sealed class PossibleMovesProvider(
    open val logger: Logger = LoggerFactory().create()
) {

    abstract fun calculatePossibleMovesOfPiece(
        mutableGame: MutableGame,
        position: Coordinate
    ): List<RevertableMove>

    companion object {
        fun calculatePossibleMovesOfPlayer(
            mutableGame: MutableGame,
            player: Player
        ): List<RevertableMove> {
            val possibleMoves = mutableListOf<RevertableMove>()
            for (i in 0 until 8) {
                for (j in 0 until 8) {
                    val pos = Coordinate(i, j)
                    val piece = mutableGame.board.get(pos)
                    if (piece?.player != player) {
                        continue
                    }
                    val possibleMovesOfPiece =
                        piece.moves.calculatePossibleMovesOfPiece(mutableGame, pos)
                    possibleMoves.addAll(possibleMovesOfPiece)
                }
            }
            return possibleMoves
        }
    }

    class PawnMovesProvider(
        override val logger: Logger = LoggerFactory().create()
    ) : PossibleMovesProvider(logger) {
        override fun calculatePossibleMovesOfPiece(
            mutableGame: MutableGame,
            position: Coordinate
        ): List<RevertableMove> {
            val piece = mutableGame.board.get(position) ?: return listOf()
            var possibleMoves =
                piece.basicMoves.calculateBasicMoves(mutableGame, position).toMutableList()
            possibleMoves.addEnPassantMoves(mutableGame, position)
            possibleMoves.addPromotePawnMoves(mutableGame, position)
            return filterMovesThatLeaveKingInCheck(mutableGame, possibleMoves, piece.player)
        }
    }

    class RookMovesProvider(
        override val logger: Logger = LoggerFactory().create()
    ) : PossibleMovesProvider(logger) {
        override fun calculatePossibleMovesOfPiece(
            mutableGame: MutableGame,
            position: Coordinate
        ): List<RevertableMove> {
            val piece = mutableGame.board.get(position)
            if (piece == null) {
                logger.e(TAG, "Piece must not be null here! Something is wrong with the code")
                return listOf()
            }
            val possibleMoves = piece.basicMoves.calculateBasicMoves(mutableGame, position)
            return filterMovesThatLeaveKingInCheck(mutableGame, possibleMoves, piece.player)
        }
    }

    class KnightMovesProvider(
        override val logger: Logger = LoggerFactory().create()
    ) : PossibleMovesProvider(logger) {
        override fun calculatePossibleMovesOfPiece(
            mutableGame: MutableGame,
            position: Coordinate
        ): List<RevertableMove> {
            val piece = mutableGame.board.get(position)
            if (piece == null) {
                logger.e(TAG, "Piece must not be null here! Something is wrong with the code")
                return listOf()
            }
            val possibleMoves = piece.basicMoves.calculateBasicMoves(mutableGame, position)
            return filterMovesThatLeaveKingInCheck(mutableGame, possibleMoves, piece.player)

        }
    }

    class BishopMovesProvider(
        override val logger: Logger = LoggerFactory().create()
    ) : PossibleMovesProvider(logger) {
        override fun calculatePossibleMovesOfPiece(
            mutableGame: MutableGame,
            position: Coordinate
        ): List<RevertableMove> {
            val piece = mutableGame.board.get(position)
            if (piece == null) {
                logger.e(TAG, "Piece must not be null here! Something is wrong with the code")
                return listOf()
            }
            val possibleMoves = piece.basicMoves.calculateBasicMoves(mutableGame, position)
            return filterMovesThatLeaveKingInCheck(mutableGame, possibleMoves, piece.player)

        }
    }

    class QueenMovesProvider(
        override val logger: Logger = LoggerFactory().create()
    ) : PossibleMovesProvider(logger) {
        override fun calculatePossibleMovesOfPiece(
            mutableGame: MutableGame,
            position: Coordinate
        ): List<RevertableMove> {
            val piece = mutableGame.board.get(position)
            if (piece == null) {
                logger.e(TAG, "Piece must not be null here! Something is wrong with the code")
                return listOf()
            }
            val possibleMoves = piece.basicMoves.calculateBasicMoves(mutableGame, position)
            return filterMovesThatLeaveKingInCheck(mutableGame, possibleMoves, piece.player)

        }
    }

    class KingMovesProvider(
        override val logger: Logger = LoggerFactory().create()
    ) : PossibleMovesProvider(logger) {
        override fun calculatePossibleMovesOfPiece(
            mutableGame: MutableGame,
            position: Coordinate
        ): List<RevertableMove> {
            val piece = mutableGame.board.get(position)
            if (piece == null) {
                logger.e(TAG, "Piece must not be null here! Something is wrong with the code")
                return listOf()
            }
            val possibleMoves =
                piece.basicMoves.calculateBasicMoves(mutableGame, position).toMutableList()
            possibleMoves.addCastlingMoves(mutableGame, position)
            return filterMovesThatLeaveKingInCheck(mutableGame, possibleMoves, piece.player)
        }
    }

    protected fun MutableList<RevertableMove>.addPromotePawnMoves(
        mutableGame: MutableGame,
        position: Coordinate
    ) {
        val player = mutableGame.getPlayerOrNull(position, TAG) ?: return
        val toRow = when (player) {
            Player.WHITE -> 0
            Player.BLACK -> 7
        }
        val requiredRow = when (player) {
            Player.WHITE -> 1
            Player.BLACK -> 6
        }

        if (position.row == requiredRow) {
            replaceMoveWithPromotePawnMoves(
                position,
                position.copy(row = toRow).left(),
                captureAndPromote = true
            )
            replaceMoveWithPromotePawnMoves(position, position.copy(row = toRow))
            replaceMoveWithPromotePawnMoves(
                position,
                position.copy(row = toRow).right(),
                captureAndPromote = true
            )
        }
    }

    private fun MutableList<RevertableMove>.replaceMoveWithPromotePawnMoves(
        position: Coordinate,
        toPosition: Coordinate,
        captureAndPromote: Boolean = false
    ) {
        if (!toPosition.isValid()) {
            return
        }
        val matchingMoves = filter { it.fromPos == position && it.toPos == toPosition }
        if (!matchingMoves.any()) {
            return
        }
        if (matchingMoves.size != 1) {
            logger.e(
                TAG,
                "replaceMoveWithPromotePawnMoves expectes exactly 1 matching move, but found ${matchingMoves.size}"
            )
        }
        remove(matchingMoves.first())

        add(PromotePawnMove(position, toPosition, Type.QUEEN, captureAndPromote))
        add(PromotePawnMove(position, toPosition, Type.ROOK, captureAndPromote))
        add(PromotePawnMove(position, toPosition, Type.BISHOP, captureAndPromote))
        add(PromotePawnMove(position, toPosition, Type.KNIGHT, captureAndPromote))
    }

    protected fun MutableList<RevertableMove>.addEnPassantMoves(
        mutableGame: MutableGame,
        position: Coordinate
    ) {
        when (val player = mutableGame.getPlayerOrNull(position, TAG)) {
            Player.WHITE -> {
                if (position.row == 3) {
                    val enPassantPositions = listOf(position.left(), position.right())
                    enPassantPositions.forEach { curPos ->
                        if (curPos.isValid()
                            && mutableGame.board.isPlayer(curPos, player.opponent())
                            && mutableGame.simulatableMoveStack.lastMoveWas(
                                curPos.up().up(),
                                curPos
                            )
                        ) {
                            add(
                                CaptureEnPassantMove(
                                    fromPos = position,
                                    toPos = curPos.up(),
                                    capturePiecePos = curPos
                                )
                            )
                        }
                    }
                }
            }
            Player.BLACK -> {
                if (position.row == 4) {
                    val enPassantPositions = listOf(position.left(), position.right())
                    enPassantPositions.forEach { curPos ->
                        if (curPos.isValid()
                            && mutableGame.board.isPlayer(curPos, player.opponent())
                            && mutableGame.simulatableMoveStack.lastMoveWas(
                                curPos.down().down(),
                                curPos
                            )
                        ) {
                            add(
                                CaptureEnPassantMove(
                                    fromPos = position,
                                    toPos = curPos.down(),
                                    capturePiecePos = curPos
                                )
                            )
                        }
                    }
                }
            }
            null -> {
                throw Exception("This should not be possible! There is something wrong! Player must always be black or white here.")
            }
        }
    }

    protected fun MutableList<RevertableMove>.addCastlingMoves(
        mutableGame: MutableGame,
        position: Coordinate
    ) {
        val piece = mutableGame.board.get(position)
        if (piece == null || piece.type != Type.KING) {
            return
        }

        val player = mutableGame.getPlayerOrNull(position, TAG) ?: return
        when (player) {
            Player.WHITE -> {
                if (checkCastingConditionFulfilled(
                        mutableGame,
                        Coordinate(7, 4),
                        Coordinate(7, 7)
                    )
                ) {
                    add(
                        CastlingMove(
                            kingFromPos = Coordinate(7, 4),
                            kingToPos = Coordinate(7, 6),
                            rookFromPos = Coordinate(7, 7),
                            rookToPos = Coordinate(7, 5)
                        )
                    )
                }
                if (checkCastingConditionFulfilled(
                        mutableGame,
                        Coordinate(7, 4),
                        Coordinate(7, 0)
                    )
                ) {
                    add(
                        CastlingMove(
                            kingFromPos = Coordinate(7, 4),
                            kingToPos = Coordinate(7, 2),
                            rookFromPos = Coordinate(7, 0),
                            rookToPos = Coordinate(7, 3)
                        )
                    )
                }
            }
            Player.BLACK -> {
                if (checkCastingConditionFulfilled(
                        mutableGame,
                        Coordinate(0, 4),
                        Coordinate(0, 7)
                    )
                ) {
                    add(
                        CastlingMove(
                            kingFromPos = Coordinate(0, 4),
                            kingToPos = Coordinate(0, 6),
                            rookFromPos = Coordinate(0, 7),
                            rookToPos = Coordinate(0, 5)
                        )
                    )
                }
                if (checkCastingConditionFulfilled(
                        mutableGame,
                        Coordinate(0, 4),
                        Coordinate(0, 0)
                    )
                ) {
                    add(
                        CastlingMove(
                            kingFromPos = Coordinate(0, 4),
                            kingToPos = Coordinate(0, 2),
                            rookFromPos = Coordinate(0, 0),
                            rookToPos = Coordinate(0, 3)
                        )
                    )
                }
            }
        }
    }

    private fun checkCastingConditionFulfilled(
        mutableGame: MutableGame,
        kingPos: Coordinate,
        rookPos: Coordinate
    ): Boolean {
        val king = mutableGame.board.get(kingPos)
        val rook = mutableGame.board.get(rookPos)
        if (king == null || rook == null) {
            return false
        }

        if (king.type != Type.KING || king.isMoved) {
            return false
        }
        if (rook.type != Type.ROOK || rook.player != king.player || rook.isMoved) {
            return false
        }

        // Check that cells between rook and king are empty
        val fieldsInCheck = mutableGame.boardStatus.getCellsInCheck(king.player)
        val row = kingPos.row
        val fromCol =
            if (kingPos.column > rookPos.column) rookPos.column + 1 else kingPos.column + 1
        val toCol =
            if (kingPos.column > rookPos.column) kingPos.column - 1 else rookPos.column - 1

        for (i in fromCol..toCol) {
            if (mutableGame.board.get(
                    Coordinate(
                        row,
                        i
                    )
                ) != null || fieldsInCheck[row][i]
            ) {
                return false
            }
        }

        return true
    }

    protected fun filterMovesThatLeaveKingInCheck(
        mutableGame: MutableGame,
        moves: List<RevertableMove>,
        player: Player
    ): List<RevertableMove> {
        val filteredMoves = mutableListOf<RevertableMove>()
        moves.forEach { move ->
            mutableGame.executeMove(move, true)
            if (!mutableGame.boardStatus.kingIsInCheck(player)) {
                filteredMoves.add(move)
            }
            mutableGame.rollbackSimulatedMoves()
        }
        return filteredMoves
    }
}


