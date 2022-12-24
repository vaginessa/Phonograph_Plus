package player.phonograph.ui.modules.settings.subdialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.afollestad.materialdialogs.customview.customView
import com.heinrichreimersoftware.materialintro.view.InkPageIndicator
import mt.pref.ThemeColor
import player.phonograph.R
import player.phonograph.model.NowPlayingScreen
import player.phonograph.util.preferences.NowPlayingScreenConfig
import player.phonograph.util.ViewUtil

/**
 * @author Karim Abou Zeid (kabouzeid)
 */
class NowPlayingScreenPreferenceDialog : DialogFragment(), OnPageChangeListener {
    private val accentColor get() = ThemeColor.accentColor(requireActivity())
    private var viewPagerPosition = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = layoutInflater.inflate(R.layout.preference_dialog_now_playing_screen, null)
        val viewPager: ViewPager =
            view.findViewById<ViewPager?>(R.id.now_playing_screen_view_pager)
                .also {
                    it.adapter = NowPlayingScreenAdapter(requireContext())
                    it.addOnPageChangeListener(this)
                    it.pageMargin = ViewUtil.convertDpToPixel(32f, it.resources).toInt()
                    it.currentItem = NowPlayingScreenConfig.nowPlayingScreen.ordinal
                }
        val pageIndicator: InkPageIndicator =
            view.findViewById<InkPageIndicator?>(R.id.page_indicator)
                .apply {
                    setViewPager(viewPager)
                    onPageSelected(viewPager.currentItem)
                }
        return MaterialDialog(requireContext())
            .title(R.string.pref_title_now_playing_screen_appearance)
            .positiveButton(android.R.string.ok) {
                NowPlayingScreenConfig.nowPlayingScreen =
                    NowPlayingScreen.values()[viewPagerPosition]
            }
            .negativeButton(android.R.string.cancel)
            .customView(view = view, dialogWrapContent = false)
            .apply {
                getActionButton(WhichButton.POSITIVE).updateTextColor(accentColor)
                getActionButton(WhichButton.NEGATIVE).updateTextColor(accentColor)
            }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
    override fun onPageSelected(position: Int) {
        viewPagerPosition = position
    }

    override fun onPageScrollStateChanged(state: Int) {}

    private class NowPlayingScreenAdapter(private val context: Context) : PagerAdapter() {
        override fun instantiateItem(collection: ViewGroup, position: Int): Any {
            val nowPlayingScreen = NowPlayingScreen.values()[position]
            val layoutInflater = LayoutInflater.from(context)
            val layout: ViewGroup =
                layoutInflater.inflate(
                    R.layout.preference_now_playing_screen_item,
                    collection,
                    false
                ) as ViewGroup
            layout.findViewById<ImageView>(R.id.image).apply {
                setImageResource(nowPlayingScreen.drawableResId)
            }
            layout.findViewById<TextView>(R.id.title).apply {
                setText(nowPlayingScreen.titleRes)
            }
            collection.addView(layout)
            return layout
        }

        override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
            collection.removeView(view as View)
        }

        override fun getCount(): Int = NowPlayingScreen.values().size

        override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`

        override fun getPageTitle(position: Int): CharSequence =
            context.getString(NowPlayingScreen.values()[position].titleRes)
    }

    companion object {
        fun newInstance(): NowPlayingScreenPreferenceDialog = NowPlayingScreenPreferenceDialog()
    }
}
