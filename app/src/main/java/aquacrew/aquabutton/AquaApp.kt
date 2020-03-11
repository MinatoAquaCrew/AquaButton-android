package aquacrew.aquabutton

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.FileProvider
import androidx.core.content.edit
import moe.feng.common.eventshelper.EventsHelper
import java.io.File

class AquaApp : Application() {

    interface Component {

        val context: Context get() = application

    }

    companion object {

        private lateinit var application: Application

        const val FILEPROVIDER_AUTHORITY = "aquacrew.aquabutton.fileprovider"

        const val PREFERENCE_NAME_DEFAULT = "aquabutton"

        const val KEY_DARK_MODE = "AquaApp_dark_mode"

        fun getUriForFile(file: File): Uri {
            return FileProvider.getUriForFile(application, FILEPROVIDER_AUTHORITY, file)
        }

        fun preferences(): SharedPreferences {
            return application.getSharedPreferences(PREFERENCE_NAME_DEFAULT, Context.MODE_PRIVATE)
        }

        var darkMode: Int
            get() = preferences().getInt(KEY_DARK_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            set(value) {
                preferences().edit(commit = true) {
                    putInt(KEY_DARK_MODE, value)
                }
                AppCompatDelegate.setDefaultNightMode(value)
            }

    }

    override fun onCreate() {
        super.onCreate()

        application = this

        EventsHelper.getInstance(this)

        AppCompatDelegate.setDefaultNightMode(darkMode)
    }

}