package com.raktaseva.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RaktaSevaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Firebase initialization will happen here automatically if google-services is applied
        // or manually if needed
    }
}
