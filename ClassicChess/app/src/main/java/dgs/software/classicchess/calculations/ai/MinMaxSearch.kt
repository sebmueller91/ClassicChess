package dgs.software.classicchess.calculations.ai

import dgs.software.classicchess.calculations.possiblemoves.GameStatusProvider
import dgs.software.classicchess.calculations.possiblemoves.PossibleMovesProvider
import dgs.software.classicchess.model.MutableGame
import dgs.software.classicchess.model.Player
import dgs.software.classicchess.model.Type
import dgs.software.classicchess.model.moves.*
import java.util.concurrent.ConcurrentHashMap

private const val USE_TRANSPOSITION_TABLE = false
private const val USE_MOVE_ORDERING = true

class MinMaxSearch(
    private val evaluationFunction: EvaluationFunction
) {
    private val transpositionTable = ConcurrentHashMap<Long, Int>()

    fun search(mutableGame: MutableGame, player: Player): RevertableMove {
        var bestMove: RevertableMove? = null
        var bestValue = if (player == Player.WHITE) Int.MIN_VALUE else Int.MAX_VALUE

        val possibleMoves = PossibleMovesProvider.calculatePossibleMovesOfPlayer(mutableGame, player)
        val orderedMoves = if (USE_MOVE_ORDERING) {
            possibleMoves.orderMovesByPriority(mutableGame)
        } else {
            possibleMoves
        }

        for (possibleMove in orderedMoves) {
            mutableGame.executeMove(possibleMove)
            val evaluation = minMax(mutableGame, evaluationFunction.searchDepth - 1, player.opponent(), Int.MIN_VALUE, Int.MAX_VALUE)
            mutableGame.rollbackAndDeleteLastMove(mutableGame)

            if (player == Player.WHITE && evaluation > bestValue) {
                bestMove = possibleMove
                bestValue = evaluation
            } else if (player == Player.BLACK && evaluation < bestValue) {
                bestMove = possibleMove
                bestValue = evaluation
            }
        }

        return bestMove ?: throw RuntimeException("No valid moves found")
    }

    private fun minMax(
        mutableGame: MutableGame,
        depth: Int,
        player: Player,
        alpha: Int,
        beta: Int
    ): Int {
        val gameStatusProvider = GameStatusProvider(mutableGame)

        if (depth == 0 || gameStatusProvider.isGameOver()) {
            return evaluationFunction.evaluate(mutableGame, player)
        }

        val hash = mutableGame.zobristHash()
        if (USE_TRANSPOSITION_TABLE && transpositionTable.containsKey(hash)) {
            return transpositionTable[hash]!!
        }

        var alpha = alpha
        var beta = beta
        var bestValue = if (player == Player.WHITE) Int.MIN_VALUE else Int.MAX_VALUE

        val possibleMoves =
            PossibleMovesProvider.calculatePossibleMovesOfPlayer(mutableGame, player)
        val orderedMoves = if (USE_MOVE_ORDERING) {
            possibleMoves.orderMovesByPriority(mutableGame)
        } else {
            possibleMoves
        }

        for (possibleMove in orderedMoves) {
            mutableGame.executeMove(possibleMove)
            val evaluation = minMax(mutableGame, depth - 1, player.opponent(), alpha, beta)
            mutableGame.rollbackAndDeleteLastMove(mutableGame)

            if (player == Player.WHITE) {
                bestValue = maxOf(bestValue, evaluation)
                alpha = maxOf(alpha, bestValue)
            } else {
                bestValue = minOf(bestValue, evaluation)
                beta = minOf(beta, bestValue)
            }

            if (beta <= alpha) {
                break
            }
        }

        if (USE_TRANSPOSITION_TABLE) {
            transpositionTable[hash] = bestValue
        }

        return bestValue
    }

    private fun List<RevertableMove>.orderMovesByPriority(mutableGame: MutableGame): List<RevertableMove> {
        return sortedBy { move ->
            when (move) {
                is MoveAndCapturePiece -> {
                    move.getTypeOfPieceToCapture(mutableGame).getCapturePieceOrderWeight()
                }
                is CaptureEnPassantMove -> {
                    move.getTypeOfPieceToCapture(mutableGame).getCapturePieceOrderWeight()
                }
                is PromotePawnMove -> {
                    move.getTypeOfPieceToCapture(mutableGame).getCapturePieceOrderWeight()
                }
                is MovePiece -> 6
                is CastlingMove -> 7
                else -> 8
            }
        }
    }

    private fun Type.getCapturePieceOrderWeight(): Int {
        return when (this) {
            Type.QUEEN -> 1
            Type.ROOK -> 2
            Type.BISHOP, Type.KNIGHT -> 3
            Type.PAWN -> 4
            else -> 5
        }
    }
}
