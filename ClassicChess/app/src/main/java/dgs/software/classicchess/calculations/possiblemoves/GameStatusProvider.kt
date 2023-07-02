package dgs.software.classicchess.calculations.possiblemoves

import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.MutableGame
import dgs.software.classicchess.model.Player

private val TAG = "GameStatusProvider"

interface GameStatusProvider {
    fun isStalemate(mutableGame: MutableGame, player: Player) : Boolean
    fun isCheckmate(mutableGame: MutableGame, player: Player) : Boolean
}

class DefaultGameStatusProvider(
    private val possibleMovesProvider: PossibleMovesProvider = DefaultPossibleMovesProvider(),
    private val boardStatusProvider: BoardStatusProvider = DefaultBoardStatusProvider()
) : GameStatusProvider {
    override fun isStalemate(mutableGame: MutableGame, player: Player) : Boolean {
        return !boardStatusProvider.kingIsInCheck(mutableGame, player) && !playerCanPerformMove(mutableGame, player)
    }

    override fun isCheckmate(mutableGame: MutableGame, player: Player) : Boolean {
        val kingIsInCheck = boardStatusProvider.kingIsInCheck(mutableGame, player)
        val playerCanPerformMove = playerCanPerformMove(mutableGame, player)
        return kingIsInCheck && !playerCanPerformMove
    }

    private fun playerCanPerformMove(mutableGame: MutableGame, player: Player) : Boolean {
        for (i in 0 until 8) {
            for (j in 0 until 8) {
                val pos = Coordinate(i,j)
                if (mutableGame.board.isPlayer(pos, player)) {
                    val possibleMoves = possibleMovesProvider.getPossibleMoves(mutableGame, pos)
                    if (possibleMoves.any()) {
                        return true
                    }
                }
            }
        }
        return false
    }
}