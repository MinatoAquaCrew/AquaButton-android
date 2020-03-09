package moe.feng.aquabutton.ui.main.list

import moe.feng.aquabutton.R
import moe.feng.aquabutton.databinding.VoiceSingleItemBinding
import moe.feng.aquabutton.model.VoiceItem
import moe.feng.aquabutton.ui.common.list.DataBindingViewHolder
import moe.feng.aquabutton.ui.common.list.ItemBasedSimpleViewBinder
import moe.feng.aquabutton.ui.common.list.dataBindingViewHolderCreatorOf
import moe.feng.aquabutton.ui.main.list.VoiceItemBinder.ViewHolder

class VoiceItemBinder : ItemBasedSimpleViewBinder<VoiceItem, ViewHolder>() {

    class ViewHolder(dataBinding: VoiceSingleItemBinding) :
        DataBindingViewHolder<VoiceItem, VoiceSingleItemBinding>(dataBinding)

    override val viewHolderCreator: ViewHolderCreator<ViewHolder> =
        dataBindingViewHolderCreatorOf(R.layout.voice_single_item)

}