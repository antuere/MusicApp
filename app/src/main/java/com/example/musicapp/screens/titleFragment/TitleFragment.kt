package com.example.musicapp.screens.titleFragment

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.musicapp.R
import com.example.musicapp.databinding.FragmentTitleBinding
import timber.log.Timber

class TitleFragment : Fragment() {

    companion object {
        fun newInstance() = TitleFragment()
    }

    private lateinit var viewModel: TitleViewModel
    private lateinit var binding: FragmentTitleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.i("my log in onCreateView")
        binding = FragmentTitleBinding.inflate(inflater, container, false)

        val factory = TitleViewModelFactory(requireActivity().application)

        viewModel = ViewModelProvider(this, factory)[TitleViewModel::class.java]

//        viewModel.profile.observe(viewLifecycleOwner) {
//            Timber.i("my log Music profile has name(in liveData) is ${it?.name ?: "null"}")
//            it?.let {
//                viewModel.getPlaylist()
//                requireActivity().title = it.name
//            }
//        }

        viewModel.playlists.observe(viewLifecycleOwner) {
            it?.let {
                Timber.i("my log we in playlist LiveData and his size ${it.size}")
                binding.testText.text = it.size.toString()
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
                        findNavController().navigate(TitleFragmentDirections.actionTitleFragmentToPlayerFragment())
                        true
                    }
                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.STARTED)

    }
}