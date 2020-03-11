package aquacrew.aquabutton.ui.common

import android.graphics.Color
import android.os.Build
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
import aquacrew.aquabutton.R
import aquacrew.aquabutton.util.ResourcesUtils

abstract class BaseActivity(@LayoutRes open val layoutRes: Int = 0) : AppCompatActivity() {

    var lightNavBar: Boolean = false
    var hideNavigation: Boolean = true

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

    fun setTransparentUi(lightNavBar: Boolean? = null, hideNavigation: Boolean? = null) {
        if (lightNavBar != null) {
            this.lightNavBar = lightNavBar
        }
        if (hideNavigation != null) {
            this.hideNavigation = hideNavigation
        }

        var lightNavBarFlag = this.lightNavBar
        var flags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        val lightStatusArray = obtainStyledAttributes(
            intArrayOf(android.R.attr.windowLightStatusBar))
        val lightStatus = lightStatusArray.getBoolean(0, false)
        lightStatusArray.recycle()
        if (lightStatus) {
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        if (this.hideNavigation) {
            flags = flags or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            window.navigationBarColor = Color.TRANSPARENT
        } else {
            window.navigationBarColor = ResourcesUtils.resolveColor(
                theme, android.R.attr.navigationBarColor)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                lightNavBarFlag = true
            }
        }
        if (lightNavBarFlag && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
        window.decorView.systemUiVisibility = flags
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }
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