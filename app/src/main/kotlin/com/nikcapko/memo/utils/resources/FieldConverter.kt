package com.nikcapko.memo.utils.resources

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FieldConverter @Inject constructor(@ApplicationContext var context: Context) : ResourceManager {

    override fun getString(resId: Int): String {
        return context.getString(resId)
    }

    override fun getColor(resId: Int): Int {
        return ContextCompat.getColor(context, resId)
    }

    override fun getDrawable(resId: Int): Drawable? {
        return ContextCompat.getDrawable(context, resId)
    }
}
