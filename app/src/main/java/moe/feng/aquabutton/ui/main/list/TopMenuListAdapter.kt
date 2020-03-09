package moe.feng.aquabutton.ui.main.list

import com.drakeet.multitype.MultiTypeAdapter
import moe.feng.aquabutton.model.VoiceCategory

class TopMenuListAdapter(
    var onVoiceCategoryItemClick: ((VoiceCategory) -> Unit) = {}
) : MultiTypeAdapter() {

    init {
        register(VoiceCategoryMenuItemBinder(onVoiceCategoryItemClick))
    }

}