package com.nikcapko.memo.data.db.data

import androidx.room.Embedded
import androidx.room.Relation

data class WordFormDBEntity(
    @Embedded
    val word: WordDBEntity? = null,

    @Relation(parentColumn = "id", entityColumn = "wordId")
    val forms: List<FormDBEntity> = listOf(),
)
