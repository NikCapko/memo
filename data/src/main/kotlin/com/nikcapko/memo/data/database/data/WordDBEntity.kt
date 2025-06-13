package com.nikcapko.memo.data.database.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
internal data class WordDBEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") var id: Long,
    @ColumnInfo(name = "word") var word: String? = null,
    @ColumnInfo(name = "translation") var translation: String? = null,
    @ColumnInfo(name = "frequency") var frequency: Float? = null,
)
