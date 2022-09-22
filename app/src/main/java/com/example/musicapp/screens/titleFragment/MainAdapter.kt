package com.example.musicapp.screens.titleFragment

import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.convertDayOfWeekToNumber
import com.example.musicapp.databinding.HeaderItemBinding
import com.example.musicapp.network.musicProfile.Day
import kotlinx.coroutines.*
import java.util.*



/*Adapter for MAIN recycle view, just for show single day
* with nested recycle view. Nested recycle view showing
* one playlist with time start, time end and proportion*/

class MainAdapter :
    ListAdapter<Day, MainAdapter.TitleViewHolder>(MyDiffCallBack()) {

    private val backScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleViewHolder {
        return TitleViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TitleViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
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

        fun bind(item: Day) {
            val calendar = Calendar.getInstance()
            val currentDayOnCalendar = calendar.get(Calendar.DAY_OF_WEEK)
            val dayFromProfile = convertDayOfWeekToNumber(item.day.uppercase(), false)

            if (currentDayOnCalendar == dayFromProfile){
                binding.header.paintFlags = binding.header.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                binding.header.setTextColor(Color.rgb(0,137,123))
            }

            binding.header.text = item.day.replaceFirstChar { it.titlecaseChar() }

            val childAdapter = ChildAdapter()
            binding.nestedRecycleView.adapter = childAdapter

            childAdapter.submitList(playlistItems[item])

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