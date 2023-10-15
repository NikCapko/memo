package com.nikcapko.memo.utils.resources

import android.graphics.drawable.Drawable

interface ResourceManager {
    fun getString(resId: Int): String
    fun getColor(resId: Int): Int
    fun getDrawable(resId: Int): Drawable?
}
