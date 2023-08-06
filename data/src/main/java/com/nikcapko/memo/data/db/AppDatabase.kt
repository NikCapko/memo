package com.nikcapko.memo.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nikcapko.memo.data.db.data.WordDBEntity

@Database(entities = [WordDBEntity::class], version = 1, exportSchema = false)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
}
