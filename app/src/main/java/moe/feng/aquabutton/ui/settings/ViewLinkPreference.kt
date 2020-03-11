package moe.feng.aquabutton.ui.settings

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import androidx.core.net.toUri
import androidx.preference.Preference
import moe.feng.aquabutton.R

class ViewLinkPreference
@JvmOverloads
constructor (
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.preferenceStyle,
    defStyleRes: Int = R.style.Preference
) : Preference(context, attrs, defStyleAttr, defStyleRes) {

    var linkUrl: String? = null

    var exceptionHandler: (Exception) -> Unit = {}

    init {
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.ViewLinkPreference, defStyleAttr, defStyleRes
        )

        linkUrl = a.getString(R.styleable.ViewLinkPreference_linkUrl)

        a.recycle()
    }

    override fun onClick() {
        linkUrl?.let {
            try {
                val intent = Intent(Intent.ACTION_VIEW, it.toUri())
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            } catch (e: Exception) {
                exceptionHandler(e)
            }
        }
    }

}