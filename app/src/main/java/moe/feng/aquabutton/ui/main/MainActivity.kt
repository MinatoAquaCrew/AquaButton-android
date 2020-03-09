package moe.feng.aquabutton.ui.main

import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.commit
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_main.*
import moe.feng.aquabutton.R
import moe.feng.aquabutton.api.AquaAssetsApi
import moe.feng.aquabutton.model.VoiceCategory
import moe.feng.aquabutton.model.selectItem
import moe.feng.aquabutton.ui.common.BaseActivity
import moe.feng.aquabutton.ui.main.list.TopMenuListAdapter

class MainActivity : BaseActivity(R.layout.activity_main) {

    companion object {

        private const val TOP_MENU_STATE_COLLAPSED = 0
        private const val TOP_MENU_STATE_EXPANDED = 1
        private const val TOP_MENU_STATE_SEARCH = 2

        private const val KEY_STATES = "state:STATES"

    }

    @Parcelize
    private class State(
        var topMenuState: Int = TOP_MENU_STATE_COLLAPSED,
        var voiceData: List<VoiceCategory> = emptyList()
    ) : Parcelable {

        val currentCategory: VoiceCategory? get() = voiceData.find { it.selected }

    }

    private lateinit var state: State

    private val topMenuAdapter: TopMenuListAdapter = TopMenuListAdapter(
        onVoiceCategoryItemClick = this::onVoiceCategoryItemClick
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTransparentUi()

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_dehaze_24_primary_text)
        }

        topMenuList.adapter = topMenuAdapter

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
            android.R.id.home -> {
                if (state.topMenuState != TOP_MENU_STATE_COLLAPSED) {
                    updateTopMenuStates(newState = TOP_MENU_STATE_COLLAPSED, animate = true)
                } else {
                    updateTopMenuStates(newState = TOP_MENU_STATE_EXPANDED, animate = true)
                }
                return true
            }
            R.id.action_search -> {
                updateTopMenuStates(newState = TOP_MENU_STATE_SEARCH, animate = true)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateTopMenuStates(newState: Int? = null, animate: Boolean = false) {
        if (newState != null) {
            state.topMenuState = newState
        }
        when (state.topMenuState) {
            TOP_MENU_STATE_COLLAPSED -> {
                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_dehaze_24_primary_text)
                topMenuContainer.updateLayoutParams { height = 0 }
            }
            TOP_MENU_STATE_EXPANDED -> {
                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_24_primary_text)
                topMenuContainer.updateLayoutParams { height = ViewGroup.LayoutParams.WRAP_CONTENT }
                topMenuList.isVisible = true
            }
            TOP_MENU_STATE_SEARCH -> {
                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_24_primary_text)
                topMenuContainer.updateLayoutParams { height = ViewGroup.LayoutParams.WRAP_CONTENT }
                topMenuList.isGone = true
                // TODO Search Ui
            }
        }
        if (animate) {
            TransitionManager.beginDelayedTransition(rootView, AutoTransition().apply {
                duration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
            })
        }
    }

    private fun onVoiceCategoryItemClick(item: VoiceCategory) {
        state.voiceData.selectItem(item)
        updateTopMenuStates(newState = TOP_MENU_STATE_COLLAPSED, animate = true)
        setupContentFragment()
    }

    private fun setupContentFragment() {
        state.currentCategory?.let {
            val fragment = supportFragmentManager.findFragmentById(R.id.contentFrame)
            val showedCategory = (fragment as? CategoryListFragment)?.category
            if (it.name != showedCategory?.name) {
                supportFragmentManager.commit {
                    replace(R.id.contentFrame, CategoryListFragment(it))
                }
            }
        }
    }

}