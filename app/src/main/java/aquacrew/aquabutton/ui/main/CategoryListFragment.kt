package aquacrew.aquabutton.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.updatePadding
import kotlinx.android.synthetic.main.fragment_main_category_list.*
import aquacrew.aquabutton.R
import aquacrew.aquabutton.model.VoiceCategory
import aquacrew.aquabutton.ui.common.BaseFragment
import aquacrew.aquabutton.ui.main.event.MainUiEventCallback
import aquacrew.aquabutton.ui.main.list.ContributeVoicesItemBinder
import aquacrew.aquabutton.ui.main.list.VoiceItemListAdapter
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

        adapter.items = category.voiceList +
                ContributeVoicesItemBinder.Item(R.string.missing_voices_text)

        titleButton.setOnClickListener {
            EventsHelper.getInstance(it.context)
                .of<MainUiEventCallback>()
                .toggleCategoryMenu()
        }

        subscribeButton.text = getString(R.string.subscribe_button, getString(R.string.vtuber_name))
        subscribeButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.data = getString(R.string.vtuber_url).toUri()
            try {
                startActivity(intent)
            } catch (ignored: Exception) {

            }
        }
    }

    override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets {
        val fabSize = resources.getDimensionPixelSize(R.dimen.fab_size)
        contentList.updatePadding(bottom = insets.systemWindowInsetBottom + fabSize)
        return insets
    }

}