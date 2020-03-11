package androidx.preference

typealias Preference = moe.shizuku.preference.Preference
typealias EditTextPreference = moe.shizuku.preference.EditTextPreference
typealias ListPreference = moe.shizuku.preference.ListPreference
typealias SeekBarPreference = moe.shizuku.preference.SeekBarPreference
typealias SwitchPreference = moe.shizuku.preference.SwitchPreference
typealias TwoStatePreference = moe.shizuku.preference.TwoStatePreference
typealias SimpleMenuPreference = moe.shizuku.preference.SimpleMenuPreference

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
