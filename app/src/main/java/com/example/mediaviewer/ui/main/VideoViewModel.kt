package com.example.mediaviewer.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediaviewer.app.MediaApp
import com.example.mediaviewer.data.Video
import com.example.repository.VideoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class VideoViewModel @Inject constructor(val repository: VideoRepository) : ViewModel()  {

    private val _videos = MutableLiveData<List<Video>>()
    val videos: LiveData<List<Video>> get() = _videos

    fun fetchVideos() {
        viewModelScope.launch(Dispatchers.IO) {
            val videoList = repository.getAllVideos()
            _videos.postValue(videoList)
        }
    }
}