package moe.feng.aquabutton.ui.common

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import moe.feng.aquabutton.R

abstract class BaseActivity(@LayoutRes open val layoutRes: Int = 0) : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (layoutRes > 0) {
            setContentView(layoutRes)

            val toolbar = findViewById<Toolbar>(R.id.toolbar)
            if (toolbar != null) {
                setSupportActionBar(toolbar)
            }

            val rootView = findViewById<View>(R.id.rootView)
            rootView?.setOnApplyWindowInsetsListener { _, insets -> onApplyWindowInsets(insets) }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    open fun onApplyWindowInsets(insets: WindowInsets): WindowInsets {
        return insets
    }

    fun setTransparentUi() {
        var flags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.decorView.systemUiVisibility = flags
    }

    fun launch(block: suspend CoroutineScope.() -> Unit): Job =
        lifecycleScope.launch(block = block)

    fun launchWhenCreated(block: suspend CoroutineScope.() -> Unit): Job =
        lifecycleScope.launchWhenCreated(block)

    fun launchWhenStarted(block: suspend CoroutineScope.() -> Unit): Job =
        lifecycleScope.launchWhenStarted(block)

    fun launchWhenResumed(block: suspend CoroutineScope.() -> Unit): Job =
        lifecycleScope.launchWhenResumed(block)

    fun View.onClick(block: suspend CoroutineScope.() -> Unit) {
        setOnClickListener {
            lifecycleScope.launch {
                block()
            }
        }
    }

}