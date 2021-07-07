package com.nik.capko.memo.utils.resources

import android.content.Context
import android.graphics.drawable.Drawable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FieldConverter @Inject constructor(val context: Context) : IResourceManager {

    override fun getString(resId: Int): String {
        return ResourceManager.getString(context, resId)
    }

    override fun getColor(resId: Int): Int {
        return ResourceManager.getColor(context, resId)
    }

    override fun getDrawable(resId: Int): Drawable? {
        return ResourceManager.getDrawable(context, resId)
    }
}
