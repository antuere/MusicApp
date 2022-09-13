package com.example.musicapp.screens.titleFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.network.MusicApi
import com.example.musicapp.network.musicProfile.MusicProfile
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class TitleViewModel : ViewModel() {


    private var _profile = MutableLiveData<MusicProfile>()
    val response: LiveData<MusicProfile>
        get() = _profile

    private val _showError = MutableLiveData<String>()
    val showError: LiveData<String>
        get() = _showError

    init {
        getResponseFromServer()
    }

    private fun getResponseFromServer() {
        viewModelScope.launch {
            try {
                _profile.value = MusicApi.retrofitService.getJSON()
            } catch (e: Exception) {

                _showError.value = e.message
            }
        }


    }
}