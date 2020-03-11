package moe.feng.aquabutton.util.ext

import moe.shizuku.preference.*

inline fun <T : TwoStatePreference> T.onValueChanged(
    crossinline block: T.(value: Boolean) -> Boolean
) {
    setOnPreferenceChangeListener { preference, newValue ->
        (preference as T).block(newValue as Boolean)
    }
}

inline fun <T : ListPreference> T.onValueChanged(
    crossinline block: T.(value: String) -> Boolean
) {
    setOnPreferenceChangeListener { preference, newValue ->
        (preference as T).block(newValue as String)
    }
}

inline fun <T : SeekBarPreference> T.onValueChanged(
    crossinline block: T.(value: Int) -> Boolean
) {
    setOnPreferenceChangeListener { preference, newValue ->
        (preference as T).block(newValue as Int)
    }
}

inline fun <T : EditTextPreference> T.onValueChanged(
    crossinline block: T.(value: String) -> Boolean
) {
    setOnPreferenceChangeListener { preference, newValue ->
        (preference as T).block(newValue as? String ?: "")
    }
}

inline fun <T : Preference> T.onClick(crossinline block: T.() -> Unit) {
    setOnPreferenceClickListener {
        (it as T).block()
        true
    }
}
