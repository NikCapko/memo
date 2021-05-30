package com.nik.capko.memo.db.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class WordDBEntity(
    @PrimaryKey
    var id: Long,
    var word: String? = null,
    var type: String? = null,
    var gender: String? = null,
    var translation: String? = null,
    var frequency: Float? = null,
    var primaryLanguage: Boolean = false,
)
