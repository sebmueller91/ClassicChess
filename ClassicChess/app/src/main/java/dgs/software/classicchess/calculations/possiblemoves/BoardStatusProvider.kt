package dgs.software.classicchess.calculations.possiblemoves

import android.util.Log
import dgs.software.classicchess.model.*

private const val TAG = "BoardStatusProvider"

interface BoardStatusProvider {
    fun kingIsInCheck(player: Player) : Boolean
    fun getCellsInCheck(player: Player): Array<Array<Boolean>>
    fun getPositionOfKing(player: Player) : Coordinate
}

class DefaultBoardStatusProvider(
    private val game: Game,
    private val basicMovesProvider: BasicMovesProvider = DefaultBasicMovesProvider(game)
) : BoardStatusProvider {

    override fun kingIsInCheck(player: Player) : Boolean {
        val positionOfKing = getPositionOfKing(player)
        val cellsInCheck = getCellsInCheck(player)
        return cellsInCheck[positionOfKing.row][positionOfKing.column]
    }

    override fun getCellsInCheck(player: Player): Array<Array<Boolean>> {
        val fieldsInCheck = Array(8) { Array(8) { false } }

        for (i in 0 until 8) {
            for (j in 0 until 8) {
                val pos = Coordinate(i,j)
                if (game.get(pos).isPlayer(player.opponent())) {
                    val possibleMoves = basicMovesProvider.getBasicMoves(pos)
                    possibleMoves.forEach{
                        fieldsInCheck[it.toPos.row][it.toPos.column] = true
                    }
                }
            }
        }

        return fieldsInCheck
    }

    override fun getPositionOfKing(player: Player) : Coordinate {
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


}