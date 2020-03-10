package moe.feng.aquabutton.ui.main.list

import kotlinx.android.synthetic.main.voice_category_menu_item.*
import moe.feng.aquabutton.R
import moe.feng.aquabutton.databinding.VoiceCategoryMenuItemBinding
import moe.feng.aquabutton.model.VoiceCategory
import moe.feng.aquabutton.ui.common.list.DataBindingViewHolder
import moe.feng.aquabutton.ui.common.list.ItemBasedSimpleViewBinder
import moe.feng.aquabutton.ui.common.list.dataBindingViewHolderCreatorOf
import moe.feng.aquabutton.ui.main.list.VoiceCategoryMenuItemBinder.ViewHolder
import moe.feng.aquabutton.ui.sound.MaterialSound

class VoiceCategoryMenuItemBinder(
    var onItemClick: ((VoiceCategory) -> Unit) = {}
) : ItemBasedSimpleViewBinder<VoiceCategory, ViewHolder>() {

    class ViewHolder(dataBinding: VoiceCategoryMenuItemBinding) :
        DataBindingViewHolder<VoiceCategory, VoiceCategoryMenuItemBinding>(dataBinding)

    override val viewHolderCreator: ViewHolderCreator<ViewHolder> =
        dataBindingViewHolderCreatorOf(R.layout.voice_category_menu_item)

    override fun onViewHolderCreated(holder: ViewHolder) {
        holder.button1.setOnTouchListener(MaterialSound.onTouchListenerForNavigationHoverTap())
        holder.button1.setOnClickListener {
            onItemClick(holder.data)
        }
    }

}