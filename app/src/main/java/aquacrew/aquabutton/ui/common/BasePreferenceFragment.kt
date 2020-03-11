package aquacrew.aquabutton.ui.common

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.preference.Preference
import androidx.preference.PreferenceFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import aquacrew.aquabutton.ui.settings.PreferenceActivity

abstract class BasePreferenceFragment(
    private val preferencesRes: Int = 0
) : PreferenceFragment() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        if (preferencesRes != 0) {
            addPreferencesFromResource(preferencesRes)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val title = getActivityTitle(context)
        if (title != null) {
            val activity = if (context is Activity) context else this.activity
            activity?.title = title
        }
    }

    fun setPreferenceClickListener(key: String,
                                   onClick: suspend CoroutineScope.() -> Unit) {
        findPreference(key)!!.setOnPreferenceClickListener {
            lifecycleScope.launch { onClick() }
            true
        }
    }

    inline fun <reified T : Any> setPreferenceChangeListener(
        key: String,
        crossinline onChange: (T) -> Boolean) {
        findPreference(key)!!.setOnPreferenceChangeListener { _, newValue ->
            onChange(newValue as T)
        }
    }

    inline fun <reified T : Fragment> launchPreference() {
        activity?.let { PreferenceActivity.start(it, T::class.java) }
    }

    open fun getActivityTitle(context: Context): CharSequence? {
        return null
    }

    inline fun <reified T : Preference> preference(key: String): Lazy<T> {
        return lazy { findPreference(key) as T }
    }

    fun launchWhenCreated(block: suspend CoroutineScope.() -> Unit): Job =
        lifecycleScope.launchWhenCreated(block)

    fun launchWhenStarted(block: suspend CoroutineScope.() -> Unit): Job =
        lifecycleScope.launchWhenStarted(block)

    fun launchWhenResumed(block: suspend CoroutineScope.() -> Unit): Job =
        lifecycleScope.launchWhenResumed(block)

}