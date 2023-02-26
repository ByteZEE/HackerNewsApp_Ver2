package com.rafiadly.hackernewsapp.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rafiadly.hackernewsapp.data.model.StoryID
import com.rafiadly.hackernewsapp.data.model.StoryList
import kotlinx.coroutines.flow.Flow

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStory(stories: List<StoryID>)

    @Query("SELECT * FROM story_id")
    fun getAllStories(): Flow<List<StoryID>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStoryDetail(story: StoryList)

    @Query("SELECT * FROM story_list WHERE id = :id")
    fun getStoryDetail(id: Int): Flow<StoryList>
}