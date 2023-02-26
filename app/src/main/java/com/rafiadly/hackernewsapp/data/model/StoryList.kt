package com.rafiadly.hackernewsapp.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "story_list")
@Parcelize
data class StoryList(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "title")
    var title: String? = null,

    @ColumnInfo(name = "score")
    var score: Int = 0,

//    @ColumnInfo(name = "comments")
//    var commentIds: List<Int> = emptyList()
): Parcelable
