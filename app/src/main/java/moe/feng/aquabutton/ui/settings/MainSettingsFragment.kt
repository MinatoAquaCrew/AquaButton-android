package moe.feng.aquabutton.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import moe.feng.aquabutton.AquaApp
import moe.feng.aquabutton.R
import moe.feng.aquabutton.ui.common.BasePreferenceFragment
import moe.feng.aquabutton.ui.sound.MaterialSound
import moe.feng.aquabutton.util.IntentUtils
import moe.feng.aquabutton.util.ext.onValueChanged
import moe.shizuku.preference.SimpleMenuPreference
import moe.shizuku.preference.SwitchPreference

class MainSettingsFragment : BasePreferenceFragment(R.xml.pref_main) {

    private val darkMode: SimpleMenuPreference by preference("dark_mode")
    private val soundEnabled: SwitchPreference by preference("ui_sound_enabled")

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)

        var currentDarkMode = AppCompatDelegate.getDefaultNightMode().toString()
        if (currentDarkMode !in resources.getStringArray(R.array.dark_mode_entry_values)) {
            currentDarkMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM.toString()
        }
        darkMode.value = currentDarkMode
        darkMode.onValueChanged { newValue ->
            val value = newValue.toInt()
            AquaApp.darkMode = value
            activity?.let { IntentUtils.restartApp(it) }
            true
        }

        soundEnabled.isChecked = MaterialSound.isEnabled
        soundEnabled.onValueChanged { value ->
            MaterialSound.isEnabled = value
            true
        }
    }

}