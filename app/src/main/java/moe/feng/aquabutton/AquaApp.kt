package moe.feng.aquabutton

import android.app.Application
import moe.feng.common.eventshelper.EventsHelper

class AquaApp : Application() {

    override fun onCreate() {
        super.onCreate()

        EventsHelper.getInstance(this)
    }

}