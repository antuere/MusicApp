package com.example.musicapp.domain.usecase

import com.example.musicapp.MyPlayer
import com.google.android.exoplayer2.ExoPlayer

class UpdateSongsUseCase(private val player: ExoPlayer, private val playerExtra: ExoPlayer) {


    operator fun invoke() {
        player.volume = 1F
        playerExtra.volume = 1F

        MyPlayer.resetFlags()

        player.stop()
        playerExtra.stop()

        player.clearMediaItems()
        playerExtra.clearMediaItems()

        val addSongsToPlayerUseCase = AddSongsToPlayerUseCase(player, playerExtra)
        addSongsToPlayerUseCase.invoke()

        MyPlayer.isAddNew = true

        player.prepare()
        player.play()

        playerExtra.volume = 0F
    }
}
