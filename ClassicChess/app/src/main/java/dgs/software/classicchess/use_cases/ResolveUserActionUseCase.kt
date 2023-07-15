package dgs.software.classicchess.use_cases

import UserClickAction
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.Game
import dgs.software.classicchess.model.Piece
import dgs.software.classicchess.model.moves.PromotePawnMove
import dgs.software.classicchess.model.moves.RevertableMove

class ResolveUserClickActionUseCase {
    fun execute(game: Game, possibleMoves: List<RevertableMove>, clickedCoordinate: Coordinate) : UserClickAction {
        val selectedPiece: Piece? = game.board.get(clickedCoordinate)
        val clickedMove = possibleMoves.filter { it.toPos == clickedCoordinate }

        return if (clickedMove.any()) {
            if (clickedMove.first() is PromotePawnMove) {
                UserClickAction.ExecutePromotePawnMove(clickedMove.first().toPos)
            } else {
                UserClickAction.ExecuteRegularMove(clickedMove.first())
            }
        } else if (selectedPiece == null) {
            UserClickAction.EmptyCellSelected
        } else if (selectedPiece.player == game.currentPlayer) {
            UserClickAction.DisplayPossibleMovesOfPiece
        } else {
            UserClickAction.OpponentCellSelected
        }
    }
}