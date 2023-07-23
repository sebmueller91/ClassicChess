package dgs.software.classicchess

import android.app.Application
import dgs.software.classicchess.di.classicChessModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class ClassicChessApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidLogger()
            androidContext(this@ClassicChessApplication)
            modules(classicChessModule)
        }
    }
}