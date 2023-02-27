package com.rafiadly.hackernewsapp.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "story")
@Parcelize
data class Story(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id:Int,
    @ColumnInfo(name = "story_id")
    val storyID: Int? = 0,
    @ColumnInfo(name = "title")
    val title:String,
    @ColumnInfo(name = "comments")
    val comments:Int = 0,
    @ColumnInfo(name = "score")
    val score:Int = 0,
    @ColumnInfo(name = "date")
    val date:String = "",
    @ColumnInfo(name = "author")
    val author:String = "",
    @ColumnInfo(name = "desc")
    val desc:String = "No Description",
//    val commentIds:List<Int> = emptyList()
): Parcelable