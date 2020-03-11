package aquacrew.aquabutton.ui.main.list

import android.content.Intent
import android.view.View
import androidx.annotation.StringRes
import androidx.core.net.toUri
import kotlinx.android.synthetic.main.voice_no_result_item.*
import aquacrew.aquabutton.R
import aquacrew.aquabutton.ui.common.list.*
import aquacrew.aquabutton.ui.main.list.ContributeVoicesItemBinder.Item
import aquacrew.aquabutton.ui.main.list.ContributeVoicesItemBinder.ViewHolder

class ContributeVoicesItemBinder : ItemBasedSimpleViewBinder<Item, ViewHolder>() {

    override val viewHolderCreator: ViewHolderCreator<ViewHolder> =
        viewHolderCreatorOf(R.layout.voice_no_result_item)

    class Item(@StringRes val textRes: Int = R.string.no_result_text)

    class ViewHolder(itemView: View) : ItemBasedViewHolder<Item>(itemView) {

        override fun onItemClick() {
            val intent = Intent(Intent.ACTION_VIEW, "https://github.com/zyzsdy/aqua-button".toUri())
            context.startActivity(intent)
        }

        override fun onBind() {
            text1.text = context.getString(data.textRes, context.getString(R.string.vtuber_name))
        }

    }

}