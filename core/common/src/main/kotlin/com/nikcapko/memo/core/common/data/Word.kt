package com.nikcapko.memo.core.common.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

internal const val WORD_GAME_PRICE = 0.02f

@Parcelize
data class Word(
    var id: Long,
    var word: String,
    var translation: String,
    var frequency: Float,
) : Parcelable
