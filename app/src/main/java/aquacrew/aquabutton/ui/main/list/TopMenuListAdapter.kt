package aquacrew.aquabutton.ui.main.list

import com.drakeet.multitype.MultiTypeAdapter
import aquacrew.aquabutton.model.VoiceCategory

class TopMenuListAdapter(
    var onVoiceCategoryItemClick: ((VoiceCategory) -> Unit) = {}
) : MultiTypeAdapter() {

    init {
        register(VoiceCategoryMenuItemBinder(onVoiceCategoryItemClick))
    }

}