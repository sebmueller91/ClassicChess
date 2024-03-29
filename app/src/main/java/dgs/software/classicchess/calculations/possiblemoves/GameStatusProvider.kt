package dgs.software.classicchess.calculations.possiblemoves

import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.MutableGame
import dgs.software.classicchess.model.Player
import dgs.software.classicchess.ui.theme.White

private val TAG = "GameStatusProvider"

class GameStatusProvider(
    private val mutableGame: MutableGame
) {
    fun isGameOver(): Boolean {
        return isStalemate(Player.WHITE) ||
                isStalemate(Player.BLACK) ||
                isCheckmate(Player.WHITE) ||
                isCheckmate(Player.BLACK)
    }

    fun isStalemate(player: Player): Boolean {
        return !mutableGame.boardStatus.kingIsInCheck(player) && !playerCanPerformMove(player)
    }

    fun isCheckmate(player: Player): Boolean {
        val kingIsInCheck = mutableGame.boardStatus.kingIsInCheck(player)
        val playerCanPerformMove = playerCanPerformMove(player)
        return kingIsInCheck && !playerCanPerformMove
    }

    private fun playerCanPerformMove(player: Player): Boolean {
        for (i in 0 until 8) {
            for (j in 0 until 8) {
                val pos = Coordinate(i, j)
                val piece = mutableGame.board.get(pos)
                if (piece?.player != player) {
                    continue
                }
                val possibleMoves = piece.moves.calculatePossibleMovesOfPiece(mutableGame, pos)
                if (possibleMoves.any()) {
                    return true
                }
            }
        }
        return false
    }
}