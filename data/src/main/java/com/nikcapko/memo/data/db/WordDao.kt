package com.nikcapko.memo.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.nikcapko.memo.data.db.base.BaseDao
import com.nikcapko.memo.data.db.data.WordDBEntity
import com.nikcapko.memo.data.db.data.WordFormDBEntity

@Dao
interface WordDao : BaseDao<WordDBEntity> {
    @Transaction
    @Query("SELECT * FROM words order by frequency asc")
    suspend fun getAllWords(): List<WordFormDBEntity>

    @Transaction
    @Query("SELECT * FROM words WHERE primaryLanguage == 1 order by frequency asc")
    suspend fun getWordsForGame(): List<WordFormDBEntity>

    @Transaction
    @Query("SELECT * FROM words WHERE primaryLanguage == 1 order by frequency asc LIMIT :limit")
    suspend fun getWordsForGameByLimit(limit: Int): List<WordFormDBEntity>

    @Transaction
    @Query("DELETE FROM words")
    suspend fun removeAll()

    @Transaction
    @Query("DELETE FROM words WHERE id == :id")
    suspend fun deleteWordById(id: Long)
}
