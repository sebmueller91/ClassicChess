package dgs.software.classicchess.calculations.ai

import dgs.software.classicchess.calculations.ai.params.SearchParameters
import dgs.software.classicchess.model.MutableGame
import dgs.software.classicchess.model.Player
import dgs.software.classicchess.model.Type

class EvaluationFunction(private val searchParameters: SearchParameters) {
    val searchDepth: Int
        get() = searchParameters.searchDepth

    fun evaluate(mutableGame: MutableGame, player: Player): Int {
        var score = 0
        for (row in 0 until 8) {
            for (col in 0 until 8) {
                val piece = mutableGame.board.get(row, col)
                if (piece != null) {
                    val isEndgame = isEndgame(mutableGame)
                    val pieceValue = searchParameters.getWeight(piece)
                    val squareTable = searchParameters.getSquareTable(piece, isEndgame)
                    val positionValue = squareTable[row][col]

                    score += if (piece.player == player) {
                        pieceValue + positionValue
                    } else {
                        -pieceValue - positionValue
                    }
                }
            }
        }
        return score
    }
}

private fun isEndgame(mutableGame: MutableGame): Boolean {
    // The game is in the endgame phase if either:
    // 1. There are no queens on the board, or
    // 2. Each side that has a queen has no other pieces except at most one rook or one minor piece.

    var whiteQueensCount = 0
    var blackQueensCount = 0
    var whiteMinorPiecesCount = 0
    var blackMinorPiecesCount = 0
    var whiteRooksCount = 0
    var blackRooksCount = 0

    for (row in 0 until 8) {
        for (col in 0 until 8) {
            val piece = mutableGame.board.get(row, col)
            if (piece != null) {
                when (piece.type) {
                    Type.QUEEN -> {
                        if (piece.player == Player.WHITE) whiteQueensCount++
                        else blackQueensCount++
                    }
                    Type.BISHOP, Type.KNIGHT -> {
                        if (piece.player == Player.WHITE) whiteMinorPiecesCount++
                        else blackMinorPiecesCount++
                    }
                    Type.ROOK -> {
                        if (piece.player == Player.WHITE) whiteRooksCount++
                        else blackRooksCount++
                    }
                    else -> {}
                }
            }
        }
    }

    val whiteInEndgame = whiteQueensCount == 0 || whiteMinorPiecesCount + whiteRooksCount <= 1
    val blackInEndgame = blackQueensCount == 0 || blackMinorPiecesCount + blackRooksCount <= 1

    return whiteInEndgame || blackInEndgame
}

