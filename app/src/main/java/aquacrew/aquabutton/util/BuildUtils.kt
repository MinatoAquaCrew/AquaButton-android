package aquacrew.aquabutton.util

import android.os.Build
import java.util.*

object BuildUtils {

    fun isMeizu(): Boolean {
        return Build.MANUFACTURER.toLowerCase(Locale.getDefault()).contains("meizu")
    }

}