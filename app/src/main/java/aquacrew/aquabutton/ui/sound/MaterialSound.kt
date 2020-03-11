package aquacrew.aquabutton.ui.sound

import android.annotation.SuppressLint
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RawRes
import androidx.core.content.edit
import androidx.core.content.getSystemService
import aquacrew.aquabutton.AquaApp
import aquacrew.aquabutton.R

object MaterialSound : AquaApp.Component {

    private const val KEY_ENABLED = "MaterialSound_enabled"

    private val audioManager: AudioManager get() = context.getSystemService()!!

    private var lastMediaPlayer: MediaPlayer? = null

    private fun play(@RawRes rawRes: Int, forceEnabled: Boolean = false, volume: Float = 0.5f) {
        try {
            lastMediaPlayer?.stop()
            lastMediaPlayer?.release()
        } catch (ignored: Exception) {

        }
        if (!forceEnabled && !isEnabled) {
            return
        }
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .build()
        val id = audioManager.generateAudioSessionId()
        val mediaPlayer = MediaPlayer.create(context, rawRes, audioAttributes, id)
        mediaPlayer.setVolume(volume, volume)
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener {
            try {
                mediaPlayer.release()
            } catch (ignored: Exception) {

            }
        }
        lastMediaPlayer = mediaPlayer
    }

    var isEnabled: Boolean
        get() = AquaApp.preferences().getBoolean(KEY_ENABLED, true)
        set(value) {
            AquaApp.preferences().edit {
                putBoolean(KEY_ENABLED, value)
            }
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

    fun heroSimpleCelebration1() {
        play(R.raw.hero_simple_celebration_01, volume = 0.1f)
    }

    fun onTouchListenerForNavigationHoverTap(): View.OnTouchListener {
        return NavigationHoverTapTouchListener()
    }

    @SuppressLint("ClickableViewAccessibility")
    private class NavigationHoverTapTouchListener : View.OnTouchListener {

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                navigationHoverTap()
            }
            return false
        }

    }

}