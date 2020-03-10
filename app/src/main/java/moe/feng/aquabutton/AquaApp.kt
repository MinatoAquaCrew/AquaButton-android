package moe.feng.aquabutton

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import moe.feng.aquabutton.api.AquaAssetsApi
import moe.feng.common.eventshelper.EventsHelper
import java.io.File

class AquaApp : Application() {

    interface Component {

        val context: Context get() = application

    }

    companion object {

        private lateinit var application: Application

        const val FILEPROVIDER_AUTHORITY = "moe.feng.aquabutton.fileprovider"

        fun getUriForFile(file: File): Uri {
            return FileProvider.getUriForFile(application, FILEPROVIDER_AUTHORITY, file)
        }

    }

    override fun onCreate() {
        super.onCreate()

        application = this

        EventsHelper.getInstance(this)
    }

}