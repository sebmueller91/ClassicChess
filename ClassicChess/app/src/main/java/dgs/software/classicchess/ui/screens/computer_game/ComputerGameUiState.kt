package dgs.software.classicchess.ui.screens.computer_game

import dgs.software.classicchess.calculations.ai.Difficulty
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.Game
import dgs.software.classicchess.model.Player
import dgs.software.classicchess.model.moves.RevertableMove

data class ComputerGameUiState(
    val game: Game = Game(),
    val selectedCoordinate: Coordinate? = null,
    val selectedPawnPromotionPosition: Coordinate? = null,
    val possibleMovesForSelectedPiece: List<RevertableMove> = listOf(),
    val kingInCheck: Coordinate? = null,
    val playerWon: Player? = null,
    val playerStalemate: Player? = null,
    val boardDisplayedInverted: Boolean = false,
    val requestPawnPromotionInput: Boolean = false,
    val canRedoMove: Boolean = false,
    val canUndoMove: Boolean = false,
    val canStartNewGame: Boolean = false,
    val computerPlayer: Player = Player.BLACK,
    val computeAiMove: Boolean = false,
    val difficulty: Difficulty,
)