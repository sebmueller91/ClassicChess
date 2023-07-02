package dgs.software.classicchess.use_cases

import dgs.software.classicchess.calculations.possiblemoves.BoardStatusProvider
import dgs.software.classicchess.calculations.possiblemoves.DefaultBoardStatusProvider
import dgs.software.classicchess.calculations.possiblemoves.DefaultGameStatusProvider
import dgs.software.classicchess.model.*

class UpdateGameStatusUseCase(
    private val gameStatusProvider: DefaultGameStatusProvider = DefaultGameStatusProvider(),
    private val boardStatusProvider: BoardStatusProvider = DefaultBoardStatusProvider()
) {
    fun execute(game: Game): GameStatusInfo {
        val mutableGame = game.toMutableGame()
        return GameStatusInfo(
            kingInCheck = getKingInCheck(mutableGame),
            playerStalemate = getPlayerStalemate(mutableGame),
            playerWon = getPlayerWon(mutableGame)
        )
    }

    private fun getKingInCheck(mutableGame: MutableGame): Coordinate? {
        Player.values().forEach { player ->
            val positionOfKing = boardStatusProvider.getPositionOfKing(mutableGame, player)
            val cellsInCheck = boardStatusProvider.getCellsInCheck(mutableGame,player)
            if (cellsInCheck[positionOfKing.row][positionOfKing.column]) {
                return positionOfKing
            }
        }
        return null
    }

    private fun getPlayerWon(mutableGame: MutableGame): Player? {
        return if (gameStatusProvider.isCheckmate(mutableGame, Player.WHITE)) {
            Player.BLACK
        } else if (gameStatusProvider.isCheckmate(mutableGame,Player.BLACK)) {
            Player.WHITE
        } else {
            null
        }
    }

    private fun getPlayerStalemate(mutableGame: MutableGame): Player? {
        return if (gameStatusProvider.isStalemate(mutableGame, Player.WHITE)) {
            Player.BLACK
        } else if (gameStatusProvider.isStalemate(mutableGame,Player.BLACK)) {
            Player.WHITE
        } else {
            null
        }
    }
}

class GameStatusInfo(
    val kingInCheck: Coordinate? = null,
    val playerWon: Player? = null,
    val playerStalemate: Player? = null
)