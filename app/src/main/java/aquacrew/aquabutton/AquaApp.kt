package aquacrew.aquabutton

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.FileProvider
import androidx.core.content.edit
import aquacrew.aquabutton.api.AssetsApi
import aquacrew.aquabutton.api.provider.AquaButtonApiProvider
import moe.feng.common.eventshelper.EventsHelper
import java.io.File

class AquaApp : Application() {

    /**
     * Global app component can implement this interface to get application context easily.
     */
    interface Component {

        val context: Context get() = application

    }

    companion object {

        private lateinit var application: Application

        const val FILEPROVIDER_AUTHORITY = "${BuildConfig.APPLICATION_ID}.fileprovider"

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

        // Assign static application
        application = this

        // Install api provider implementation on application create
        AssetsApi.installProvider(AquaButtonApiProvider())

        // Init components
        EventsHelper.getInstance(this)

        // Set night mode
        AppCompatDelegate.setDefaultNightMode(darkMode)
    }

}