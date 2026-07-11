package com.akira.miokotoba

import android.app.Application

class MioKotobaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppContainer.initialize(applicationContext)
    }
}
