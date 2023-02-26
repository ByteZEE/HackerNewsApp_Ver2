package com.rafiadly.hackernewsapp.data.source.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rafiadly.hackernewsapp.data.model.StoryID
import com.rafiadly.hackernewsapp.data.model.StoryList

@Database(entities = [StoryList::class, StoryID::class], version = 1)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao() : StoryDao

    companion object {
        @Volatile
        private var INSTANCE: StoryDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): StoryDatabase {
            if (INSTANCE == null) {
                synchronized(StoryDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                    StoryDatabase::class.java, "story_database")
                        .build()
                }
            }
            return INSTANCE as StoryDatabase
        }
    }
}