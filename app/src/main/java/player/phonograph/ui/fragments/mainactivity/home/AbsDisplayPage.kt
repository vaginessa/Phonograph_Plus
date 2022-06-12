/*
 * Copyright (c) 2022 chr_56
 */

package player.phonograph.ui.fragments.mainactivity.home

import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.RadioButton
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import player.phonograph.App
import player.phonograph.BuildConfig
import player.phonograph.R
import player.phonograph.adapter.display.DisplayAdapter
import player.phonograph.databinding.FragmentDisplayPageBinding
import player.phonograph.databinding.PopupWindowMainBinding
import player.phonograph.interfaces.Displayable
import player.phonograph.util.PhonographColorUtil
import player.phonograph.util.Util
import player.phonograph.util.ViewUtil
import player.phonograph.util.ViewUtil.setUpFastScrollRecyclerViewColor
import util.mdcolor.pref.ThemeColor
import java.lang.ref.WeakReference

/**
 * @param IT the model type that this fragment displays
 * @param A relevant Adapter
 * @param LM relevant LayoutManager
 */
sealed class AbsDisplayPage<IT, A : DisplayAdapter<out Displayable>, LM : GridLayoutManager> :
    AbsPage() {

    private var _viewBinding: FragmentDisplayPageBinding? = null
    private val binding get() = _viewBinding!!

    abstract fun getDataSet(): List<IT>
    abstract fun loadDataSet()

    /**
     * Notify every [Displayable] items changes, do not reload dataset
     */
    abstract fun refreshDataSet()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        loadDataSet()
        _viewBinding = FragmentDisplayPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    // for mini player bar
    private var outerAppbarOffsetListener =
        AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            binding.container.setPadding(
                binding.container.paddingLeft,
                binding.container.paddingTop,
                binding.container.paddingRight,
                hostFragment.totalAppBarScrollingRange + verticalOffset
            )
        }

    private var innerAppbarOffsetListener =
        AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            binding.container.setPadding(
                binding.container.paddingLeft,
                binding.innerAppBar.totalScrollRange + verticalOffset,
                binding.container.paddingRight,
                binding.container.paddingBottom

            )
        }

//    protected abstract fun

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.empty.text = resources.getText(R.string.loading)

        initRecyclerView()
