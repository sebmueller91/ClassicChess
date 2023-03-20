package dgs.software.classicchess.calculations.possiblemoves

import android.util.Log
import dgs.software.classicchess.model.*
import dgs.software.classicchess.model.moves.CaptureEnPassantMove
import dgs.software.classicchess.model.moves.CastlingMove
import dgs.software.classicchess.model.moves.PromotePawnMove
import dgs.software.classicchess.model.moves.RevertableMove

private val TAG = "PossibleMovesProvider"

interface PossibleMovesProvider {
    fun getPossibleMoves(position: Coordinate): List<RevertableMove>
}

class DefaultPossibleMovesProvider(
    private val game: Game,
    private val basicPossibleMovesProvider: BasicMovesProvider = DefaultBasicMovesProvider(game),
    private val boardStatusProvider: BoardStatusProvider = DefaultBoardStatusProvider(game)
) : PossibleMovesProvider {
    override fun getPossibleMoves(position: Coordinate): List<RevertableMove> {
        if (game.get(position) is Cell.Empty) {
            Log.e(TAG, "Tried to calculate moves for an empty cell at pos $position")
            return listOf<RevertableMove>()
        }
        val piece = game.getAsPiece(position)
        val possibleMoves = when (piece.type) {
            Type.PAWN -> getMovesForPawn(position)
            Type.ROOK -> getMovesForRook(position)
            Type.KNIGHT -> getMovesForKnight(position)
            Type.BISHOP -> getMovesForBishop(position)
            Type.QUEEN -> getMovesForQueen(position)
            Type.KING -> getMovesForKing(position)
        }

        return filterMovesThatLeaveKingInCheck(possibleMoves, piece.player)
    }

    private fun getMovesForPawn(position: Coordinate): List<RevertableMove> {
        var possibleMoves = basicPossibleMovesProvider.getBasicMoves(position).toMutableList()
        possibleMoves.addEnPassantMoves(position)
        possibleMoves.addPromotePawnMoves(position)
        return possibleMoves
    }

    private fun getMovesForRook(position: Coordinate): List<RevertableMove> {
        return basicPossibleMovesProvider.getBasicMoves(position)
    }

    private fun getMovesForKnight(position: Coordinate): List<RevertableMove> {
        return basicPossibleMovesProvider.getBasicMoves(position)
    }

    private fun getMovesForBishop(position: Coordinate): List<RevertableMove> {
        return basicPossibleMovesProvider.getBasicMoves(position)
    }

    private fun getMovesForQueen(position: Coordinate): List<RevertableMove> {
        return basicPossibleMovesProvider.getBasicMoves(position)
    }

    private fun getMovesForKing(position: Coordinate): List<RevertableMove> {
        var possibleMoves = basicPossibleMovesProvider.getBasicMoves(position).toMutableList()
        possibleMoves.addCastlingMoves(position)
        return possibleMoves
    }

    private fun MutableList<RevertableMove>.addPromotePawnMoves(position: Coordinate) {
        val pawn = game.getAsPiece(position)
        val toRow = when (pawn.player) {
            Player.WHITE -> 0
            Player.BLACK -> 7
        }
        val requiredRow = when (pawn.player) {
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
            Log.e(
                TAG,
                "replaceMoveWithPromotePawnMoves expectes exactly 1 matching move, but found ${matchingMoves.size}"
            )
        }
        remove(matchingMoves.first())

        add(PromotePawnMove(position, toPosition, Type.QUEEN, { game }, captureAndPromote))
        add(PromotePawnMove(position, toPosition, Type.ROOK, { game }, captureAndPromote))
        add(PromotePawnMove(position, toPosition, Type.BISHOP, { game }, captureAndPromote))
        add(PromotePawnMove(position, toPosition, Type.KNIGHT, { game }, captureAndPromote))
    }

    private fun MutableList<RevertableMove>.addEnPassantMoves(position: Coordinate) {
        val pawn = game.getAsPiece(position)
        when (pawn.player) {
            Player.WHITE -> {
                if (position.row == 3) {
                    val enPassantPositions = listOf(position.left(), position.right())
                    enPassantPositions.forEach { curPos ->
                        if (curPos.isValid()
                            && game.isPlayer(curPos, pawn.player.opponent())
                            && game.simulatableMoveStack.lastMoveWas(curPos.up().up(), curPos)
                        ) {
                            add(
                                CaptureEnPassantMove(
                                    fromPos = position,
                                    toPos = curPos.up(),
                                    capturePiecePos = curPos
                                ) { game })
                        }
                    }
                }
            }
            Player.BLACK -> {
                if (position.row == 4) {
                    val enPassantPositions = listOf(position.left(), position.right())
                    enPassantPositions.forEach { curPos ->
                        if (curPos.isValid()
                            && game.isPlayer(curPos, pawn.player.opponent())
                            && game.simulatableMoveStack.lastMoveWas(curPos.down().down(), curPos)
                        ) {
                            add(
                                CaptureEnPassantMove(
                                    fromPos = position,
                                    toPos = curPos.down(),
                                    capturePiecePos = curPos
                                ) { game })
                        }
                    }
                }
            }
        }
    }

    private fun MutableList<RevertableMove>.addCastlingMoves(position: Coordinate) {
        if (game.get(position) is Cell.Empty || game.getAsPiece(position).type != Type.KING) {
            return
        }

        val castlingPositions = mutableListOf<Pair<Coordinate, Coordinate>>()
        val king = game.getAsPiece(position)
        when (king.player) {
            Player.WHITE -> {
                if (checkCastingConditionFulfilled(Coordinate(7, 4), Coordinate(7, 7))) {
                    add(
                        CastlingMove(
                            kingFromPos = Coordinate(7, 4),
                            kingToPos = Coordinate(7, 6),
                            rookFromPos = Coordinate(7, 7),
                            rookToPos = Coordinate(7, 5)
                        ) { game }
                    )
                }
                if (checkCastingConditionFulfilled(Coordinate(7, 4), Coordinate(7, 0))) {
                    add(
                        CastlingMove(
                            kingFromPos = Coordinate(7, 4),
                            kingToPos = Coordinate(7, 2),
                            rookFromPos = Coordinate(7, 0),
                            rookToPos = Coordinate(7, 3)
                        ) { game }
                    )
                }
            }
            Player.BLACK -> {
                if (checkCastingConditionFulfilled(Coordinate(0, 4), Coordinate(0, 7))) {
                    add(
                        CastlingMove(
                            kingFromPos = Coordinate(0, 4),
                            kingToPos = Coordinate(0, 6),
                            rookFromPos = Coordinate(0, 7),
                            rookToPos = Coordinate(0, 5)
                        ) { game }
                    )
                }
                if (checkCastingConditionFulfilled(Coordinate(0, 4), Coordinate(0, 0))) {
                    add(
                        CastlingMove(
                            kingFromPos = Coordinate(0, 4),
                            kingToPos = Coordinate(0, 2),
                            rookFromPos = Coordinate(0, 0),
                            rookToPos = Coordinate(0, 3)
                        ) { game }
                    )
                }
            }
        }
    }

    private fun checkCastingConditionFulfilled(
        kingPos: Coordinate,
        rookPos: Coordinate
    ): Boolean {
        if (game.get(kingPos) is Cell.Empty || game.get(rookPos) is Cell.Empty) {
            return false
        }
        val king = game.getAsPiece(kingPos)
        val rook = game.getAsPiece(rookPos)

        if (king.type != Type.KING || king.isMoved) {
            return false
        }
        if (rook.type != Type.ROOK || rook.player != king.player || rook.isMoved) {
            return false
        }

        // Check that cells between rook and king are empty
        val fieldsInCheck = boardStatusProvider.getCellsInCheck(king.player)
        val row = kingPos.row
        val fromCol =
            if (kingPos.column > rookPos.column) rookPos.column + 1 else kingPos.column + 1
        val toCol = if (kingPos.column > rookPos.column) kingPos.column - 1 else rookPos.column - 1

        for (i in fromCol..toCol) {
            if (game.get(Coordinate(row, i)) !is Cell.Empty || fieldsInCheck[row][i]) {
                return false
            }
        }

        return true
    }

    private fun filterMovesThatLeaveKingInCheck(
        moves: List<RevertableMove>,
        player: Player
    ): List<RevertableMove> {
        val filteredMoves = mutableListOf<RevertableMove>()
        moves.forEach { move ->
            game.executeMove(move, true)
            if (!boardStatusProvider.kingIsInCheck(player)) {
                filteredMoves.add(move)
            }
            game.rollbackSimulatedMoves()
        }
        return filteredMoves
    }
}


