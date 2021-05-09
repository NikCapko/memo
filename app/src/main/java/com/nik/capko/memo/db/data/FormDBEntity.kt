package com.nik.capko.memo.db.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.nik.capko.memo.data.Form

@Entity(
    tableName = "forms",
    foreignKeys = [
        ForeignKey(
            entity = WordDBEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("wordId")
        )
    ]
)
data class FormDBEntity(
    @PrimaryKey
    var key: String,
    var name: String? = null,
    var value: String? = null,
    var wordId: Long,
) {
    companion object {
        fun FormDBEntity.toFormModel(): Form {
            return Form(
                key,
                name,
                value
            )
        }
    }
}
