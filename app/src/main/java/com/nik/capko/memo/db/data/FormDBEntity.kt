package com.nik.capko.memo.db.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "forms",
    primaryKeys = ["key", "wordId"],
    foreignKeys = [
        ForeignKey(
            entity = WordDBEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("wordId")
        )
    ]
)

data class FormDBEntity(
    @ColumnInfo(name = "key") var key: String,
    @ColumnInfo(name = "name") var name: String? = null,
    @ColumnInfo(name = "value") var value: String? = null,
    @ColumnInfo(name = "wordId") var wordId: Long,
)
