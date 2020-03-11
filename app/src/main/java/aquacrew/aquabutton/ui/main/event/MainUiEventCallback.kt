package aquacrew.aquabutton.ui.main.event

import aquacrew.aquabutton.model.VoiceItem
import moe.feng.common.eventshelper.EventsListener
import moe.feng.common.eventshelper.EventsOnThread

@EventsListener
interface MainUiEventCallback {

    @EventsOnThread(EventsOnThread.MAIN_THREAD)
    fun toggleCategoryMenu()

    @EventsOnThread(EventsOnThread.MAIN_THREAD)
    fun showErrorTextOnSnackbar(text: String)

    @EventsOnThread(EventsOnThread.MAIN_THREAD)
    fun requestSaveVoice(voice: VoiceItem)

    @EventsOnThread(EventsOnThread.MAIN_THREAD)
    fun requestReload()

}