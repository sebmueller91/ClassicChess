package dgs.software.classicchess.use_cases

import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.Piece
import dgs.software.classicchess.model.moves.PromotePawnMove
import dgs.software.classicchess.model.moves.RevertableMove
import dgs.software.classicchess.ui.screens.LocalGameUiState

class ResolveUserClickActionUseCase {
    fun execute(localGameUiState: LocalGameUiState, clickedCoordinate: Coordinate) : UserClickAction {
        val selectedPiece: Piece? = localGameUiState.game.board.get(clickedCoordinate)
        val clickedMove = localGameUiState.possibleMovesForSelectedPiece.filter { it.toPos == clickedCoordinate }

        return if (clickedMove.any()) {
            if (clickedMove.first() is PromotePawnMove) {
                UserClickAction.ExecutePromotePawnMove(clickedMove.first().toPos)
            } else {
                UserClickAction.ExecuteRegularMove(clickedMove.first())
            }
        } else if (selectedPiece == null) {
            UserClickAction.EmptyCellSelected
        } else if (selectedPiece.player == localGameUiState.game.currentPlayer) {
            UserClickAction.DisplayPossibleMovesOfPiece
        } else {
            UserClickAction.OpponentCellSelected
        }
    }
}

sealed class UserClickAction() {
    object EmptyCellSelected: UserClickAction()
    object DisplayPossibleMovesOfPiece: UserClickAction()
    object OpponentCellSelected: UserClickAction()
    data class ExecuteRegularMove(val revertableMove: RevertableMove): UserClickAction()
    data class ExecutePromotePawnMove(val coordinate: Coordinate): UserClickAction()
}