package dgs.software.classicchess.model.actions

import dgs.software.classicchess.model.Board
import dgs.software.classicchess.model.Cell
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.Game

data class CapturePieceAction(
    val coordinate: Coordinate,
    val getGame: () -> Game
) : RevertableAction() {
    lateinit var capturedPiece: Cell

    override fun execute() {
        super.execute()
        capturedPiece = getGame().get(coordinate)
        getGame().set(coordinate, Cell.Empty())
    }

    override fun rollback() {
        super.rollback()
        getGame().set(coordinate, capturedPiece)
    }
}