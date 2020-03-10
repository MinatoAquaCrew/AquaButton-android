package moe.feng.aquabutton.ui.settings.list

import android.content.Intent
import android.view.View
import androidx.core.net.toUri
import kotlinx.android.synthetic.main.licenses_item.*
import moe.feng.aquabutton.R
import moe.feng.aquabutton.databinding.LicensesItemBinding
import moe.feng.aquabutton.model.LicenseItem
import moe.feng.aquabutton.ui.common.list.DataBindingViewHolder
import moe.feng.aquabutton.ui.common.list.ItemBasedSimpleViewBinder
import moe.feng.aquabutton.ui.common.list.dataBindingViewHolderCreatorOf

class LicensesItemBinder
    : ItemBasedSimpleViewBinder<LicenseItem, LicensesItemBinder.ViewHolder>() {

    override val viewHolderCreator: ViewHolderCreator<ViewHolder>
        = dataBindingViewHolderCreatorOf(R.layout.licenses_item)

    class ViewHolder(dataBinding: LicensesItemBinding) :
        DataBindingViewHolder<LicenseItem, LicensesItemBinding>(dataBinding) {

        init {
            cardView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, data.url.toUri())
                context.startActivity(intent)
            }
        }

    }

}