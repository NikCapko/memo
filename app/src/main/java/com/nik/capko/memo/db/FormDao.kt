package com.nik.capko.memo.db

import androidx.room.Dao
import androidx.room.Query
import com.nik.capko.memo.base.db.BaseDao
import com.nik.capko.memo.db.data.FormDBEntity

@Dao
interface FormDao : BaseDao<FormDBEntity> {
    @Query("DELETE FROM forms")
    suspend fun removeAll()
}
