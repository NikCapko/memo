package com.nik.capko.memo.data

data class Game(
    val type: GameType,
    val name: String,
) {
    companion object {
        fun getDefaultList(): List<Game> {
            return listOf(
                Game(GameType.SELECT_TRANSLATE, "Выбери перевод"),
                Game(GameType.FIND_PAIRS, "Найди пару"),
                Game(GameType.PHRASES, "Словосочетания"),
            )
        }
    }
}

enum class GameType {
    SELECT_TRANSLATE,
    FIND_PAIRS,
    PHRASES,
}
