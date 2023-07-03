package dgs.software.classicchess.calculations.possiblemoves

import android.util.Log
import dgs.software.classicchess.model.*

private const val TAG = "BoardStatusProvider"

class BoardStatusProvider(
    private val basicMovesProvider: BasicMovesProvider) {
    fun kingIsInCheck(mutableGame: MutableGame, player: Player) : Boolean {
        val positionOfKing = getPositionOfKing(mutableGame, player)
        val cellsInCheck = getCellsInCheck(mutableGame, player)
        return cellsInCheck[positionOfKing.row][positionOfKing.column]
    }

    fun getCellsInCheck(mutableGame: MutableGame, player: Player): Array<Array<Boolean>> {
        val fieldsInCheck = Array(8) { Array(8) { false } }

        for (i in 0 until 8) {
            for (j in 0 until 8) {
                val pos = Coordinate(i,j)
                if (mutableGame.board.isPlayer(pos, player.opponent())) {
                    val possibleMoves = basicMovesProvider.getBasicMoves(mutableGame, pos)
                    possibleMoves.forEach{
                        fieldsInCheck[it.toPos.row][it.toPos.column] = true
                    }
                }
            }
        }

        return fieldsInCheck
    }

    fun getPositionOfKing(mutableGame: MutableGame, player: Player) : Coordinate {
        for (i in 0 until 8) {
            for (j in 0 until 8) {
                val piece = mutableGame.board.get(Coordinate(i,j))
                if (piece?.player == player && piece.type == Type.KING) {
                    return Coordinate(i,j)
                }
            }
        }
        Log.e(TAG, "No king found for player $player")
        throw java.lang.IllegalStateException("No king found for player $player")
    }


}