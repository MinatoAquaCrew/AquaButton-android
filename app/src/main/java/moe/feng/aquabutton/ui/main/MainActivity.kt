package moe.feng.aquabutton.ui.main

import android.content.Intent
import android.content.res.Configuration
import android.graphics.drawable.Animatable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.inputmethod.EditorInfo
import android.webkit.MimeTypeMap
import androidx.appcompat.app.messageRes
import androidx.appcompat.app.okButton
import androidx.appcompat.app.showAlertDialog
import androidx.appcompat.app.titleRes
import androidx.appcompat.widget.TooltipCompat
import androidx.coordinatorlayout.widget.CoordinatorLayout
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
import moe.feng.aquabutton.util.FileUtils
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
    private var saveJob: Job? = null

    private val topMenuAdapter: TopMenuListAdapter = TopMenuListAdapter(
        onVoiceCategoryItemClick = this::onVoiceCategoryItemClick
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTransparentUi(lightNavBar = true)

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
        homeButton.onClick { toggleCategoryMenu() }
        shuffleButton.onClick { playShuffleVoice() }
        TooltipCompat.setTooltipText(shuffleButton, getString(R.string.action_shuffle))

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

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        try {
            setTransparentUi()
        } catch (ignored: Exception) {
        }
    }

    override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets {
        statusBackground.updateLayoutParams { height = insets.systemWindowInsetTop }
        bottomNavBackground.updateLayoutParams { height = insets.systemWindowInsetBottom }
        shuffleButton.updateLayoutParams<CoordinatorLayout.LayoutParams> {
            bottomMargin = resources.getDimensionPixelSize(R.dimen.fab_margin_bottom) +
                    insets.systemWindowInsetBottom
        }
        if (insets.systemWindowInsetBottom > 0) {
            if (!hideNavigation) setTransparentUi(hideNavigation = true)
        } else {
            if (hideNavigation) setTransparentUi(hideNavigation = false)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (insets.systemGestureInsets.bottom != insets.tappableElementInsets.bottom) {
                bottomNavBackground.alpha = 0F
            } else {
                bottomNavBackground.alpha = 1F
            }
        } else {
            bottomNavBackground.alpha = 1F
        }
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

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.findItem(R.id.action_search).isVisible = state.topMenuState != TOP_MENU_STATE_SEARCH
        return super.onPrepareOptionsMenu(menu)
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

    override fun requestSaveVoice(voice: VoiceItem) {
        val mimeTypeMap = MimeTypeMap.getSingleton()
        val ext = MimeTypeMap.getFileExtensionFromUrl(voice.path)
        val mimeType = mimeTypeMap.getExtensionFromMimeType(ext) ?: "audio/*"
        val fileName = voice.description() + "." + (ext ?: "mp3")

        saveJob?.cancel()
        saveJob = launchWhenCreated {
            val uri: Uri?
            try {
                uri = FileUtils.requestNewDocumentUri(this@MainActivity, mimeType, fileName)
            } catch (e: Exception) {
                e.printStackTrace()
                showAlertDialog {
                    titleRes = R.string.os_cannot_save_title
                    messageRes = R.string.os_cannot_save_message
                    okButton()
                }
                return@launchWhenCreated
            }
            if (uri != null) {
                val saveProgressBar = showSnackbar(
                    textRes = R.string.tips_voice_saving,
                    duration = Snackbar.LENGTH_INDEFINITE,
                    actionTextRes = android.R.string.cancel,
                    onAction = {
                        AquaAssetsApi.cancelDownloadingVoice()
                        saveJob?.cancel()
                    }
                )
                val voiceFile = AquaAssetsApi.getVoice(voice)
                whenCreated { saveProgressBar.dismiss() }
                FileUtils.copyFileToUri(this@MainActivity, voiceFile, uri)
                val path = uri.toString()
                showSnackbar(
                    text = getString(R.string.tips_voice_saved, path),
                    duration = Snackbar.LENGTH_LONG,
                    actionTextRes = R.string.action_share,
                    onAction = {
                        val sendIntent = Intent(Intent.ACTION_SEND).apply {
                            putExtra(Intent.EXTRA_STREAM, uri)
                            type = mimeType
                        }
                        startActivity(Intent.createChooser(
                            sendIntent,
                            getString(R.string.share_to_title)
                        ))
                    }
                )
            }
        }
    }

    private fun showSnackbar(text: String? = null,
                             textRes: Int = 0,
                             duration: Int = Snackbar.LENGTH_SHORT,
                             actionText: String? = null,
                             actionTextRes: Int = 0,
                             onAction: (() -> Unit)? = null): Snackbar {
        val bar = Snackbar.make(rootView, text ?: getString(textRes), duration)
        if (onAction != null) {
            val actionTextValue = actionText
                ?: actionTextRes.takeIf { it != 0 }?.let(::getString)
                ?: throw IllegalArgumentException("actionText or actionTextRes cannot be empty")
            bar.setAction(actionTextValue) { onAction() }
        }
        bar.show()
        return bar
    }

    private fun updateTopMenuStates(newState: Int? = null, animate: Boolean = false) {
        val lastState = state.topMenuState
        if (newState != null) {
            state.topMenuState = newState
        }
        when (state.topMenuState) {
            TOP_MENU_STATE_COLLAPSED -> {
                homeLogo.isVisible = true
                homeTitle.isGone = true
                homeButton.setImageResource(
                    if (animate && lastState != TOP_MENU_STATE_COLLAPSED)
                        R.drawable.ic_anim_close_to_haze else R.drawable.ic_dehaze_24
                )
                topMenuContainer.updateLayoutParams { height = 0 }
                searchEdit.hideKeyboard()
            }
            TOP_MENU_STATE_EXPANDED -> {
                homeLogo.isGone = true
                homeTitle.isVisible = true
                homeTitle.setText(R.string.choose_category_title)
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
                homeLogo.isGone = true
                homeTitle.isVisible = true
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
            TransitionManager.beginDelayedTransition(rootLinearLayout, AutoTransition().apply {
                duration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
            })
            (homeButton.drawable as? Animatable)?.start()
        }
        invalidateOptionsMenu()
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

    private fun playShuffleVoice() = launchWhenResumed {
        if (state.voiceData.isEmpty()) {
            showSnackbar(textRes = R.string.tips_op_requires_load)
            return@launchWhenResumed
        }
        val luckyCategory = state.voiceData.random()
        val luckyVoice = luckyCategory.voiceList.random()
        showSnackbar(
            text = getString(R.string.tips_shuffle_result,
                luckyCategory.description(), luckyVoice.description()),
            actionTextRes = R.string.action_stop,
            onAction = {
                VoicePlayer.stop()
            },
            duration = Snackbar.LENGTH_LONG
        )
        VoicePlayer.stop()
        VoicePlayer.play(AquaAssetsApi.getVoice(luckyVoice))
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