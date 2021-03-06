package aquacrew.aquabutton.ui.main

import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import androidx.core.os.bundleOf
import androidx.core.view.updatePadding
import kotlinx.android.synthetic.main.fragment_main_search_result_list.*
import kotlinx.android.synthetic.main.fragment_main_search_result_list.contentList
import kotlinx.android.synthetic.main.fragment_main_search_result_list.contentTitle
import aquacrew.aquabutton.R
import aquacrew.aquabutton.model.VoiceItem
import aquacrew.aquabutton.ui.common.BaseFragment
import aquacrew.aquabutton.ui.main.list.ContributeVoicesItemBinder
import aquacrew.aquabutton.ui.main.list.VoiceItemListAdapter

class SearchResultListFragment : BaseFragment(R.layout.fragment_main_search_result_list) {

    companion object {

        private const val EXTRA_KEYWORD = "extra:KEYWORD"
        private const val EXTRA_DATA = "extra:DATA"

        operator fun invoke(keyword: String, data: List<VoiceItem>): SearchResultListFragment {
            return SearchResultListFragment().apply {
                arguments = bundleOf(
                    EXTRA_KEYWORD to keyword,
                    EXTRA_DATA to ArrayList(data)
                )
            }
        }

    }

    lateinit var keyword: String
    private lateinit var data: List<VoiceItem>

    private val adapter: VoiceItemListAdapter = VoiceItemListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireArguments().let {
            keyword = it.getString(EXTRA_KEYWORD)
                ?: throw IllegalArgumentException("keyword is null")
            data = it.getParcelableArrayList(EXTRA_DATA)
                ?: throw IllegalArgumentException("data is null")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        contentTitle.text = getString(R.string.search_result_title, keyword)

        contentList.adapter = adapter

        clearButton.setOnClickListener {
            (activity as? MainActivity)?.clearSearchResult()
        }

        adapter.items = data + ContributeVoicesItemBinder.Item()
    }

    override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets {
        val fabSize = resources.getDimensionPixelSize(R.dimen.fab_size)
        contentList.updatePadding(bottom = insets.systemWindowInsetBottom + fabSize)
        return insets
    }

}