//        _bindingPopup = PopupWindowMainBinding.inflate(LayoutInflater.from(hostFragment.mainActivity))
        initAppBar()
    }

    protected lateinit var adapter: A
    protected lateinit var layoutManager: LM

    protected abstract fun initLayoutManager(): LM
    protected abstract fun initAdapter(): A

    protected var isRecyclerViewPrepared: Boolean = false

    private lateinit var adapterDataObserver: RecyclerView.AdapterDataObserver

    private fun initRecyclerView() {

        layoutManager = initLayoutManager()
        adapter = initAdapter()

        adapterDataObserver = object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                checkEmpty()
                updateHeaderText()
            }
        }
        adapter.registerAdapterDataObserver(adapterDataObserver)

        binding.recyclerView.setUpFastScrollRecyclerViewColor(
            hostFragment.mainActivity,
            ThemeColor.accentColor(App.instance.applicationContext)
        )
        binding.recyclerView.also {
            it.adapter = adapter
            it.layoutManager = layoutManager
        }
        isRecyclerViewPrepared = true
    }

    private fun initAppBar() {

        binding.innerAppBar.setExpanded(false)
        binding.innerAppBar.addOnOffsetChangedListener(innerAppbarOffsetListener)
        val actionDrawable = AppCompatResources.getDrawable(
            hostFragment.mainActivity,
            R.drawable.ic_sort_variant_white_24dp
        )
        actionDrawable?.colorFilter = BlendModeColorFilterCompat
            .createBlendModeColorFilterCompat(
                binding.textPageHeader.currentTextColor,
                BlendModeCompat.SRC_IN
            )
        binding.buttonPageHeader.setImageDrawable(actionDrawable)
        binding.buttonPageHeader.setOnClickListener { onPopupShow() }

        hostFragment.addOnAppBarOffsetChangedListener(outerAppbarOffsetListener)
    }

    // all pages share/re-used one popup on host fragment
    private val popupWindow: PopupWindow
        get() {
            if (hostFragment.displayPopup.get() == null) {
                hostFragment.displayPopup = WeakReference(createPopup())
            }
            return hostFragment.displayPopup.get()!!
        }
    private val _bindingPopup: PopupWindowMainBinding?
        get() {
            if (hostFragment.displayPopupView.get() == null)
                hostFragment.displayPopupView =
                    WeakReference(PopupWindowMainBinding.inflate(LayoutInflater.from(hostFragment.mainActivity)))
            return hostFragment.displayPopupView.get()!!
        }
    private val popupView get() = _bindingPopup!!

    private fun createPopup(): PopupWindow {
        val mainActivity = hostFragment.mainActivity // context

        // todo move to util or view model
        val accentColor = ThemeColor.accentColor(mainActivity)
        val textColor = ThemeColor.textColorSecondary(mainActivity)
        val widgetColor = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_enabled),
                intArrayOf(android.R.attr.state_selected),
                intArrayOf()
            ),
            intArrayOf(accentColor, accentColor, textColor)
        )
        //
        // init content color
        //
        popupView.apply {
            // text color
            this.textGridSize.setTextColor(accentColor)
            this.textSortOrderBasic.setTextColor(accentColor)
            this.textSortOrderContent.setTextColor(accentColor)
            // checkbox color
            this.actionColoredFooters.buttonTintList = widgetColor
            // radioButton
            for (i in 0 until this.gridSize.childCount) (this.gridSize.getChildAt(i) as RadioButton).buttonTintList =
                widgetColor
            for (i in 0 until this.sortOrderContent.childCount) (this.sortOrderContent.getChildAt(i) as RadioButton).buttonTintList =
                widgetColor
            for (i in 0 until this.sortOrderBasic.childCount) (this.sortOrderBasic.getChildAt(i) as RadioButton).buttonTintList =
                widgetColor
        }

        return PopupWindow(
            popupView.root,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        ).apply {
            this.animationStyle = android.R.style.Animation_Dialog
            this.setBackgroundDrawable(
                ColorDrawable(
                    PhonographColorUtil.getCorrectBackgroundColor(
                        mainActivity
                    )
                )
            )
        }
    }

    protected fun resetPopupMenuBackgroundColor() {
        popupWindow.setBackgroundDrawable(
            ColorDrawable(
                PhonographColorUtil.getCorrectBackgroundColor(
                    hostFragment.mainActivity
                )
            )
        )
    }

    private fun onPopupShow() {
        // first, hide all items
        hideAllPopupItems()

        // display available items
        configPopup(popupWindow, popupView)
        popupWindow.setOnDismissListener(initOnDismissListener(popupWindow, popupView))

        // show popup
        popupWindow.showAtLocation(
            binding.root, Gravity.TOP or Gravity.END, 0,
            (
                hostFragment.mainActivity.findViewById<player.phonograph.views.StatusBarView>(R.id.status_bar)?.height
                    ?: 8
                ) +
                hostFragment.totalHeaderHeight + binding.innerAppBar.height
        )

        // then valid background color
        resetPopupMenuBackgroundColor()
    }

    protected fun hideAllPopupItems() {
        popupView.sortOrderBasic.visibility = View.GONE
        popupView.sortOrderBasic.clearCheck()
        popupView.textSortOrderBasic.visibility = View.GONE

        popupView.sortOrderContent.visibility = View.GONE
        popupView.sortOrderContent.clearCheck()
        popupView.textSortOrderContent.visibility = View.GONE

        popupView.textGridSize.visibility = View.GONE
        popupView.gridSize.clearCheck()
        popupView.gridSize.visibility = View.GONE

        popupView.actionColoredFooters.visibility = View.GONE
    }

    private fun initOnDismissListener(
        popupMenu: PopupWindow,
        popup: PopupWindowMainBinding,
    ): PopupWindow.OnDismissListener {
        val displayUtil = DisplayUtil(this)

        return PopupWindow.OnDismissListener {

            //  Grid Size
            var gridSizeSelected = 0
            for (i in 0 until displayUtil.maxGridSize) {
                if ((popup.gridSize.getChildAt(i) as RadioButton).isChecked) {
                    gridSizeSelected = i + 1
                    break
                }
            }

            if (gridSizeSelected > 0 && gridSizeSelected != displayUtil.gridSize) {

                displayUtil.gridSize = gridSizeSelected
                val itemLayoutRes =
                    if (gridSizeSelected > displayUtil.maxGridSizeForList) R.layout.item_grid else R.layout.item_list

                if (adapter.layoutRes != itemLayoutRes) {
                    loadDataSet()
                    initRecyclerView() // again
                }
                layoutManager.spanCount = gridSizeSelected
            }

            if (this !is GenrePage) {
                // color footer
                val coloredFootersSelected = popup.actionColoredFooters.isChecked
                if (displayUtil.colorFooter != coloredFootersSelected) {
                    displayUtil.colorFooter = coloredFootersSelected
                    adapter.usePalette = coloredFootersSelected
                    refreshDataSet()
                }
            }

            // sort order
            saveSortOrderImpl(displayUtil, popupMenu, popup)
        }
    }

    abstract protected fun saveSortOrderImpl(
        displayUtil: DisplayUtil,
        popupMenu: PopupWindow,
        popup: PopupWindowMainBinding,
    )

    private fun configPopup(popupMenu: PopupWindow, popup: PopupWindowMainBinding) {
        val displayUtil = DisplayUtil(this)

        // grid size
        popup.textGridSize.visibility = View.VISIBLE
        popup.gridSize.visibility = View.VISIBLE
        if (Util.isLandscape(resources)) popup.textGridSize.text = resources.getText(R.string.action_grid_size_land)
        val current = displayUtil.gridSize
        val max = displayUtil.maxGridSize
        for (i in 0 until max) popup.gridSize.getChildAt(i).visibility = View.VISIBLE
        popup.gridSize.clearCheck()
        (popup.gridSize.getChildAt(current - 1) as RadioButton).isChecked = true

        // color footer
        if (this !is GenrePage) { // Genre Page never colored
            popup.actionColoredFooters.visibility = View.VISIBLE
            popup.actionColoredFooters.isChecked = displayUtil.colorFooter
            popup.actionColoredFooters.isEnabled = displayUtil.gridSize > displayUtil.maxGridSizeForList
        }

        // sort order

        // clear existed
        popup.sortOrderBasic.visibility = View.VISIBLE
        popup.textSortOrderBasic.visibility = View.VISIBLE
        popup.sortOrderContent.visibility = View.VISIBLE
        popup.textSortOrderContent.visibility = View.VISIBLE
        for (i in 0 until popup.sortOrderContent.childCount) popup.sortOrderContent.getChildAt(i).visibility = View.GONE

        setupSortOrderImpl(displayUtil, popupMenu, popup)
    }

    abstract protected fun setupSortOrderImpl(
        displayUtil: DisplayUtil,
        popupMenu: PopupWindow,
        popup: PopupWindowMainBinding
    )

    protected open val emptyMessage: Int @StringRes get() = R.string.empty
    protected fun checkEmpty() {
        if (isRecyclerViewPrepared) {
            binding.empty.setText(emptyMessage)
            binding.empty.visibility = if (getDataSet().isEmpty()) View.VISIBLE else View.GONE
        }
    }

    protected fun updateHeaderText() {
        binding.textPageHeader.text = getHeaderText()
    }

    protected abstract fun getHeaderText(): CharSequence

    override fun onMediaStoreChanged() {
        loadDataSet()
        super.onMediaStoreChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter.unregisterAdapterDataObserver(adapterDataObserver)

        binding.innerAppBar.addOnOffsetChangedListener(innerAppbarOffsetListener)
        hostFragment.removeOnAppBarOffsetChangedListener(outerAppbarOffsetListener)
        _viewBinding = null
    }

    protected val loaderCoroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onResume() {
        super.onResume()
        if (BuildConfig.DEBUG) Log.v("Metrics", "${System.currentTimeMillis().mod(10000000)} AbsDisplayPage.onResume()")
    }
}
