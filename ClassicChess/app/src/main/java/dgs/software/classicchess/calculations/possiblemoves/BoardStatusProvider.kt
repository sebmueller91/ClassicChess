package dgs.software.classicchess.calculations.possiblemoves

import android.util.Log
import dgs.software.classicchess.model.*

private const val TAG = "BoardStatusProvider"

class BoardStatusProvider(
    private val mutableGame: MutableGame
) {
    fun kingIsInCheck(player: Player): Boolean {
        val positionOfKing = getPositionOfKing(player)
        val cellsInCheck = getCellsInCheck(player)
        return cellsInCheck[positionOfKing.row][positionOfKing.column]
    }

    fun getCellsInCheck(player: Player): Array<Array<Boolean>> {
        val fieldsInCheck = Array(8) { Array(8) { false } }

        for (i in 0 until 8) {
            for (j in 0 until 8) {
                val pos = Coordinate(i, j)
                if (!mutableGame.board.isPlayer(pos, player.opponent())) {
                    continue
                }
                val possibleMoves = mutableGame.board.get(pos)?.basicMoves?.calculateBasicMoves(mutableGame, pos)
                possibleMoves?.forEach {
                    fieldsInCheck[it.toPos.row][it.toPos.column] = true
                }
            }
        }

        return fieldsInCheck
    }

    fun getPositionOfKing(player: Player): Coordinate {
        for (i in 0 until 8) {
            for (j in 0 until 8) {
                val piece = mutableGame.board.get(Coordinate(i, j))
                if (piece?.player == player && piece.type == Type.KING) {
                    return Coordinate(i, j)
                }
            }
        }
        Log.e(TAG, "No king found for player $player")
        throw java.lang.IllegalStateException("No king found for player $player")
    }


}