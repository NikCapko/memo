package com.nikcapko.memo.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.nikcapko.memo.data.db.base.BaseDao
import com.nikcapko.memo.data.db.data.FormDBEntity

@Dao
interface FormDao : BaseDao<FormDBEntity> {
    @Transaction
    @Query("DELETE FROM forms")
    suspend fun removeAll()
}
