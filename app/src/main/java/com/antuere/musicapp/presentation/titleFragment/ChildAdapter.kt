package com.antuere.musicapp.presentation.titleFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.antuere.domain.usecase.IncreaseProportionUseCase
import com.antuere.musicapp.util.MyPlayer
import com.antuere.musicapp.databinding.DayItemBinding
import com.antuere.domain.usecase.ReduceProportionUseCase
import com.antuere.musicapp.util.PlaylistItem


/*Adapter for NESTED recycle view, for show
* one playlist with time start, time end and proportion */

class ChildAdapter(private val myPlayer: MyPlayer) :
    ListAdapter<PlaylistItem, ChildAdapter.ChildViewHolder>(ChildDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        return ChildViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, myPlayer)
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

        fun bind(item: PlaylistItem, myPlayer: MyPlayer) {
            with(binding) {

                val timeTextString = "${item.from} - ${item.to}"
                timeText.text = timeTextString

                val playlist = item.playlist
                playListName.text = playlist.name

//               Song integrity check: if current MD5 not match with required - show errorView
                if (item.showError) errorView.visibility = View.VISIBLE

                myPlayer.playlistsRequired.forEach {
                    if (it.playlistId == item.playlist.id) {
                        val prop = it.proportion
                        proportion.text = it.proportion.toString()
                        buttonLeft.isClickable = prop != 1
                    }
                }

                buttonLeft.setOnClickListener {
                    val reduceProportionUseCase =
                        ReduceProportionUseCase(myPlayer.playlistsRequired)
                    val newProp = reduceProportionUseCase.invoke(item.playlist.id)
                    if (newProp < 1) return@setOnClickListener
                    proportion.text = newProp.toString()

                    myPlayer.updateSongs()
                }

                buttonRight.setOnClickListener {
                    val increaseProportionUseCase =
                        IncreaseProportionUseCase(myPlayer.playlistsRequired)
                    val newProp = increaseProportionUseCase.invoke(item.playlist.id)
                    if (newProp < 1) return@setOnClickListener
                    proportion.text = newProp.toString()

                    myPlayer.updateSongs()

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