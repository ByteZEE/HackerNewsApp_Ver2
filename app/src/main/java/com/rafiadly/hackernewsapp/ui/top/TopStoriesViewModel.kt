package com.rafiadly.hackernewsapp.ui.top

import android.util.Log
import androidx.lifecycle.*
import com.rafiadly.hackernewsapp.data.model.Story
import com.rafiadly.hackernewsapp.data.repository.StoryRepository
import com.rafiadly.hackernewsapp.utils.Resource
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

class TopStoriesViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    private var _topStoryIds: MutableLiveData<List<Int>> = MutableLiveData()
    val topStoryIds: LiveData<List<Int>> = _topStoryIds

    private var _topStories: MutableLiveData<List<Story>> = MutableLiveData()
    val topStories: LiveData<List<Story>> = _topStories

    private var _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private var _favStory = MutableLiveData<Story>()
    val favStory: LiveData<Story> = _favStory

    private val listStories = arrayListOf<Story>()

    init {
        getTopStoryIds()
        getFavStory()
    }

    private fun getTopStoryIds() {
        viewModelScope.launch {
            _loading.postValue(true)
//            for (i in 1..3) {
//                storyRepository.getStories().collect{it ->
//                    Log.d("Checkpoint", "1")
//                }
//            }
//            storyRepository.getStories().collect{ it ->
//                _topStoryIds.value = if (it is Resource.Success) it.data?.map { it.id } else emptyList()
//                if (it is Resource.Success) {
//                    Log.d("Jumlah ID : ", _topStoryIds.value?.size.toString())
//                }
//                loadStories()
//            }

            storyRepository.getTopStoryIds().collect {
                _topStoryIds.value = if (it is Resource.Success) it.data else emptyList()
            }
        }
    }

    /**
     * Ini ngga jalan kodenya
     */
    fun loadStories() {
        viewModelScope.launch {
            if (_topStoryIds.value!!.isNotEmpty()) {
                _loading.postValue(true)

//                Log.d("Size : ", "${_topStoryIds.value!!.take(20).size}")
                _topStoryIds.value!!.take(20).forEach {
                    Log.d("Database : ", it.toString())
//                    Log.d("Check :" , "$it")
                    storyRepository.getDetailWithNRB(it).collect { story ->
                        if (story is Resource.Success) {
//                            Log.d("Checkpoint", "1")
//                            Log.d("Take : ", "${story.data?.id}")
//                            listStories.add(Story(
//                                id = if (story.data != null) story.data.id else 0,
//                                title = (if (story.data != null) story.data.title else "").toString(),
//                                score = (if (story.data != null) story.data.score else 0)
//                            ))
                            story.data?.forEach {
                                listStories.add(
                                    Story(
                                        id = it.id,
                                        title = (it.title),
                                        score = (it.score)
                                    )
                                )
                            }
                            _topStories.postValue(listStories)
                            _loading.postValue(false)
                        }
                    }
                }

            }
        }
    }

    fun cacheDetailStory(id: Int) {
        viewModelScope.launch {
            storyRepository.getDetailWithNRB(id).collect { resource ->
                when (resource) {
                    is Resource.Error -> _loading.postValue(false)
                    is Resource.Loading -> _loading.postValue(true)
                    is Resource.Success -> {
                        val story = resource.data
                        _topStories.postValue(story!!)
                        _loading.postValue(false)
                    }
                }
            }
        }
    }

    fun getFavStory() {
        _favStory.postValue(storyRepository.getFavourite())
    }
}