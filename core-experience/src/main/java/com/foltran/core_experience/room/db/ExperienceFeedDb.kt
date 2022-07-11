package com.foltran.core_experience.room.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.foltran.core_experience.room.key.ExperienceFeedRemoteKey
import com.foltran.core_experience.shared.data.model.ExperienceItem


@Database(
    entities = [ExperienceItem::class, ExperienceFeedRemoteKey::class],
    version = 1,
    exportSchema = false
)
abstract class ExperienceFeedDb : RoomDatabase() {
    companion object {
        fun create(context: Context, useInMemory: Boolean = true): ExperienceFeedDb {
            val databaseBuilder = if (useInMemory) {
                Room.inMemoryDatabaseBuilder(context, ExperienceFeedDb::class.java)
            } else {
                Room.databaseBuilder(context, ExperienceFeedDb::class.java, "experience.db")
            }
            return databaseBuilder
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun experiences(): ExperienceFeedDao
    abstract fun remoteKeys(): ExperienceFeedRemoteKeyDao
}