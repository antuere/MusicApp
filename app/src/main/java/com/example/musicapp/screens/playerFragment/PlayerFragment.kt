package com.example.musicapp.screens.playerFragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.musicapp.MyPlayer
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentPlayerBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.material.appbar.MaterialToolbar

class PlayerFragment : Fragment(), Player.Listener {

    companion object {
        fun newInstance() = PlayerFragment()
    }

    private lateinit var viewModel: PlayerViewModel
    private lateinit var binding: FragmentPlayerBinding
    private lateinit var actionBar: MaterialToolbar


    private lateinit var player: ExoPlayer
    private lateinit var playerView: StyledPlayerView

    private lateinit var activity: AppCompatActivity
    private lateinit var title: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        actionBar = binding.toolBarApp


        activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(actionBar)

        actionBar.navigationIcon =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_back_24)
        actionBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        player = MyPlayer.getInstanceMain(requireContext())
        playerView = binding.playerView
        playerView.player = player

        player.addListener(this)

        activity.supportActionBar?.title = ""
        title = binding.textTitle

        title.text = player.mediaMetadata.title
        title.ellipsize = TextUtils.TruncateAt.MARQUEE
        title.isSingleLine = true
        title.marqueeRepeatLimit = -1
        title.isSelected = true

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[PlayerViewModel::class.java]


    }

    override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
        super.onMediaMetadataChanged(mediaMetadata)

        title.text = mediaMetadata.title ?: "Title not found"

    }
}


