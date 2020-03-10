package moe.feng.aquabutton.ui.settings

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.preference.Preference
import moe.feng.aquabutton.R
import moe.feng.aquabutton.ui.common.BasePreferenceFragment

class LicensesSettingsFragment : BasePreferenceFragment(R.xml.pref_licenses) {

    companion object {

        const val KEY_OSS_LICENSES = "oss_licenses"
        const val KEY_HOLO_TERMS = "holo_terms"

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
            KEY_HOLO_TERMS -> {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = getString(R.string.licenses_holo_terms_url).toUri()
                startActivity(intent)
                return true
            }
        }
        return super.onPreferenceTreeClick(preference)
    }

}