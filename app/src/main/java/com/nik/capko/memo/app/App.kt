package com.nik.capko.memo.app

import android.app.Application
import com.nik.capko.memo.utils.AppStorage
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    companion object {
        var prefs: AppStorage? = null
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        prefs = AppStorage(applicationContext)
    }
}

val appStorage: AppStorage by lazy {
    App.prefs!!
}
