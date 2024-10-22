package com.example.mediaviewer.di

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.mediaviewer.MainActivity
import com.example.mediaviewer.app.MediaApp

import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton




@Singleton
@Component(modules = [AppModule::class,ViewModelModule::class])
interface ApplicationComponent {

    fun inject(activity: MainActivity)
    fun getMap():Map<Class<*>,ViewModel>
}

