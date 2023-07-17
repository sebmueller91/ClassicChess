package dgs.software.classicchess.utils

import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.MutableGame
import dgs.software.classicchess.model.Player
import java.util.*

fun MutableGame.getPlayerOrNull(coordinate: Coordinate, logTag: String = "NOTAG"): Player? {
    val piece = board.get(coordinate)
    if (piece == null) {
        return null
    }
    return piece.player
}

fun random64BitNumber(): Long {
    val random = Random()
    return random.nextLong()
}