package moe.feng.aquabutton.ui.settings

import android.os.Bundle
import androidx.preference.SwitchPreference
import androidx.preference.onValueChanged
import moe.feng.aquabutton.R
import moe.feng.aquabutton.ui.common.BasePreferenceFragment
import moe.feng.aquabutton.ui.sound.MaterialSound

class MainSettingsFragment : BasePreferenceFragment(R.xml.pref_main) {

    private val soundEnabled: SwitchPreference by preference("ui_sound_enabled")

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        super.onCreatePreferences(savedInstanceState, rootKey)

        soundEnabled.isChecked = MaterialSound.isEnabled
        soundEnabled.onValueChanged { value ->
            MaterialSound.isEnabled = value
            true
        }
    }

}