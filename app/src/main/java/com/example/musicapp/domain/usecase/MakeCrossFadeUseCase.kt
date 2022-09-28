package com.example.musicapp.domain.usecase

import com.google.android.exoplayer2.ExoPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import kotlin.concurrent.timerTask

class MakeCrossFadeUseCase(
    private val playerFirst: ExoPlayer,
    private val playerSecond: ExoPlayer
) {
    operator fun invoke() {
        val position = playerFirst.currentPosition
        val timeLeft = playerFirst.contentDuration - position

//        Start crossFade effect 7 seconds before the end of the song
        val delay = timeLeft - 7000
        var delayValue = 0.7F

        val startSong = playerFirst.currentMediaItem!!.mediaMetadata.title

        val timer = Timer()

        val timerTask = timerTask {
            CoroutineScope(Dispatchers.Main).launch {

                if (!playerFirst.isPlaying) {
                    Timber.i("my log: return ;(")
                    return@launch
                }

                val currentSong = playerFirst.currentMediaItem!!.mediaMetadata.title

                if (!playerSecond.isPlaying && currentSong == startSong) {

                    Timber.i("my log: start change volumes")
                    playerFirst.volume = 0.9F
                    delay(350)
                    playerFirst.volume = 0.85F
                    delay(350)

                    playerFirst.volume = 0.8F
                    delay(350)
                    playerFirst.volume = 0.75F
                    delay(350)

                    playerSecond.prepare()
                    playerSecond.play()

                    repeat(14) {
                        playerSecond.volume = 1 - delayValue
                        playerFirst.volume = delayValue
                        delay(350)

                        delayValue -= 0.05F
                    }

                    playerSecond.volume = 1F
                    playerFirst.volume = 0.0F
                    delay(800)

                    playerFirst.stop()


                }
            }
        }
        timer.schedule(timerTask, delay)
    }
}