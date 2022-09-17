package com.example.musicapp.screens.titleFragment

import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicapp.MyPlayer
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentTitleBinding
import com.example.musicapp.foldersPaths
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.material.appbar.MaterialToolbar
import timber.log.Timber
import java.io.File

class TitleFragment : Fragment(), Player.Listener {

    companion object {
        fun newInstance() = TitleFragment()
    }

    private lateinit var viewModel: TitleViewModel
    private lateinit var binding: FragmentTitleBinding
    private lateinit var actionBar: MaterialToolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTitleBinding.inflate(inflater, container, false)
        actionBar = binding.toolBarApp
        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(actionBar)

        Timber.i("my log in onCreateView")

        val factory = TitleViewModelFactory(requireActivity().application)

        viewModel = ViewModelProvider(this, factory)[TitleViewModel::class.java]

        viewModel.profile.observe(viewLifecycleOwner) {
            Timber.i("my log Music profile has name(in liveData) is ${it?.name ?: "null"}")
            it?.let {
                activity.supportActionBar!!.title = it.name
            }
        }

        viewModel.playlists.observe(viewLifecycleOwner) {
            it?.let {
                Timber.i("my log we in playlist LiveData and his size ${it.size}")
            }
        }

        viewModel.renderUI.observe(viewLifecycleOwner) {
            if (it) {
                val profile = viewModel.profile.value!!
                val adapter = MainAdapter(profile)
                binding.dayList.adapter = adapter
                adapter.submitListOnAnotherThread(profile.schedule.days.sorted())
                binding.progressCircular.visibility = View.GONE

            } else {
                binding.progressCircular.show()
            }
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.showError.observe(viewLifecycleOwner) {
            it?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                Timber.i("error : $it")
            }
        }


        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.play_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.play -> {
                        findNavController().navigate(
                            TitleFragmentDirections.actionTitleFragmentToPlayerFragment(
                                viewModel.profile.value!!.schedule
                            )
                        )
                        true
                    }
                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }


}