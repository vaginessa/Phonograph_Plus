/*
 * Copyright (c) 2022 chr_56
 */

package lib.phonograph.preferencedsl

import de.Maxr1998.modernpreferences.Preference
import de.Maxr1998.modernpreferences.PreferenceScreen
import de.Maxr1998.modernpreferences.PreferencesAdapter
import lib.phonograph.view.BorderCircleView
import mt.pref.ThemeColor
import mt.util.color.darkenColor
import player.phonograph.App
import player.phonograph.R
import util.phonograph.misc.ColorChooserListener
import androidx.fragment.app.FragmentActivity
import android.view.View

abstract class ColorSettingPreference(
    key: String,
    val activity: FragmentActivity
) : Preference(key) {
    init {
        persistent = false
    }

    override fun getWidgetLayoutResource(): Int = R.layout.x_preference_color

    private var borderCircleView: BorderCircleView? = null
    abstract val color: Int

    override fun bindViews(holder: PreferencesAdapter.ViewHolder) {
        super.bindViews(holder)
        borderCircleView = holder.widget as BorderCircleView
        invalidateColor()
    }

    private fun invalidateColor() {
        borderCircleView?.apply {
            apply {
                if (color != 0) {
                    visibility = View.VISIBLE
                    setBackgroundColor(color)
                    borderColor = darkenColor(color)
                } else {
                    visibility = View.GONE
                }
            }
        }
    }

    protected abstract val mode: Int
    override fun onClick(holder: PreferencesAdapter.ViewHolder) {
        super.onClick(holder)
        ColorChooserListener(activity, color, mode).showDialog()
    }
}

class AccentColorSettingPreference(key: String, activity: FragmentActivity) :
        ColorSettingPreference(key, activity) {
    override val color: Int get() = ThemeColor.accentColor(App.instance)
    override val mode: Int get() = ColorChooserListener.MODE_ACCENT_COLOR
}

class PrimaryColorSettingPreference(key: String, activity: FragmentActivity) :
        ColorSettingPreference(key, activity) {
    override val color: Int get() = ThemeColor.primaryColor(App.instance)
    override val mode: Int get() = ColorChooserListener.MODE_PRIMARY_COLOR
}

inline fun PreferenceScreen.Appendable.accentColorSetting(
    key: String,
    activity: FragmentActivity,
    block: AccentColorSettingPreference.() -> Unit
): AccentColorSettingPreference =
    AccentColorSettingPreference(key, activity).apply(block).also(::addPreferenceItem)

inline fun PreferenceScreen.Appendable.primaryColorSetting(
    key: String,
    activity: FragmentActivity,
    block: PrimaryColorSettingPreference.() -> Unit
): PrimaryColorSettingPreference =
    PrimaryColorSettingPreference(key, activity).apply(block).also(::addPreferenceItem)

