package com.example.musicapp.screens.titleFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.checkMD5
import com.example.musicapp.databinding.DayItemBinding
import com.example.musicapp.foldersPaths
import com.example.musicapp.getPlaylist
import com.example.musicapp.network.musicProfile.MusicProfile
import com.example.musicapp.network.musicProfile.TimeZone
import timber.log.Timber

class ChildAdapter(private val profile: MusicProfile) :
    ListAdapter<TimeZone, ChildAdapter.ChildViewHolder>(ChildDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        return ChildViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, profile)
    }


    class ChildViewHolder private constructor(private val binding: DayItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(viewGroup: ViewGroup): ChildViewHolder {
                val inflater = LayoutInflater.from(viewGroup.context)
                val binding = DayItemBinding.inflate(inflater, viewGroup, false)
                return ChildViewHolder(binding)
            }
        }

        fun bind(item: TimeZone, profile: MusicProfile) {
            with(binding) {
                val timeTextString = "${item.from} - ${item.to}"

                timeText.text = timeTextString
                val playlist = item.playlistsOfZone.first().getPlaylist(profile.schedule.playlists)
                playListName.text = playlist.name

                playlist.songs.forEach {
                    val check = it.checkMD5(foldersPaths[playlist.name] + "/${it.name}")
                    if (!check) {
                        binding.errorView.visibility = View.VISIBLE
                    }
                }

                buttonLeft.setOnClickListener {
                    var current = proportion.text.toString().toInt()
                    current--
                    if (current < 1) {
                        proportion.text = "1"
                    } else proportion.text = current.toString()

                }

                buttonRight.setOnClickListener {
                    var current = proportion.text.toString().toInt()
                    current++
                    proportion.text = current.toString()
                }

            }
        }
    }


    class ChildDiffUtil : DiffUtil.ItemCallback<TimeZone>() {

        override fun areItemsTheSame(oldItem: TimeZone, newItem: TimeZone): Boolean {
            return ((oldItem.from + oldItem.to)
                    == (newItem.from + newItem.to))
        }

        override fun areContentsTheSame(oldItem: TimeZone, newItem: TimeZone): Boolean {
            return oldItem == newItem
        }
    }
}