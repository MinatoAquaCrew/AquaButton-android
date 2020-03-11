package aquacrew.aquabutton.ui.main.list

import kotlinx.android.synthetic.main.voice_category_menu_item.*
import aquacrew.aquabutton.R
import aquacrew.aquabutton.databinding.VoiceCategoryMenuItemBinding
import aquacrew.aquabutton.model.VoiceCategory
import aquacrew.aquabutton.ui.common.list.DataBindingViewHolder
import aquacrew.aquabutton.ui.common.list.ItemBasedSimpleViewBinder
import aquacrew.aquabutton.ui.common.list.dataBindingViewHolderCreatorOf
import aquacrew.aquabutton.ui.main.list.VoiceCategoryMenuItemBinder.ViewHolder
import aquacrew.aquabutton.ui.sound.MaterialSound

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