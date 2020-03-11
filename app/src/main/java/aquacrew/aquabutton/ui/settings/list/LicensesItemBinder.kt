package aquacrew.aquabutton.ui.settings.list

import android.content.Intent
import androidx.core.net.toUri
import kotlinx.android.synthetic.main.licenses_item.*
import aquacrew.aquabutton.R
import aquacrew.aquabutton.databinding.LicensesItemBinding
import aquacrew.aquabutton.model.LicenseItem
import aquacrew.aquabutton.ui.common.list.DataBindingViewHolder
import aquacrew.aquabutton.ui.common.list.ItemBasedSimpleViewBinder
import aquacrew.aquabutton.ui.common.list.dataBindingViewHolderCreatorOf

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