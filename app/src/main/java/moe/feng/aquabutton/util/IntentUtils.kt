package moe.feng.aquabutton.util

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import moe.feng.aquabutton.ui.main.MainActivity

object IntentUtils {

    fun restartApp(activity: Activity) {
        val intent = Intent.makeRestartActivityTask(
            ComponentName(activity, MainActivity::class.java)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        activity.startActivity(intent)
        activity.finish()
        Runtime.getRuntime().exit(0)
    }

}