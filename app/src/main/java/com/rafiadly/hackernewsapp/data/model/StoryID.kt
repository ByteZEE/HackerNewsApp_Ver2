package com.rafiadly.hackernewsapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "story_id")
data class StoryID(
    @PrimaryKey()
    @ColumnInfo(name = "id")
    var id: Int = 0
)
