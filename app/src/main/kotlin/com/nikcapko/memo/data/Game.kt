package com.nikcapko.memo.data

import android.os.Parcelable
import com.nikcapko.memo.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class Game(
    val type: Type,
    val nameStringId: Int,
) : Parcelable {
    companion object {

        const val MAX_WORDS_COUNT_SELECT_TRANSLATE = 5
        const val MAX_WORDS_COUNT_FIND_PAIRS = 5

        fun getDefaultList(): List<Game> {
            return listOf(
                Game(Type.SELECT_TRANSLATE, R.string.game_type_select_translate),
                Game(Type.FIND_PAIRS, R.string.game_type_find_pairs),
            )
        }
    }

    enum class Type {
        SELECT_TRANSLATE,
        FIND_PAIRS,
    }
}
