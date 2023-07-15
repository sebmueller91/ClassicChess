package dgs.software.classicchess.ui.components

import dgs.software.classicchess.R
import dgs.software.classicchess.model.Piece
import dgs.software.classicchess.model.Player
import dgs.software.classicchess.model.Type

fun getIconId(piece: Piece): Int {
    return when (piece.player) {
        Player.WHITE -> when (piece.type) {
            Type.PAWN -> R.drawable.pawn_white
            Type.ROOK -> R.drawable.rook_white
            Type.KNIGHT -> R.drawable.knight_white
            Type.BISHOP -> R.drawable.bishop_white
            Type.QUEEN -> R.drawable.queen_white
            Type.KING -> R.drawable.king_white
        }
        Player.BLACK -> when (piece.type) {
            Type.PAWN -> R.drawable.pawn_black
            Type.ROOK -> R.drawable.rook_black
            Type.KNIGHT -> R.drawable.knight_black
            Type.BISHOP -> R.drawable.bishop_black
            Type.QUEEN -> R.drawable.queen_black
            Type.KING -> R.drawable.king_black
        }
    }
}

// returns true if it is a cell with light background, false otherwise
fun getCellBackgroundType(rowIndex: Int, colIndex: Int): Boolean {
    return (rowIndex % 2 == 0 && colIndex % 2 == 0) || (rowIndex % 2 != 0 && colIndex % 2 != 0)
}