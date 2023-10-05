package com.nikcapko.memo.utils.resources

import android.content.Context
import android.graphics.drawable.Drawable
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FieldConverter @Inject constructor(@ApplicationContext var context: Context) : IResourceManager {

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
