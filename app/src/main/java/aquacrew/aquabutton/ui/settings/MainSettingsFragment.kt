package aquacrew.aquabutton.ui.settings

import android.os.Bundle
import android.text.format.Formatter
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.SimpleMenuPreference
import androidx.preference.SwitchPreference
import androidx.preference.onValueChanged
import com.google.android.material.snackbar.Snackbar
import aquacrew.aquabutton.AquaApp
import aquacrew.aquabutton.R
import aquacrew.aquabutton.api.AssetsApi
import aquacrew.aquabutton.ui.common.BasePreferenceFragment
import aquacrew.aquabutton.ui.sound.MaterialSound
import aquacrew.aquabutton.util.IntentUtils

class MainSettingsFragment : BasePreferenceFragment(R.xml.pref_main) {

    private val darkMode: SimpleMenuPreference by preference("dark_mode")
    private val soundEnabled: SwitchPreference by preference("ui_sound_enabled")
    private val voiceCache: Preference by preference("clear_voice_cache")

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

    override fun onResume() {
        super.onResume()
        updateVoiceCacheSummary()
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        if (preference == voiceCache) {
            launchWhenCreated {
                val totalSize = AssetsApi.getVoiceCacheSize()
                AssetsApi.clearVoiceCache()
                val clearedSize = totalSize - AssetsApi.getVoiceCacheSize()
                MaterialSound.heroSimpleCelebration1()
                Snackbar.make(
                    listView,
                    getString(R.string.tips_clear_cache_format,
                        Formatter.formatShortFileSize(requireContext(), clearedSize)),
                    Snackbar.LENGTH_LONG
                ).show()
                updateVoiceCacheSummary()
            }
            return true
        }
        return super.onPreferenceTreeClick(preference)
    }

    private fun updateVoiceCacheSummary() {
        launchWhenCreated {
            val summaryFormat = getString(R.string.clear_voice_cache_summary)
            voiceCache.summary = summaryFormat.format(getString(R.string.placeholder_calculating))
            val cacheSize = AssetsApi.getVoiceCacheSizeText()
            voiceCache.summary = summaryFormat.format(cacheSize)
        }
    }

}