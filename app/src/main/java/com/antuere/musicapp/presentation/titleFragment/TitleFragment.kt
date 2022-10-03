package com.antuere.musicapp.presentation.titleFragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.antuere.musicapp.R
import com.antuere.musicapp.databinding.FragmentTitleBinding
import com.antuere.musicapp.util.MyPlayer
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class TitleFragment : Fragment() {

    companion object {
        fun newInstance() = TitleFragment()
    }

    @Inject lateinit var myPlayer: MyPlayer

    private val viewModel: TitleViewModel by viewModels()

    private lateinit var binding: FragmentTitleBinding
    private lateinit var actionBar: MaterialToolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTitleBinding.inflate(inflater, container, false)
        actionBar = binding.toolBarApp
        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(actionBar)

        Timber.i("my log in onCreateView")

//      Observe profile and when he is not null, change title for ActionBar on name of profile
        viewModel.profile.observe(viewLifecycleOwner) {
            Timber.i("my log Music profile has name(in liveData) is ${it?.name ?: "null"}")
            it?.let {
                activity.supportActionBar!!.title = it.name
            }
        }


        /*Show main recycleView when profile success download and hide progress bar,
        else just show progress bar */
        viewModel.renderUI.observe(viewLifecycleOwner) {
            if (it) {
                val profile = viewModel.profile.value!!
                val calendar = Calendar.getInstance()
                val currentDayOnCalendar = calendar.get(Calendar.DAY_OF_WEEK)

                val adapter = MainAdapter(currentDayOnCalendar, myPlayer)
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

        //If retrofit throw Error : app will not crash, instead this we will show toast with error message
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
                            TitleFragmentDirections.actionTitleFragmentToPlayerFragment()
                        )
                        true
                    }
                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }


    // Release players when app destroy
    override fun onDestroy() {
        super.onDestroy()
        viewModel.player.observe(viewLifecycleOwner) {
            it?.release()
        }

        viewModel.playerExtra.observe(viewLifecycleOwner) {
            it?.release()
        }
    }

}