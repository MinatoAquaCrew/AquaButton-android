package aquacrew.aquabutton.ui.main.list

import com.drakeet.multitype.MultiTypeAdapter

class VoiceItemListAdapter : MultiTypeAdapter() {

    init {
        register(VoiceItemBinder())
        register(ContributeVoicesItemBinder())
    }

}