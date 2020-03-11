package aquacrew.aquabutton.ui.settings

import android.content.Context
import androidx.preference.Preference
import aquacrew.aquabutton.R
import aquacrew.aquabutton.ui.common.BasePreferenceFragment

class LicensesSettingsFragment : BasePreferenceFragment(R.xml.pref_licenses) {

    companion object {

        const val KEY_OSS_LICENSES = "oss_licenses"

    }

    override fun getActivityTitle(context: Context): CharSequence? {
        return context.getString(R.string.action_licenses)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        when (preference.key) {
            KEY_OSS_LICENSES -> {
                PreferenceActivity.start<OSSLicensesFragment>(requireContext())
                return true
            }
        }
        return super.onPreferenceTreeClick(preference)
    }

}