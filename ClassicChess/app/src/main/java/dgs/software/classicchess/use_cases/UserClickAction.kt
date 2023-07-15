import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.moves.RevertableMove

sealed class UserClickAction() {
    object EmptyCellSelected: UserClickAction()
    object DisplayPossibleMovesOfPiece: UserClickAction()
    object OpponentCellSelected: UserClickAction()
    data class ExecuteRegularMove(val revertableMove: RevertableMove): UserClickAction()
    data class ExecutePromotePawnMove(val coordinate: Coordinate): UserClickAction()
}