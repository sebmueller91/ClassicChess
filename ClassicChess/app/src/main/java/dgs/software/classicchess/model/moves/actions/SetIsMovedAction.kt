package dgs.software.classicchess.model.moves.actions

import dgs.software.classicchess.model.Cell

data class SetIsMovedAction(
    val piece: Cell.Piece
) : RevertableAction() {
    private var previousState = false

    override fun execute() {
        super.execute()
        previousState = piece.isMoved
    }

    override fun rollback() {
        super.rollback()
        piece.isMoved = previousState
    }
}