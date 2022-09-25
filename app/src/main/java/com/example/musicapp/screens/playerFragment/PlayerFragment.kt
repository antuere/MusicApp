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
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentPlayerBinding
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.material.appbar.MaterialToolbar

class PlayerFragment : Fragment() {

    companion object {
        fun newInstance() = PlayerFragment()
    }

    private lateinit var viewModel: PlayerViewModel
    private lateinit var binding: FragmentPlayerBinding
    private lateinit var actionBar: MaterialToolbar


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

        playerView = binding.playerView
        activity.supportActionBar?.title = ""
        title = binding.textTitle

        title.ellipsize = TextUtils.TruncateAt.MARQUEE
        title.isSingleLine = true
        title.marqueeRepeatLimit = -1
        title.isSelected = true

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = PlayerViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, factory)[PlayerViewModel::class.java]


        viewModel.showMain.observe(viewLifecycleOwner) {

            if (it) {
                playerView.player = viewModel.mainPlayer.value
                title.text =
                        viewModel.mainPlayer.value!!.mediaMetadata.title ?: "Today without music"

            } else {
                playerView.player = viewModel.extraPlayer.value
                title.text =
                        viewModel.extraPlayer.value!!.mediaMetadata.title ?: "Today without music"


            }
        }

        viewModel.changeTitle.observe(viewLifecycleOwner) {

            if (it) {
                title.text =
                        viewModel.mainPlayer.value!!.mediaMetadata.title ?: "Today without music"

            } else {
                title.text =
                        viewModel.extraPlayer.value!!.mediaMetadata.title ?: "Today without music"
            }
        }

    }

}


