package moe.feng.aquabutton.ui.main

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.inputmethod.EditorInfo
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.commit
import androidx.lifecycle.whenCreated
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import androidx.view.hideKeyboard
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import moe.feng.aquabutton.R
import moe.feng.aquabutton.api.AquaAssetsApi
import moe.feng.aquabutton.model.VoiceCategory
import moe.feng.aquabutton.model.VoiceItem
import moe.feng.aquabutton.model.selectItem
import moe.feng.aquabutton.ui.common.BaseActivity
import moe.feng.aquabutton.ui.main.event.MainUiEventCallback
import moe.feng.aquabutton.ui.main.list.TopMenuListAdapter
import moe.feng.aquabutton.util.VoicePlayer
import moe.feng.common.eventshelper.EventsHelper

class MainActivity : BaseActivity(R.layout.activity_main), MainUiEventCallback {

    companion object {

        private const val TOP_MENU_STATE_COLLAPSED = 0
        private const val TOP_MENU_STATE_EXPANDED = 1
        private const val TOP_MENU_STATE_SEARCH = 2

        private const val KEY_STATES = "state:STATES"

    }

    @Parcelize
    private class State(
        var topMenuState: Int = TOP_MENU_STATE_COLLAPSED,
        var voiceData: List<VoiceCategory> = emptyList(),
        var searchKeyword: String? = null,
        var searchResult: List<VoiceItem>? = null
    ) : Parcelable {

        val currentCategory: VoiceCategory? get() = voiceData.find { it.selected }

    }

    private lateinit var state: State
    private var searchJob: Job? = null

    private val topMenuAdapter: TopMenuListAdapter = TopMenuListAdapter(
        onVoiceCategoryItemClick = this::onVoiceCategoryItemClick
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTransparentUi()

        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayShowCustomEnabled(true)
        }

        topMenuList.adapter = topMenuAdapter
        searchEdit.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val keyword = searchEdit.text?.toString()
                if (keyword.isNullOrEmpty()) {
                    showSnackbar(textRes = R.string.tips_input_search_keyword)
                } else {
                    searchEdit.hideKeyboard()
                    doSearch(keyword)
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        homeButton.setOnClickListener { toggleCategoryMenu() }

        state = savedInstanceState?.getParcelable(KEY_STATES) ?: State()

        if (state.voiceData.isEmpty()) {
            launch {
                state.voiceData = AquaAssetsApi.getVoices()
                state.voiceData.firstOrNull()?.selected = true

                topMenuAdapter.items = state.voiceData
                topMenuAdapter.notifyDataSetChanged()
                setupContentFragment()
            }
        } else {
            topMenuAdapter.items = state.voiceData
            setupContentFragment()
        }

        updateTopMenuStates()
        setupContentFragment()
        EventsHelper.getInstance(this).registerListener(this)
    }

    override fun onStart() {
        super.onStart()
        VoicePlayer.init()
    }

    override fun onStop() {
        super.onStop()
        VoicePlayer.release()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventsHelper.getInstance(this).unregisterListener(this)
    }

