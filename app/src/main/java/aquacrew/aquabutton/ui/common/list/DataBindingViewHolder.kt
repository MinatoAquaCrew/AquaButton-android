package aquacrew.aquabutton.ui.common.list

import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import aquacrew.aquabutton.BR

abstract class DataBindingViewHolder<T : Any, DB : ViewDataBinding>(val dataBinding: DB)
    : ItemBasedViewHolder<T>(dataBinding.root) {

    @CallSuper
    override fun onBind() {
        dataBinding.setVariable(BR.data, data)
    }

}