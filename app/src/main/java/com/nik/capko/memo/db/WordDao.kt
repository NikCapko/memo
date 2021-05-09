package com.nik.capko.memo.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.nik.capko.memo.db.data.WordDBEntity
import com.nik.capko.memo.db.data.WordFormDBEntity

@Dao
interface WordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(word: WordDBEntity)

    @Transaction
    @Query("SELECT * FROM words")
    suspend fun getAllWords(): List<WordFormDBEntity>
}
