package com.nikcapko.memo.data.db.base

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

internal interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg obj: T)

    @Update
    suspend fun update(vararg obj: T)

    @Delete
    suspend fun delete(vararg obj: T)
}
