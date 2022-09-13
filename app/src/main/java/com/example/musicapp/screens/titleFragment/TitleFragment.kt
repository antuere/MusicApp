package com.example.musicapp.screens.titleFragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.musicapp.databinding.FragmentTitleBinding

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
        binding = FragmentTitleBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[TitleViewModel::class.java]

        viewModel.response.observe(viewLifecycleOwner){
            it?.let {
                binding.testText.text = it.name
            }
        }

        viewModel.showError.observe(viewLifecycleOwner){
            it?.let{
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}