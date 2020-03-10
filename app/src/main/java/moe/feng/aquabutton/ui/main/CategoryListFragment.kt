package moe.feng.aquabutton.ui.main

import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import androidx.core.os.bundleOf
import androidx.core.view.updatePadding
import kotlinx.android.synthetic.main.fragment_main_category_list.*
import moe.feng.aquabutton.R
import moe.feng.aquabutton.model.VoiceCategory
import moe.feng.aquabutton.ui.common.BaseFragment
import moe.feng.aquabutton.ui.main.event.MainUiEventCallback
import moe.feng.aquabutton.ui.main.list.ContributeVoicesItemBinder
import moe.feng.aquabutton.ui.main.list.VoiceItemListAdapter
import moe.feng.common.eventshelper.EventsHelper
import moe.feng.common.eventshelper.of

class CategoryListFragment : BaseFragment(R.layout.fragment_main_category_list) {

    companion object {

        private const val EXTRA_CATEGORY = "extra:CATEGORY"

        operator fun invoke(category: VoiceCategory): CategoryListFragment {
            return CategoryListFragment().apply {
                arguments = bundleOf(EXTRA_CATEGORY to category)
            }
        }

    }

    lateinit var category: VoiceCategory

    private val adapter: VoiceItemListAdapter = VoiceItemListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        category = requireArguments().getParcelable(EXTRA_CATEGORY)
            ?: throw IllegalArgumentException("category is null")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        contentTitle.text = category.description()

        contentList.adapter = adapter

        adapter.items = category.voiceList.takeIf { it.isNotEmpty() }
            ?: listOf(ContributeVoicesItemBinder.Item())

        contentTitle.setOnClickListener {
            EventsHelper.getInstance(it.context)
                .of<MainUiEventCallback>()
                .toggleCategoryMenu()
        }
    }

    override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets {
        contentList.updatePadding(bottom = insets.systemWindowInsetBottom)
        return insets
    }

}