    override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets {
        statusBackground.updateLayoutParams { height = insets.systemWindowInsetTop }
        return super.onApplyWindowInsets(insets)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_STATES, state)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                if (state.voiceData.isEmpty()) {
                    showSnackbar(textRes = R.string.tips_op_requires_load)
                    return true
                }
                if (state.topMenuState != TOP_MENU_STATE_SEARCH) {
                    updateTopMenuStates(newState = TOP_MENU_STATE_SEARCH, animate = true)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showErrorTextOnSnackbar(text: String) {
        showSnackbar(text)
    }

    override fun toggleCategoryMenu() {
        if (state.topMenuState != TOP_MENU_STATE_COLLAPSED) {
            updateTopMenuStates(newState = TOP_MENU_STATE_COLLAPSED, animate = true)
        } else {
            if (state.voiceData.isEmpty()) {
                showSnackbar(textRes = R.string.tips_op_requires_load)
                return
            }
            updateTopMenuStates(newState = TOP_MENU_STATE_EXPANDED, animate = true)
        }
    }

    private fun showSnackbar(text: String? = null,
                             textRes: Int = 0,
                             duration: Int = Snackbar.LENGTH_SHORT) {
        Snackbar.make(rootView, text ?: getString(textRes), duration).show()
    }

    private fun updateTopMenuStates(newState: Int? = null, animate: Boolean = false) {
        val lastState = state.topMenuState
        if (newState != null) {
            state.topMenuState = newState
        }
        println("topMenuState = ${state.topMenuState}")
        when (state.topMenuState) {
            TOP_MENU_STATE_COLLAPSED -> {
                homeTitle.setText(R.string.app_name)
                homeButton.setImageResource(
                    if (animate && lastState != TOP_MENU_STATE_COLLAPSED)
                        R.drawable.ic_anim_close_to_haze else R.drawable.ic_dehaze_24
                )
                topMenuContainer.updateLayoutParams { height = 0 }
                searchEdit.hideKeyboard()
            }
            TOP_MENU_STATE_EXPANDED -> {
                homeTitle.setText(R.string.app_name)
                homeButton.setImageResource(
                    if (animate && lastState == TOP_MENU_STATE_COLLAPSED)
                        R.drawable.ic_anim_haze_to_close else R.drawable.ic_close_24
                )
                topMenuContainer.updateLayoutParams { height = ViewGroup.LayoutParams.WRAP_CONTENT }
                topMenuList.isVisible = true
                searchContainer.isGone = true
                searchEdit.hideKeyboard()
            }
            TOP_MENU_STATE_SEARCH -> {
                homeTitle.setText(R.string.action_search)
                homeButton.setImageResource(
                    if (animate && lastState == TOP_MENU_STATE_COLLAPSED)
                        R.drawable.ic_anim_haze_to_close else R.drawable.ic_close_24
                )
                topMenuContainer.updateLayoutParams { height = ViewGroup.LayoutParams.WRAP_CONTENT }
                topMenuList.isGone = true
                searchContainer.isVisible = true
                searchEdit.setText("")
            }
        }
        if (animate) {
            TransitionManager.beginDelayedTransition(rootView, AutoTransition().apply {
                duration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
            })
            (homeButton.drawable as? Animatable)?.start()
        }
    }

    private fun onVoiceCategoryItemClick(item: VoiceCategory) {
        state.voiceData.selectItem(item)
        state.searchKeyword = null
        searchJob?.cancel()
        updateTopMenuStates(newState = TOP_MENU_STATE_COLLAPSED, animate = true)
        setupContentFragment()
    }

    private fun setupContentFragment() {
        supportFragmentManager.commit {
            setCustomAnimations(R.anim.slide_fade_in, R.anim.slide_fade_out)

            val fragment = supportFragmentManager.findFragmentById(R.id.contentFrame)
            state.searchKeyword?.let { keyword ->
                state.searchResult?.let { result ->
                    val showedKeyword = (fragment as? SearchResultListFragment)?.keyword
                    if (keyword != showedKeyword) {
                        replace(R.id.contentFrame, SearchResultListFragment(keyword, result))
                    }
                } ?: run {
                    if (fragment !is LoadingFragment) {
                        replace(R.id.contentFrame, LoadingFragment())
                    }
                }
            } ?: state.currentCategory?.let { category ->
                val showedCategory = (fragment as? CategoryListFragment)?.category
                if (category.name != showedCategory?.name) {
                    replace(R.id.contentFrame, CategoryListFragment(category))
                }
            } ?: run {
                if (fragment !is LoadingFragment) {
                    replace(R.id.contentFrame, LoadingFragment())
                }
            }
        }
    }

    private fun doSearch(keyword: String) {
        searchJob?.cancel()
        searchJob = launch {
            if (state.searchKeyword == keyword &&
                state.searchResult?.isNotEmpty() == true) {
                return@launch
            }
            with (state) {
                searchKeyword = keyword
                searchResult = null
                voiceData.selectItem(null)
            }
            whenCreated {
                setupContentFragment()
            }
            val result = withContext(Dispatchers.Default) {
                state.voiceData.asSequence()
                    .flatMap { it.voiceList.asSequence() }
                    .filter {
                        it.description()?.contains(keyword, ignoreCase = true) == true
                    }
                    .toList()
            }
            state.searchResult = result
            whenCreated {
                setupContentFragment()
            }
        }
    }

    fun clearSearchResult() {
        with (state) {
            voiceData.selectItem(voiceData.firstOrNull())
            searchKeyword = null
            searchResult = null
        }
        topMenuAdapter.notifyDataSetChanged()
        setupContentFragment()
        updateTopMenuStates(TOP_MENU_STATE_COLLAPSED)
    }

}