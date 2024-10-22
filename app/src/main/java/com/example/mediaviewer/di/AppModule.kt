package com.example.mediaviewer.di

import android.app.Application
import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import com.example.mediaviewer.app.MediaApp
import com.example.repository.VideoRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: MediaApp) {

    @Provides
    @Singleton
    fun provideContentResolver(): ContentResolver {
        return application.contentResolver
    }

}