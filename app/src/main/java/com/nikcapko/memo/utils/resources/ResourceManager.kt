package com.nikcapko.memo.utils.resources

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat

object ResourceManager {

    fun getString(context: Context, resId: Int): String {
        return context.getString(resId)
    }

    fun getColor(context: Context, resId: Int): Int {
        return ContextCompat.getColor(context, resId)
    }

    fun getDrawable(context: Context, resId: Int): Drawable? {
        return ContextCompat.getDrawable(context, resId)
    }
}
