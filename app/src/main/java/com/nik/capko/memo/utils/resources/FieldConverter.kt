package com.nik.capko.memo.utils.resources

import android.graphics.drawable.Drawable
import com.nik.capko.memo.app.App

class FieldConverter : IResourceManager {

    override fun getString(resId: Int): String {
        return ResourceManager.getString(App.instance, resId)
    }

    override fun getColor(resId: Int): Int {
        return ResourceManager.getColor(App.instance, resId)
    }

    override fun getDrawable(resId: Int): Drawable? {
        return ResourceManager.getDrawable(App.instance, resId)
    }
}
