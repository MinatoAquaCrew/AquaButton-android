package androidx.view

import android.content.Context
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.MenuRes
import androidx.core.content.getSystemService
import androidx.core.view.forEach

fun View.hideKeyboard(flags: Int = 0) {
    val manager = context.getSystemService<InputMethodManager>()
    manager?.hideSoftInputFromWindow(windowToken, flags)
}

fun View.contextMenuBy(
    @MenuRes menuRes: Int,
    headerTitle: (context: Context) -> CharSequence? = { null },
    onMenuItemClick: (MenuItem) -> Boolean
) {
    setOnCreateContextMenuListener { menu, _, _ ->
        MenuInflater(context).inflate(menuRes, menu)
        menu.setHeaderTitle(headerTitle(context))
        menu.forEach {
            it.setOnMenuItemClickListener { item -> onMenuItemClick(item) }
        }
    }
}
