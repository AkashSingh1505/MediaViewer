package com.example.mediaviewer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mediaviewer.adapters.VideoAdapter
import com.example.mediaviewer.app.MediaApp
import com.example.mediaviewer.databinding.ActivityMainBinding
import com.example.mediaviewer.factory.GenericVMFactory
import com.example.mediaviewer.ui.VideoViewModel
import com.example.repository.VideoRepository
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var videoAdapter: VideoAdapter
    private lateinit var videoViewModel: VideoViewModel

    @Inject
    lateinit var genericVMFactory: GenericVMFactory

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.READ_MEDIA_VIDEO] == true -> {
                loadVideos()
            }
            permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true -> {
                loadVideos()
            }
            // Handle permission denial
            else -> {
                showPermissionDeniedMessage()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel
        (application as MediaApp).appComponent.inject(this)
        videoViewModel = ViewModelProvider(this, genericVMFactory).get(VideoViewModel::class.java)

        // Check and request necessary permissions
        checkAndRequestPermissions()
        loadVideos()
    }

    private fun loadVideos() {
        videoViewModel.fetchVideos()
        videoViewModel.videos.observe(this, { videos ->
            if(videos.size!=0){
                binding.noMediaTV.visibility = View.GONE
                val filteredVideos = videos.filter { !it.data.contains("whatsapp") }
                videoAdapter = VideoAdapter(filteredVideos, this)
                binding.recyclerView.apply {
                    layoutManager = LinearLayoutManager(this@MainActivity)
                    adapter = videoAdapter
                }
            }else{
                binding.noMediaTV.visibility = View.VISIBLE
            }
        })
    }

    private fun checkAndRequestPermissions() {
        when {
            // For Android 13+ (API 33), request READ_MEDIA_VIDEO
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_MEDIA_VIDEO
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    loadVideos()
                } else {
                    requestPermissionLauncher.launch(
                        arrayOf(Manifest.permission.READ_MEDIA_VIDEO)
                    )
                }
            }
            // For Android 6-12 (API 23-32), request READ_EXTERNAL_STORAGE
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    loadVideos()
                } else {
                    requestPermissionLauncher.launch(
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    )
                }
            }
            // For older devices (below Android 6), permissions are granted at install time
            else -> {
                loadVideos()
            }
        }
    }

    // Handle cases where the permission is denied
    private fun showPermissionDeniedMessage() {
        Snackbar.make(
            binding.root,
            "Permission is required to load videos. Please enable it in settings.",
            Snackbar.LENGTH_LONG
        ).setAction("Settings") {
            openAppSettings()
        }.show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
    }
}
