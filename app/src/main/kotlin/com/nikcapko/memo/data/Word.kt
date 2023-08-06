package com.nikcapko.memo.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Word(
    var id: Long,
    var word: String,
    var translation: String,
    var frequency: Float,

) : Parcelable {
    companion object {
        const val WORD_GAME_PRICE = 0.02f
    }
}
