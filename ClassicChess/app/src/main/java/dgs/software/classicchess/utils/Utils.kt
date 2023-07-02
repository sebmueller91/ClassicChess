package dgs.software.classicchess.utils

import android.util.Log
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.MutableGame
import dgs.software.classicchess.model.Player

fun MutableGame.getPlayerOrNull(coordinate: Coordinate, logTag: String = "NOTAG"): Player? {
    val piece = board.get(coordinate)
    if (piece == null) {
        Log.e(logTag, "Tried to access player for an empty cell at pos $coordinate.")
        return null
    }
    return piece.player
}