package com.nik.capko.memo.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nik.capko.memo.db.data.FormDBEntity

@Dao
interface FormDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(word: FormDBEntity)

    @Query("DELETE FROM forms")
    suspend fun removeAll()
}
