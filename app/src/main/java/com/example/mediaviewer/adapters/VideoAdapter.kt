package com.example.mediaviewer.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.MediaController
import androidx.recyclerview.widget.RecyclerView
import com.example.mediaviewer.data.Video
import com.example.mediaviewer.databinding.ItemVideoBinding

class VideoAdapter(private val videoList: List<Video>, private val context: Context) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemVideoBinding.inflate(inflater, parent, false)
        return VideoViewHolder(binding, context)
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    override fun onViewRecycled(holder: VideoViewHolder) {
        super.onViewRecycled(holder)
        holder.releaseMediaController()
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videoList[position]
        holder.bind(video)
    }

    class VideoViewHolder(private val binding: ItemVideoBinding, private val context: Context) : RecyclerView.ViewHolder(binding.root) {
        private var mediaController: MediaController? = null

        fun releaseMediaController() {
            mediaController?.hide()
            binding.videoView.stopPlayback()
            mediaController = null
        }

        // Bind video data to the UI
        fun bind(video: Video) {
            if (mediaController == null) {
                mediaController = MediaController(context).apply {
                    setAnchorView(binding.videoView)
                    binding.videoView.setMediaController(this)
                }
            }

            binding.videoView.setVideoURI(Uri.parse(video.data))
            binding.videoName.text = video.name
            binding.videoDuration.text = formatDuration(video.duration)

        }

        // Format duration to mm:ss
        private fun formatDuration(duration: Long): String {
            val minutes = duration / 1000 / 60
            val seconds = (duration / 1000) % 60
            return String.format("%02d:%02d", minutes, seconds)
        }
    }
}
