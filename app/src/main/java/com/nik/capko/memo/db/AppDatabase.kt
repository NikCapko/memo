package com.nik.capko.memo.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nik.capko.memo.db.data.FormDBEntity
import com.nik.capko.memo.db.data.WordDBEntity

@Database(entities = [WordDBEntity::class, FormDBEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
    abstract fun formDao(): FormDao
}
