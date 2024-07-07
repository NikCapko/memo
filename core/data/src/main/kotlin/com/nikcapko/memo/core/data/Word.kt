package com.nikcapko.memo.core.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Word(
    var id: Long,
    var word: String,
    var translation: String,
    var frequency: Float,
) : Parcelable
