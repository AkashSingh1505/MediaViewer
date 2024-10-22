package com.example.mediaviewer.app

import android.app.Application
import android.util.Log
import com.example.mediaviewer.di.AppModule
import com.example.mediaviewer.di.ApplicationComponent
import com.example.mediaviewer.di.DaggerApplicationComponent
import javax.inject.Inject

class MediaApp: Application() {
    lateinit var appComponent: ApplicationComponent
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.builder()
            .appModule(AppModule(this)).build()
    }
}