package dgs.software.classicchess.calculations.possiblemoves

import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.Game
import dgs.software.classicchess.model.Player

private val TAG = "GameStatusProvider"

interface GameStatusProvider {
    fun isStalemate(player: Player) : Boolean
    fun isCheckmate(player: Player) : Boolean
}

class DefaultGameStatusProvider(
    private val game: Game,
    private val possibleMovesProvider: PossibleMovesProvider = DefaultPossibleMovesProvider(game),
    private val boardStatusProvider: BoardStatusProvider = DefaultBoardStatusProvider(game)
) : GameStatusProvider {
    override fun isStalemate(player: Player) : Boolean {
        return !boardStatusProvider.kingIsInCheck(player) && !playerCanPerformMove(player)
    }

    override fun isCheckmate(player: Player) : Boolean {
        val kingIsInCheck = boardStatusProvider.kingIsInCheck(player)
        val playerCanPerformMove = playerCanPerformMove(player)
        return kingIsInCheck && !playerCanPerformMove
    }

    private fun playerCanPerformMove(player: Player) : Boolean {
        for (i in 0 until 8) {
            for (j in 0 until 8) {
                val pos = Coordinate(i,j)
                if (game.get(pos).isPlayer(player)) {
                    val possibleMoves = possibleMovesProvider.getPossibleMoves(pos)
                    if (possibleMoves.any()) {
                        return true
                    }
                }
            }
        }
        return false
    }
}