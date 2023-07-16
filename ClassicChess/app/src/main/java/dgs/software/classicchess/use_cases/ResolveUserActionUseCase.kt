package dgs.software.classicchess.use_cases

import UserClickAction
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.Game
import dgs.software.classicchess.model.Piece
import dgs.software.classicchess.model.Player
import dgs.software.classicchess.model.moves.PromotePawnMove
import dgs.software.classicchess.model.moves.RevertableMove

class ResolveUserClickActionUseCase {
    fun executeLocalGame(
        game: Game,
        possibleMoves: List<RevertableMove>,
        clickedCoordinate: Coordinate
    ): UserClickAction {
        val selectedPiece: Piece? = game.board.get(clickedCoordinate)
        val clickedMove = possibleMoves.filter { it.toPos == clickedCoordinate }

        return if (clickedMove.any()) {
            if (clickedMove.first() is PromotePawnMove) {
                UserClickAction.ExecutePromotePawnMove(clickedMove.first().toPos)
            } else {
                UserClickAction.ExecuteRegularMove(clickedMove.first())
            }
        } else if (selectedPiece == null) {
            UserClickAction.NoActionCellSelected
        } else if (selectedPiece.player == game.currentPlayer) {
            UserClickAction.DisplayPossibleMovesOfPiece
        } else {
            UserClickAction.OpponentCellSelected
        }
    }

    fun executeAiGame(
        game: Game,
        possibleMoves: List<RevertableMove>,
        clickedCoordinate: Coordinate,
        computerPlayer: Player
    ): UserClickAction {
        val selectedPiece: Piece? = game.board.get(clickedCoordinate)
        val clickedMove = possibleMoves.filter { it.toPos == clickedCoordinate }

        return if (game.currentPlayer == computerPlayer) {
            UserClickAction.NoActionCellSelected
        } else if (clickedMove.any()) {
            if (clickedMove.first() is PromotePawnMove) {
                UserClickAction.ExecutePromotePawnMove(clickedMove.first().toPos)
            } else {
                UserClickAction.ExecuteRegularMove(clickedMove.first())
            }
        } else if (selectedPiece == null) {
            UserClickAction.NoActionCellSelected
        } else if (selectedPiece.player == game.currentPlayer) {
            UserClickAction.DisplayPossibleMovesOfPiece
        } else {
            UserClickAction.OpponentCellSelected
        }
    }
}