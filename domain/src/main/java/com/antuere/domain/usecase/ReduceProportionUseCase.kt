package com.antuere.domain.usecase

import com.antuere.domain.musicProfile.PlaylistsZone

class ReduceProportionUseCase(private val playlistsRequired: List<PlaylistsZone>) {

    operator fun invoke(playlistId: Int): Int {
        playlistsRequired.forEach {
            if (it.playlistId == playlistId) {
                it.proportion--
                return it.proportion
            }
        }
        return -1
    }
}