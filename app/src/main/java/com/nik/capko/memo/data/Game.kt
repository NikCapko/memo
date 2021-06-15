package com.nik.capko.memo.data

data class Game(
    val type: Type,
    val name: String,
) {
    companion object {

        const val MAX_WORDS_COUNT_SELECT_TRANSLATE = 5
        const val MAX_WORDS_COUNT_FIND_PAIRS = 5

        fun getDefaultList(): List<Game> {
            return listOf(
                Game(Type.SELECT_TRANSLATE, "Выбери перевод"),
                Game(Type.FIND_PAIRS, "Найди пару"),
                Game(Type.PHRASES, "Словосочетания"),
            )
        }
    }

    enum class Type {
        SELECT_TRANSLATE,
        FIND_PAIRS,
        PHRASES,
    }
}
