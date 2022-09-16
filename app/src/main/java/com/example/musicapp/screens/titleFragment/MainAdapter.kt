package com.example.musicapp.screens.titleFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.databinding.HeaderItemBinding
import com.example.musicapp.network.musicProfile.Day
import com.example.musicapp.network.musicProfile.MusicProfile

class MainAdapter(private val profile: MusicProfile) :
    ListAdapter<Day, MainAdapter.TitleViewHolder>(MyDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleViewHolder {
        return TitleViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TitleViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, profile)
    }


    class TitleViewHolder private constructor(private val binding: HeaderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): TitleViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = HeaderItemBinding.inflate(inflater, parent, false)
                return TitleViewHolder(binding)
            }
        }

        fun bind(item: Day, profile: MusicProfile) {
            binding.header.text = item.day.replaceFirstChar { it.titlecaseChar() }
            val childAdapter = ChildAdapter(profile)
            binding.nestedRecycleView.adapter = childAdapter

            childAdapter.submitList(item.timeZones.sorted())

        }
    }


    class MyDiffCallBack : DiffUtil.ItemCallback<Day>() {


        override fun areItemsTheSame(oldItem: Day, newItem: Day): Boolean {
            return oldItem.day == newItem.day
        }

        override fun areContentsTheSame(oldItem: Day, newItem: Day): Boolean {
            return oldItem == newItem
        }
    }

}