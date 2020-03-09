package moe.feng.aquabutton.ui.common

import android.app.Activity
import android.content.Context
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

abstract class BaseFragment : Fragment {

    constructor() : super()

    constructor(@LayoutRes layoutId: Int) : super(layoutId)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val title = getActivityTitle(context)
        if (title != null) {
            val activity = if (context is Activity) context else this.activity
            activity?.title = title
        }
    }

    open fun getActivityTitle(context: Context): String? {
        return null
    }

    fun launchWhenCreated(block: suspend CoroutineScope.() -> Unit): Job =
        lifecycleScope.launchWhenCreated(block)

    fun launchWhenStarted(block: suspend CoroutineScope.() -> Unit): Job =
        lifecycleScope.launchWhenStarted(block)

    fun launchWhenResumed(block: suspend CoroutineScope.() -> Unit): Job =
        lifecycleScope.launchWhenResumed(block)

}