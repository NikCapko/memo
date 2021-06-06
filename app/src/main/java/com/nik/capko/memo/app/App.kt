@file:Suppress("ClassOrdering")

package com.nik.capko.memo.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
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
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        instance = this
        prefs = AppStorage(applicationContext)
    }
}

val appStorage: AppStorage by lazy {
    App.prefs!!
}
