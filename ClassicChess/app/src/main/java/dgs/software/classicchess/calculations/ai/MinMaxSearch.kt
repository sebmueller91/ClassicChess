package dgs.software.classicchess.calculations.ai

import dgs.software.classicchess.calculations.possiblemoves.GameStatusProvider
import dgs.software.classicchess.calculations.possiblemoves.PossibleMovesProvider
import dgs.software.classicchess.model.MutableGame
import dgs.software.classicchess.model.Player
import dgs.software.classicchess.model.Type
import dgs.software.classicchess.model.moves.*

private const val USE_TRANSPOSITION_TABLE = true
private const val USE_MOVE_ORDERING = true

class MinMaxSearch(
    private val evaluationFunction: EvaluationFunction
) {
    private val transpositionTable = HashMap<Int, Int>()

    fun search(mutableGame: MutableGame, player: Player): RevertableMove {
        val bestMoveSeries = minMax(
            mutableGame,
            evaluationFunction.searchDepth,
            player,
            Int.MIN_VALUE,
            Int.MAX_VALUE,
            null
        )
        return bestMoveSeries ?: throw RuntimeException("No valid moves found")
    }

    private fun minMax(
        mutableGame: MutableGame,
        depth: Int,
        player: Player,
        alpha: Int,
        beta: Int,
        move: RevertableMove?
    ): RevertableMove? {
        val gameStatusProvider = GameStatusProvider(mutableGame)

        if (depth == 0 || gameStatusProvider.isGameOver()) {
            return move
        }

        if (USE_TRANSPOSITION_TABLE) {
            transpositionTable[mutableGame.hashCode()]?.let { return move }
        }

        var alpha = alpha
        var beta = beta
        var bestMove: RevertableMove? = null
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
            val resultMove =
                minMax(mutableGame, depth - 1, player.opponent(), alpha, beta, possibleMove)
            mutableGame.rollbackAndDeleteLastMove(mutableGame)

            val evaluation = evaluationFunction.evaluate(mutableGame, player)

            if (USE_TRANSPOSITION_TABLE) {
                transpositionTable[mutableGame.hashCode()] = evaluation
            }

            if (player == Player.WHITE && resultMove != null) {
                if (evaluation > bestValue) {
                    bestMove = resultMove
                    bestValue = evaluation
                }
                alpha = maxOf(alpha, bestValue)

                if (beta <= alpha) {
                    break
                }
            } else if (player == Player.BLACK && resultMove != null) {
                if (evaluation < bestValue) {
                    bestMove = resultMove
                    bestValue = evaluation
                }
                beta = minOf(beta, bestValue)

                if (beta <= alpha) {
                    break
                }
            }
        }

        return bestMove
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