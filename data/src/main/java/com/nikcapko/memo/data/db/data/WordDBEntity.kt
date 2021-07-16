package com.nikcapko.memo.data.db.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class WordDBEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") var id: Long,
    @ColumnInfo(name = "word") var word: String? = null,
    @ColumnInfo(name = "type") var type: String? = null,
    @ColumnInfo(name = "gender") var gender: String? = null,
    @ColumnInfo(name = "translation") var translation: String? = null,
    @ColumnInfo(name = "frequency") var frequency: Float? = null,
    @ColumnInfo(name = "primaryLanguage") var primaryLanguage: Boolean = false,
)
