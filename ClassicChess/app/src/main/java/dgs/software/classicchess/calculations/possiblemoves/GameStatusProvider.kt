package dgs.software.classicchess.calculations.possiblemoves

import android.service.notification.NotificationListenerService.Ranking
import android.util.Log
import dgs.software.classicchess.model.*
import java.nio.file.WatchEvent.Kind

private val TAG = "GameStatusProvider"

class GameStatusProvider(
    val game: Game
) {
    val basicMovesProvider = BasicMovesProvider(game)

    fun isStalemate(player: Player) : Boolean {
        return !kingIsInCheck(player) && !playerCanPerformMove(player)
    }

    fun isCheckmate(player: Player) : Boolean {
        return kingIsInCheck(player) && !playerCanPerformMove(player)
    }

    fun kingIsInCheck(player: Player) : Boolean {
        val positionOfKing = getPositionOfKing(player)
        val cellsInCheck = getCellsInCheck(player)
        return cellsInCheck[positionOfKing.row][positionOfKing.column]
    }

    private fun getCellsInCheck(player: Player): Array<Array<Boolean>> {
        val fieldsInCheck = Array(8) { Array(8) { false } }

        for (i in 0 until 8) {
            for (j in 0 until 8) {
                val pos = Coordinate(i,j)
                if (game.get(pos).isPlayer(player)) {
                    val possibleMoves = basicMovesProvider.getBasicMoves(pos)
                    possibleMoves.forEach{
                        fieldsInCheck[it.toPos.row][it.toPos.column] = true
                    }
                }
            }
        }

        return fieldsInCheck
    }

    private fun getPositionOfKing(player: Player) : Coordinate {
        for (i in 0 until 8) {
            for (j in 0 until 8) {
                val piece = game.get(Coordinate(i,j))
                if (piece.isPlayer(player) && (piece as Cell.Piece).type == Type.KING) {
                    return Coordinate(i,j)
                }
            }
        }
        Log.e(TAG, "No king found for player $player")
        throw java.lang.IllegalStateException("No king found for player $player")
    }

    private fun playerCanPerformMove(player: Player) : Boolean {
        for (i in 0 until 8) {
            for (j in 0 until 8) {
                val pos = Coordinate(i,j)
                if (game.get(pos).isPlayer(player)) {
                    val possibleMoves = basicMovesProvider.getBasicMoves(pos)
                    if (possibleMoves.any()) {
                        return true
                    }
                }
            }
        }
        return false
    }
}