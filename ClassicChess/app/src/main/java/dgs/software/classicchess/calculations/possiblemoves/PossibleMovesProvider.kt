package dgs.software.classicchess.calculations.possiblemoves

import android.util.Log
import dgs.software.classicchess.model.*
import dgs.software.classicchess.model.moves.CaptureEnPassantMove
import dgs.software.classicchess.model.moves.CastlingMove
import dgs.software.classicchess.model.moves.PromotePawnMove
import dgs.software.classicchess.model.moves.RevertableMove
import dgs.software.classicchess.utils.getPlayerOrNull

private val TAG = "PossibleMovesProvider"


class PossibleMovesProvider(
    private val basicPossibleMovesProvider: BasicMovesProvider,
    private val boardStatusProvider: BoardStatusProvider
) {
    private lateinit var _mutableGame: MutableGame

    fun getPossibleMoves(
        mutableGame: MutableGame,
        position: Coordinate
    ): List<RevertableMove> {
        _mutableGame = mutableGame

        val piece = _mutableGame.board.get(position)
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
        var possibleMoves =
            basicPossibleMovesProvider.getBasicMoves(_mutableGame, position).toMutableList()
        possibleMoves.addEnPassantMoves(position)
        possibleMoves.addPromotePawnMoves(position)
        return possibleMoves
    }

    private fun getMovesForRook(position: Coordinate): List<RevertableMove> {
        return basicPossibleMovesProvider.getBasicMoves(_mutableGame, position)
    }

    private fun getMovesForKnight(position: Coordinate): List<RevertableMove> {
        return basicPossibleMovesProvider.getBasicMoves(_mutableGame, position)
    }

    private fun getMovesForBishop(position: Coordinate): List<RevertableMove> {
        return basicPossibleMovesProvider.getBasicMoves(_mutableGame, position)
    }

    private fun getMovesForQueen(position: Coordinate): List<RevertableMove> {
        return basicPossibleMovesProvider.getBasicMoves(_mutableGame, position)
    }

    private fun getMovesForKing(position: Coordinate): List<RevertableMove> {
        var possibleMoves =
            basicPossibleMovesProvider.getBasicMoves(_mutableGame, position).toMutableList()
        possibleMoves.addCastlingMoves(position)
        return possibleMoves
    }

    private fun MutableList<RevertableMove>.addPromotePawnMoves(position: Coordinate) {
        val player = _mutableGame.getPlayerOrNull(position, TAG) ?: return
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

        add(PromotePawnMove(position, toPosition, Type.QUEEN, captureAndPromote))
        add(PromotePawnMove(position, toPosition, Type.ROOK, captureAndPromote))
        add(PromotePawnMove(position, toPosition, Type.BISHOP, captureAndPromote))
        add(PromotePawnMove(position, toPosition, Type.KNIGHT, captureAndPromote))
    }

    private fun MutableList<RevertableMove>.addEnPassantMoves(position: Coordinate) {
        val player = _mutableGame.getPlayerOrNull(position, TAG)
        when (player) {
            Player.WHITE -> {
                if (position.row == 3) {
                    val enPassantPositions = listOf(position.left(), position.right())
                    enPassantPositions.forEach { curPos ->
                        if (curPos.isValid()
                            && _mutableGame.board.isPlayer(curPos, player.opponent())
                            && _mutableGame.simulatableMoveStack.lastMoveWas(
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
                            && _mutableGame.board.isPlayer(curPos, player.opponent())
                            && _mutableGame.simulatableMoveStack.lastMoveWas(
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

    private fun MutableList<RevertableMove>.addCastlingMoves(position: Coordinate) {
        val piece = _mutableGame.board.get(position)
        if (piece == null || piece.type != Type.KING) {
            return
        }

        val player = _mutableGame.getPlayerOrNull(position, TAG) ?: return
        when (player) {
            Player.WHITE -> {
                if (checkCastingConditionFulfilled(Coordinate(7, 4), Coordinate(7, 7))) {
                    add(
                        CastlingMove(
                            kingFromPos = Coordinate(7, 4),
                            kingToPos = Coordinate(7, 6),
                            rookFromPos = Coordinate(7, 7),
                            rookToPos = Coordinate(7, 5)
                        )
                    )
                }
                if (checkCastingConditionFulfilled(Coordinate(7, 4), Coordinate(7, 0))) {
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
                if (checkCastingConditionFulfilled(Coordinate(0, 4), Coordinate(0, 7))) {
                    add(
                        CastlingMove(
                            kingFromPos = Coordinate(0, 4),
                            kingToPos = Coordinate(0, 6),
                            rookFromPos = Coordinate(0, 7),
                            rookToPos = Coordinate(0, 5)
                        )
                    )
                }
                if (checkCastingConditionFulfilled(Coordinate(0, 4), Coordinate(0, 0))) {
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
        kingPos: Coordinate,
        rookPos: Coordinate
    ): Boolean {
        val king = _mutableGame.board.get(kingPos)
        val rook = _mutableGame.board.get(rookPos)
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
        val fieldsInCheck = boardStatusProvider.getCellsInCheck(_mutableGame, king.player)
        val row = kingPos.row
        val fromCol =
            if (kingPos.column > rookPos.column) rookPos.column + 1 else kingPos.column + 1
        val toCol = if (kingPos.column > rookPos.column) kingPos.column - 1 else rookPos.column - 1

        for (i in fromCol..toCol) {
            if (_mutableGame.board.get(Coordinate(row, i)) != null || fieldsInCheck[row][i]) {
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
            _mutableGame.executeMove(move, true)
            if (!boardStatusProvider.kingIsInCheck(_mutableGame, player)) {
                filteredMoves.add(move)
            }
            _mutableGame.rollbackSimulatedMoves()
        }
        return filteredMoves
    }
}


