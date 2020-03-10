package androidx.view

import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService

fun View.hideKeyboard(flags: Int = 0) {
    val manager = context.getSystemService<InputMethodManager>()
    manager?.hideSoftInputFromWindow(windowToken, flags)
}
