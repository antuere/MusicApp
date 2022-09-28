package com.example.musicapp.domain.usecase

import com.example.musicapp.MyPlayer

class IncreaseProportionUseCase {

    operator fun invoke(playlistId: Int): Int {
        MyPlayer.playlistsRequired.forEach {
            if (it.playlistId == playlistId) {
                it.proportion++
                return it.proportion
            }
        }
        return -1
    }
}