package com.example.mediaviewer.di

import androidx.lifecycle.ViewModel
import com.example.mediaviewer.ui.VideoViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @ClassKey(VideoViewModel::class)
    @IntoMap
    abstract fun bindVideoViewModel(videoViewModel: VideoViewModel): ViewModel
}