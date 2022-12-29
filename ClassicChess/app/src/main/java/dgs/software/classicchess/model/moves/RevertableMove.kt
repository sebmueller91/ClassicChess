package dgs.software.classicchess.model.moves

import android.util.Log
import dgs.software.classicchess.model.Board
import dgs.software.classicchess.model.Coordinate
import dgs.software.classicchess.model.actions.RevertableAction

private const val TAG = "Move"

abstract class RevertableMove(
    open val fromPos: Coordinate,
    open val toPos: Coordinate,
    open val board: Board
) {
    public val actions = mutableListOf<RevertableAction>()
    var isExecuted = false

    open fun execute() {
        if (isExecuted) {
            Log.e(TAG, "Move execute() called despite already executed")
        }
        isExecuted = true

        for (i in 0 until actions.size) {
            actions[i].execute()
        }
    }

    open fun rollback() {
        if (!isExecuted) {
            Log.e(TAG, "Move rollback() called despite not executed")
        }
        isExecuted = false

        for (i in actions.size-1 downTo 0) {
            actions[i].rollback()
        }
    }
}