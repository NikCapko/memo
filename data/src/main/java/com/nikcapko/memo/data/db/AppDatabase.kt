package com.nikcapko.memo.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nikcapko.memo.data.db.data.FormDBEntity
import com.nikcapko.memo.data.db.data.WordDBEntity

@Database(entities = [WordDBEntity::class, FormDBEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
    abstract fun formDao(): FormDao
}
