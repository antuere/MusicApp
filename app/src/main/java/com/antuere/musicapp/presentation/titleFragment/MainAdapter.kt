package com.antuere.musicapp.presentation.titleFragment

import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.antuere.musicapp.databinding.HeaderItemBinding
import com.antuere.domain.musicProfile.Day
import com.antuere.domain.util.convertDayOfWeekToNumber
import com.antuere.musicapp.util.MyPlayer
import kotlinx.coroutines.*


/*Adapter for MAIN recycle view, just for show single day
* with nested recycle view. Nested recycle view showing
* one playlist with time start, time end and proportion*/

class MainAdapter(private val currentDate: Int, private val myPlayer: MyPlayer) :
    ListAdapter<Day, MainAdapter.TitleViewHolder>(MyDiffCallBack()) {

    private val backScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleViewHolder {
        return TitleViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TitleViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, currentDate, myPlayer)
    }

    fun submitListOnAnotherThread(list: List<Day>) {
        backScope.launch {
            withContext(Dispatchers.Main) {
                submitList(list)
            }
        }
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

        fun bind(item: Day, currentDate: Int, myPlayer: MyPlayer) {

            val dayFromProfile = convertDayOfWeekToNumber(item.day.uppercase(), false)
            if (currentDate == dayFromProfile) {
                binding.header.paintFlags = binding.header.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                binding.header.setTextColor(Color.rgb(0, 137, 123))
            }

            binding.header.text = item.day.replaceFirstChar { it.titlecaseChar() }
            val childAdapter = ChildAdapter(myPlayer)
            binding.nestedRecycleView.adapter = childAdapter
            childAdapter.submitList(playlistItems[item.day])

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