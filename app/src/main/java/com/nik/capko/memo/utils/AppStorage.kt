package com.nik.capko.memo.utils

import android.content.Context
import android.content.SharedPreferences
import com.nik.capko.memo.utils.extensions.get
import com.nik.capko.memo.utils.extensions.put

class AppStorage(context: Context) {

    @Suppress("ClassOrdering")
    companion object {
        private const val APP_PREFERENCES = "MEMO_APP_PREFERENCES"
    }

    val preferences: SharedPreferences =
        context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

    inline fun <reified T> get(key: String, defaultValue: T): T {
        return preferences.get(key, defaultValue)
    }

    inline fun <reified T> put(key: String, value: T) {
        preferences.put(key, value)
    }
}
