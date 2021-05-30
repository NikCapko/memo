package com.nik.capko.memo.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.nik.capko.memo.base.db.BaseDao
import com.nik.capko.memo.db.data.WordDBEntity
import com.nik.capko.memo.db.data.WordFormDBEntity

@Dao
interface WordDao : BaseDao<WordDBEntity> {
    @Transaction
    @Query("SELECT * FROM words")
    suspend fun getAllWords(): List<WordFormDBEntity>

    @Query("DELETE FROM words")
    suspend fun removeAll()

    @Query("DELETE FROM words WHERE id == :id")
    suspend fun deleteWordById(id: Long)
}
