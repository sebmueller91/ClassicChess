package dgs.software.classicchess.model.moves.actions

import android.util.Log

private const val TAG = "RevertableAction"

abstract class RevertableAction {
    private var isExecuted = false
    open fun execute() {
        if (isExecuted) {
            Log.e(TAG, "RevertableAction execute() called despite already executed")
        }
        isExecuted = true
    }
    open fun rollback() {
        if (!isExecuted) {
            Log.e(TAG, "RevertableAction rollback() called despite not executed")
        }
        isExecuted = false
    }
}