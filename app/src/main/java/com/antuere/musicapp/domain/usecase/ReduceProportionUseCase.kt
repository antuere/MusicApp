package com.antuere.musicapp.domain.usecase

import com.antuere.musicapp.MyPlayer

class ReduceProportionUseCase {

    operator fun invoke(playlistId: Int): Int {
        MyPlayer.playlistsRequired.forEach {
            if (it.playlistId == playlistId) {
                it.proportion--
                return it.proportion
            }
        }
        return -1
    }
}