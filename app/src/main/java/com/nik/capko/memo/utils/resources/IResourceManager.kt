package com.nik.capko.memo.utils.resources

import android.graphics.drawable.Drawable

interface IResourceManager {
    fun getString(resId: Int): String
    fun getColor(resId: Int): Int
    fun getDrawable(resId: Int): Drawable?
}
