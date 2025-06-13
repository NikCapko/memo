package com.nikcapko.memo.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.nikcapko.memo.data.database.base.BaseDao
import com.nikcapko.memo.data.database.data.WordDBEntity

@Dao
internal interface WordDao : BaseDao<WordDBEntity> {

    @Transaction
    @Query("SELECT * FROM words order by frequency asc")
    suspend fun getAllWords(): List<WordDBEntity>

    @Transaction
    @Query("SELECT * FROM words order by frequency asc LIMIT :limit")
    suspend fun getWordsForGameByLimit(limit: Int): List<WordDBEntity>

    @Transaction
    @Query("DELETE FROM words")
    suspend fun removeAll()

    @Transaction
    @Query("DELETE FROM words WHERE id == :id")
    suspend fun deleteWordById(id: String)
}
