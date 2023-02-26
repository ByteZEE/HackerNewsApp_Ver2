package com.rafiadly.hackernewsapp.data.repository

import androidx.room.withTransaction
import com.rafiadly.hackernewsapp.data.model.Comment
import com.rafiadly.hackernewsapp.data.model.Story
import com.rafiadly.hackernewsapp.data.model.StoryID
import com.rafiadly.hackernewsapp.data.model.StoryList
import com.rafiadly.hackernewsapp.data.source.local.preferences.Preferences
import com.rafiadly.hackernewsapp.data.source.local.room.StoryDatabase
import com.rafiadly.hackernewsapp.data.source.remote.network.ApiService
import com.rafiadly.hackernewsapp.data.source.remote.response.toComment
import com.rafiadly.hackernewsapp.data.source.remote.response.toStory
import com.rafiadly.hackernewsapp.utils.Resource
import com.rafiadly.hackernewsapp.data.networkBoundResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException

class StoryRepository private constructor(
    private val api: ApiService,
    private val pref: Preferences,
    private val db: StoryDatabase
) {
    private val storyDao = db.storyDao()

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            api: ApiService,
            pref: Preferences,
            db: StoryDatabase
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(api, pref, db).apply { instance = this }
            }
    }

    fun getTopStoryIds(): Flow<Resource<List<Int>>> =
        flow {
            emit(Resource.Loading())
            try {
                val res = api.getTopStoryIds().map { it }
                emit(Resource.Success(res))
            } catch (e: HttpException) {
                emit(Resource.Error(e.message() ?: "Unexpected error occured"))
            } catch (e: IOException) {
                emit(Resource.Error(e.message ?: "Unexpected error occured"))
            }
        }.flowOn(Dispatchers.IO)

    fun getStoryDetail(storyId: Int): Flow<Resource<Story>> =
        flow {
            try {
                val res = api.getDetailStory(storyId).toStory()
                emit(Resource.Success(res))
            } catch (e: HttpException) {
                emit(Resource.Error(e.message() ?: "Unexpected error occured"))
            } catch (e: IOException) {
                emit(Resource.Error(e.message ?: "Unexpected error occured"))
            }
        }.flowOn(Dispatchers.IO)

    fun getComment(commentId: Int): Flow<Resource<Comment>> =
        flow {
            try {
                val res = api.getComment(commentId)
                if (res.deleted == null) {
                    emit(Resource.Success(res.toComment()))
                }
            } catch (e: HttpException) {
                emit(Resource.Error(e.message() ?: "Unexpected error occured"))
            } catch (e: IOException) {
                emit(Resource.Error(e.message ?: "Unexpected error occured"))
            }
        }.flowOn(Dispatchers.IO)

    fun setFavourite(story: Story) {
        pref.setStory(story)
    }

    fun getFavourite(): Story = pref.getStory()

    fun getStories() = networkBoundResource(
        query = {
            storyDao.getAllStories()
        },
        fetch = {
            api.getTopStoryIds()
        },
        saveFetchResult = { stories ->
            db.withTransaction {
                storyDao.insertStory(stories.map { StoryID(id = it) })
                storyDao.getAllStories()
            }
        }
    )


    fun getStoryDetailsOffline(id: Int) = networkBoundResource(
        query = {
            storyDao.getStoryDetail(id)
        },
        shouldFetch = { cachedData ->
            cachedData == null
        },
        fetch = {
            api.getDetailStory(id)
        },
        saveFetchResult = { detailStories ->
            db.withTransaction {
                storyDao.insertStoryDetail(
                    StoryList(
                        id = detailStories.id,
                        title = detailStories.title,
                        score = detailStories.score
                    )
                )
                storyDao.getStoryDetail(id)
            }
        }
    )

//    fun getStoryDetailsOffline(ids: List<Int>) = networkBoundResource(
//        query = {
//            storyDao.getStoryDetails(ids)
//        },
//        shouldFetch = { cachedData ->
//            cachedData.isEmpty()
//        },
//        fetch = {
//            api.getDetailStories(ids)
//        },
//        saveFetchResult = { detailStories ->
//            db.withTransaction {
//                storyDao.insertStoryDetails(detailStories)
//            }
//        }
//    )


//    fun getStoryDetailsOffline(ids: List<Int>) = ids.map { id ->
//        networkBoundResource(
//            query = {
//                storyDao.getStoryDetail(id)
//            },
//            shouldFetch = { cachedData ->
//                cachedData == null
//            },
//            fetch = {
//                api.getDetailStory(id)
//            },
//            saveFetchResult = { detailStories ->
//                db.withTransaction {
//                    storyDao.insertStoryDetail(
//                        StoryList(
//                            id = detailStories.id,
//                            title = detailStories.title,
//                            score = detailStories.score
//                        )
//                    )
//                }
//            }
//        )
//    }



}