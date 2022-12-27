package dgs.software.classicchess

import android.app.Application
import dgs.software.classicchess.data.AppContainer
import dgs.software.classicchess.data.DefaultAppContainer

class ClassicChessApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}