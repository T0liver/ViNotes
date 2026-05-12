package hu.toliver.winotes

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WiNoteApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}