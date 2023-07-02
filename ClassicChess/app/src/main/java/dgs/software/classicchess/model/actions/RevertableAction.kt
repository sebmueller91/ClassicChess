package dgs.software.classicchess.model.actions

import android.util.Log
import dgs.software.classicchess.model.MutableGame

private const val TAG = "RevertableAction"

abstract class RevertableAction {
    var isExecuted = false

    open fun execute(mutableGame: MutableGame) {
        if (isExecuted) {
            Log.e(TAG, "RevertableAction execute() called despite already executed")
        }
        isExecuted = true
    }

    open fun rollback(mutableGame: MutableGame) {
        if (!isExecuted) {
            Log.e(TAG, "RevertableAction rollback() called despite not executed")
        }
        isExecuted = false
    }
}