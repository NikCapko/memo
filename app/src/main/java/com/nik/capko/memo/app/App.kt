@file:Suppress("ClassOrdering")

package com.nik.capko.memo.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.nik.capko.memo.utils.AppStorage
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    companion object {

        private var _instance: App? = null
        val instance get() = checkNotNull(_instance) { "App isn`t initialized" }

        private var _prefs: AppStorage? = null
        val prefs get() = checkNotNull(_prefs) { "AppStorage isn`t initialized" }
    }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        _instance = this
        _prefs = AppStorage(applicationContext)
    }
}

val appStorage: AppStorage by lazy {
    App.prefs
}
