package com.example.musicapp.screens.titleFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.MyPlayer
import com.example.musicapp.databinding.DayItemBinding
import com.example.musicapp.network.musicProfile.PlaylistItem
import com.example.musicapp.network.musicProfile.PlaylistsZone


/*Adapter for NESTED recycle view, for show
* one playlist with time start, time end and proportion */

class ChildAdapter :
    ListAdapter<PlaylistItem, ChildAdapter.ChildViewHolder>(ChildDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        return ChildViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
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

        fun bind(item: PlaylistItem) {
            with(binding) {
                val timeTextString = "${item.from} - ${item.to}"
                timeText.text = timeTextString

                val playlist = item.playlist
                playListName.text = playlist.name

//               Song integrity check: if current MD5 not match with required - show errorView
                playlist.songs.forEach {
                    val check = it.checkMD5(foldersPaths[playlist.name] + "/${it.name}")
                    if (!check) {
                        binding.errorView.visibility = View.VISIBLE
                    }
                }

                MyPlayer.playlistsRequired.forEach {
                    if (it.playlistId == item.playlist.id)
                        binding.proportion.text = it.proportion.toString()
                }

                buttonLeft.setOnClickListener {
                    MyPlayer.playlistsRequired.forEach {
                        if (it.playlistId == item.playlist.id) {
                            it.proportion--
                            if (it.proportion < 1) {
                                return@setOnClickListener
                            } else proportion.text = it.proportion.toString()
                        }
                    }
                }

                buttonRight.setOnClickListener {
                    MyPlayer.playlistsRequired.forEach {
                        if (it.playlistId == item.playlist.id) {
                            it.proportion++
                            proportion.text = it.proportion.toString()
                        }
                    }
                }

            }
        }
    }


    class ChildDiffUtil : DiffUtil.ItemCallback<PlaylistItem>() {

        override fun areItemsTheSame(oldItem: PlaylistItem, newItem: PlaylistItem): Boolean {
            return ((oldItem.from + oldItem.to)
                    == (newItem.from + newItem.to))
        }

        override fun areContentsTheSame(oldItem: PlaylistItem, newItem: PlaylistItem): Boolean {
            return oldItem == newItem
        }
    }
}