package hu.toliver.vinotes

import android.app.Activity
import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ViNotesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}