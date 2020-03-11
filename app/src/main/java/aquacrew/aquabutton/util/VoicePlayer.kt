package aquacrew.aquabutton.util

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import aquacrew.aquabutton.AquaApp
import java.io.File

object VoicePlayer : AquaApp.Component {

    private var mediaPlayer: MediaPlayer? = null

    private var volume: Float = 0.8f
        set(value) {
            field = value.coerceIn(0f, 1f)
            mediaPlayer?.setVolume(value, value)
        }

    fun init() {
        mediaPlayer = MediaPlayer().also {
            it.setAudioAttributes(AudioAttributes.Builder().apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    setAllowedCapturePolicy(AudioAttributes.ALLOW_CAPTURE_BY_ALL)
                }
                setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                setUsage(AudioAttributes.USAGE_MEDIA)
            }.build())
            it.setVolume(volume, volume)
        }
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    suspend fun play(file: File) {
        play(AquaApp.getUriForFile(file))
    }

    suspend fun play(uri: Uri) {
        withContext(Dispatchers.IO) {
            mediaPlayer?.run {
                reset()
                setDataSource(context, uri)
                prepare()
                start()
            } ?: throw IllegalStateException("Did you forget to init VoicePlayer")
        }
    }

    fun stop() {
        mediaPlayer?.stop()
    }

}