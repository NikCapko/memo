package com.nikcapko.domain.model

data class Game(
    val type: Type,
    val description: String,
) {
    enum class Type(val value: String) {
        SELECT_TRANSLATE("Выбери перевод"),
        FIND_PAIRS("Найди пару"),
    }
}
