package com.example

import android.app.Application
import com.example.di.AppContainer

class M4chanicApp : Application() {

    lateinit var container: AppContainer
        private set

    val appContainer: AppContainer
        get() = container

    override fun onCreate() {
        super.onCreate()
        instance = this
        container = AppContainer(this)
    }

    companion object {
        lateinit var instance: M4chanicApp
            private set
    }
}
