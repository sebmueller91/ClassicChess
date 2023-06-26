package dgs.software.classicchess.calculations.possiblemoves

import android.util.Log
import dgs.software.classicchess.model.*
import dgs.software.classicchess.model.moves.CaptureEnPassantMove
import dgs.software.classicchess.model.moves.CastlingMove
import dgs.software.classicchess.model.moves.PromotePawnMove
import dgs.software.classicchess.model.moves.RevertableMove
import dgs.software.classicchess.utils.getPlayerOrNull

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
        val piece = game.board.get(position)
        if (piece == null) {
            Log.e(TAG, "Tried to calculate moves for an empty cell at pos $position")
            return listOf<RevertableMove>()
        }
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
        val player = game.getPlayerOrNull(position, TAG) ?: return
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
        val player = game.getPlayerOrNull(position, TAG)
        when (player) {
            Player.WHITE -> {
                if (position.row == 3) {
                    val enPassantPositions = listOf(position.left(), position.right())
                    enPassantPositions.forEach { curPos ->
                        if (curPos.isValid()
                            && game.board.isPlayer(curPos, player.opponent())
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
                            && game.board.isPlayer(curPos, player.opponent())
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
        val piece = game.board.get(position)
        if (piece == null || piece.type != Type.KING) {
            return
        }

        val player = game.getPlayerOrNull(position, TAG) ?: return
        when (player) {
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
        val king = game.board.get(kingPos)
        val rook = game.board.get(rookPos)
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
        val fieldsInCheck = boardStatusProvider.getCellsInCheck(king.player)
        val row = kingPos.row
        val fromCol =
            if (kingPos.column > rookPos.column) rookPos.column + 1 else kingPos.column + 1
        val toCol = if (kingPos.column > rookPos.column) kingPos.column - 1 else rookPos.column - 1

        for (i in fromCol..toCol) {
            if (game.board.get(Coordinate(row, i)) != null || fieldsInCheck[row][i]) {
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


