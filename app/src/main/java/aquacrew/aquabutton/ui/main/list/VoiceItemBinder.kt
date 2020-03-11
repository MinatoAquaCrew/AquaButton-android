package aquacrew.aquabutton.ui.main.list

import androidx.view.contextMenuBy
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import aquacrew.aquabutton.R
import aquacrew.aquabutton.api.AquaAssetsApi
import aquacrew.aquabutton.databinding.VoiceSingleItemBinding
import aquacrew.aquabutton.model.VoiceItem
import aquacrew.aquabutton.ui.common.list.DataBindingViewHolder
import aquacrew.aquabutton.ui.common.list.ItemBasedSimpleViewBinder
import aquacrew.aquabutton.ui.common.list.dataBindingViewHolderCreatorOf
import aquacrew.aquabutton.ui.main.event.MainUiEventCallback
import aquacrew.aquabutton.ui.main.list.VoiceItemBinder.ViewHolder
import aquacrew.aquabutton.util.VoicePlayer
import moe.feng.common.eventshelper.EventsHelper
import moe.feng.common.eventshelper.of

class VoiceItemBinder : ItemBasedSimpleViewBinder<VoiceItem, ViewHolder>() {

    class ViewHolder(dataBinding: VoiceSingleItemBinding) :
        DataBindingViewHolder<VoiceItem, VoiceSingleItemBinding>(dataBinding) {

        private var playJob: Job? = null

        init {
            itemView.contextMenuBy(
                menuRes = R.menu.context_menu_voice_item,
                headerTitle = { data.description() },
                onMenuItemClick = {
                    if (it.itemId == R.id.action_save_to) {
                        EventsHelper.getInstance(context)
                            .of<MainUiEventCallback>()
                            .requestSaveVoice(data)
                    }
                    true
                }
            )
        }

        override fun onItemClick() {
            playJob = launch {
                try {
                    VoicePlayer.stop()
                    VoicePlayer.play(AquaAssetsApi.getVoice(data))
                } catch (e: Exception) {
                    e.printStackTrace()
                    EventsHelper.getInstance(context)
                        .of<MainUiEventCallback>()
                        .showErrorTextOnSnackbar(context.getString(R.string.tips_failed_to_play))
                }
            }
        }

        override fun onRecycled() {
            playJob?.cancel()
        }

    }

    override val viewHolderCreator: ViewHolderCreator<ViewHolder> =
        dataBindingViewHolderCreatorOf(R.layout.voice_single_item)

}