package dgs.software.classicchess.logger

import android.util.Log

interface Logger {
    fun d(tag: String, msg: String)
    fun e(tag: String, msg: String)
}

object AndroidLogger : Logger {
    override fun d(tag: String, msg: String) {
        Log.d(tag, msg)
    }

    override fun e(tag: String, msg: String) {
        Log.e(tag, msg)
    }
}

object TestLogger : Logger {
    override fun d(tag: String, msg: String) {
        // Do nothing or print to console
    }

    override fun e(tag: String, msg: String) {
        // Do nothing or print to console
    }
}
