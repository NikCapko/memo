package com.nik.capko.memo.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word")
data class Word(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var word: String? = null,
    var type: Byte? = null,
    var translation: String? = null,
    var frequency: Float? = null,
)
