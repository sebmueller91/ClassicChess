package dgs.software.classicchess.use_cases

import dgs.software.classicchess.calculations.possiblemoves.BoardStatusProvider
import dgs.software.classicchess.calculations.possiblemoves.GameStatusProvider
import dgs.software.classicchess.model.*

class UpdateGameStatusUseCase {
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
            val positionOfKing = mutableGame.boardStatus.getPositionOfKing(player)
            val cellsInCheck = mutableGame.boardStatus.getCellsInCheck(player)
            if (cellsInCheck[positionOfKing.row][positionOfKing.column]) {
                return positionOfKing
            }
        }
        return null
    }

    private fun getPlayerWon(mutableGame: MutableGame): Player? =
        when {
            mutableGame.gameStatus.isCheckmate(Player.WHITE) -> {
                Player.BLACK
            }
            mutableGame.gameStatus.isCheckmate(Player.BLACK) -> {
                Player.WHITE
            }
            else -> {
                null
            }
        }

    private fun getPlayerStalemate(mutableGame: MutableGame): Player? =
        when {
            mutableGame.gameStatus.isStalemate(Player.WHITE) -> {
                Player.BLACK
            }
            mutableGame.gameStatus.isStalemate(Player.BLACK) -> {
                Player.WHITE
            }
            else -> {
                null
            }
        }
}

class GameStatusInfo(
    val kingInCheck: Coordinate? = null,
    val playerWon: Player? = null,
    val playerStalemate: Player? = null
)