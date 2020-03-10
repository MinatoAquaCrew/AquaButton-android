package moe.feng.aquabutton.ui.main.event

import moe.feng.common.eventshelper.EventsListener
import moe.feng.common.eventshelper.EventsOnThread

@EventsListener
interface MainUiEventCallback {

    @EventsOnThread(EventsOnThread.MAIN_THREAD)
    fun showErrorTextOnSnackbar(text: String)

}