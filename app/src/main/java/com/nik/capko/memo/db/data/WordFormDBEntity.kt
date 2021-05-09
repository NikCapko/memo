package com.nik.capko.memo.db.data

import androidx.room.Embedded
import androidx.room.Relation
import com.nik.capko.memo.data.Word
import com.nik.capko.memo.db.data.FormDBEntity.Companion.toFormModel

data class WordFormDBEntity(
    @Embedded
    val word: WordDBEntity? = null,

    @Relation(parentColumn = "id", entityColumn = "wordId")
    val forms: List<FormDBEntity> = listOf(),
) {
    companion object {
        fun WordFormDBEntity.toWordModel(): Word {
            return Word(
                word?.id ?: 0,
                word?.word,
                word?.type,
                word?.translation,
                word?.frequency,
                forms.map { it.toFormModel() }
            )
        }
    }
}
