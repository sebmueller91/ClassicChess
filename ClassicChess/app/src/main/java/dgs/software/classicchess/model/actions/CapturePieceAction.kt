package dgs.software.classicchess.model.actions

import dgs.software.classicchess.model.Board
import dgs.software.classicchess.model.Cell
import dgs.software.classicchess.model.Coordinate

data class CapturePieceAction(
    val board: Board,
    val coordinate: Coordinate
) : RevertableAction() {
    lateinit var capturedPiece: Cell

    override fun execute() {
        super.execute()
        capturedPiece = board.get(coordinate)
        board.set(coordinate, Cell.Empty())
    }

    override fun rollback() {
        super.rollback()
        board.set(coordinate, capturedPiece)
    }
}