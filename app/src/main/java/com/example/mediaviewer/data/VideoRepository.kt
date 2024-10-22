package com.example.repository

import android.content.ContentResolver
import android.provider.MediaStore
import com.example.mediaviewer.data.Video
import javax.inject.Inject

class VideoRepository @Inject constructor(
    private val contentResolver: ContentResolver
) {
    fun getAllVideos(): List<Video> {
        val videoList = mutableListOf<Video>()
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.DATA
        )

        val cursor = contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val displayNameColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val durationColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)

            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val displayName = it.getString(displayNameColumn)
                val duration = it.getLong(durationColumn)
                val data = it.getString(dataColumn)

                videoList.add(Video(id, displayName, duration, data))
            }
        }

        return videoList
    }
}
