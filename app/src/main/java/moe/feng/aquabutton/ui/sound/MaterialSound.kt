package moe.feng.aquabutton.ui.sound

import android.annotation.SuppressLint
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RawRes
import androidx.core.content.getSystemService
import moe.feng.aquabutton.AquaApp
import moe.feng.aquabutton.R

object MaterialSound : AquaApp.Component {

    private val audioManager: AudioManager get() = context.getSystemService()!!

    private var lastMediaPlayer: MediaPlayer? = null

    private fun play(@RawRes rawRes: Int) {
        try {
            lastMediaPlayer?.stop()
            lastMediaPlayer?.release()
        } catch (ignored: Exception) {

        }
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .build()
        val id = audioManager.generateAudioSessionId()
        val mediaPlayer = MediaPlayer.create(context, rawRes, audioAttributes, id)
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener {
            try {
                mediaPlayer.release()
            } catch (ignored: Exception) {

            }
        }
        lastMediaPlayer = mediaPlayer
    }

    fun navigationBackwardSelection() {
        play(R.raw.navigation_backward_selection)
    }

    fun navigationForwardSelection() {
        play(R.raw.navigation_forward_selection)
    }

    fun navigationHoverTap() {
        play(R.raw.navigation_hover_tap)
    }

    fun alertError3() {
        play(R.raw.alert_error_03)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun onTouchListenerForNavigationHoverTap(): View.OnTouchListener {
        return View.OnTouchListener { _, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                navigationHoverTap()
            }
            false
        }
    }

}