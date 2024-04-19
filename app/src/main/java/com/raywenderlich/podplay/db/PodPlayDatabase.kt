package com.raywenderlich.podplay.db

import android.content.Context
import androidx.room.*
import com.raywenderlich.podplay.model.Episode
import com.raywenderlich.podplay.model.Podcast
import kotlinx.coroutines.CoroutineScope
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return (date?.time)
    }
}

// 1
@Database(entities = [Podcast::class, Episode::class], version = 1)
@TypeConverters(Converters::class)
abstract class PodPlayDatabase : RoomDatabase() {
    // 2
    abstract fun podcastDao(): PodcastDao

    // 3
    companion object {

        // 4
        @Volatile
        private var INSTANCE: PodPlayDatabase? = null

        // 5
        fun getInstance(context: Context, coroutineScope: CoroutineScope): PodPlayDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            // 6
            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    PodPlayDatabase::class.java,
                    "PodPlayer")
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                // 7
                return instance
            }
        }
    }
}

