package com.rafiadly.hackernewsapp.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rafiadly.hackernewsapp.data.model.Story
import com.rafiadly.hackernewsapp.data.model.StoryID
import com.rafiadly.hackernewsapp.data.model.StoryList
import kotlinx.coroutines.flow.Flow

@Dao
interface StoryDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertStory(stories: List<Story>)
//
//    @Query("SELECT * FROM story")
//    fun getAllStories(): Flow<List<Story>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStoryDetail(story: Story)

    @Query("SELECT * FROM story")
    fun getStoryDetail(): Flow<List<Story>>

//    @Query("SELECT * FROM story WHERE id = :id")
//    fun getStoryDetail(id: Int): Flow<Story>

//    @Query("SELECT * FROM story ORDER BY ID DESC")
//    fun getAllStories(): List<Story>?
}