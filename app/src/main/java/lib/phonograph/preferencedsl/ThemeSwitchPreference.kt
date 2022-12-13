package lib.phonograph.preferencedsl

import de.Maxr1998.modernpreferences.PreferenceScreen
import de.Maxr1998.modernpreferences.PreferencesAdapter
import de.Maxr1998.modernpreferences.preferences.TwoStatePreference
import mt.pref.primaryColor
import mt.util.color.lightenColor
import mt.util.color.secondaryTextColor
import player.phonograph.R
import androidx.appcompat.widget.SwitchCompat
import android.graphics.Color

class ThemeSwitchPreference(key: String) : TwoStatePreference(key) {
    override fun getWidgetLayoutResource() = R.layout.widget_switch_compat
    override fun bindViews(holder: PreferencesAdapter.ViewHolder) {
        val primaryColor = holder.root.context.primaryColor()
        val defaultColor = holder.root.context.secondaryTextColor()
        holder.icon?.setColorFilter(primaryColor)
        (holder.widget as? SwitchCompat)?.let { switchCompat ->
            switchCompat.thumbTintList =
                generateColorStateList(primaryColor, Color.WHITE)
            switchCompat.trackTintList =
                generateColorStateList(lightenColor(primaryColor), defaultColor)
        }
        super.bindViews(holder)
    }
}

inline fun PreferenceScreen.Appendable.switchColored(key: String, block: ThemeSwitchPreference.() -> Unit): ThemeSwitchPreference {
    return ThemeSwitchPreference(key).apply(block).also(::addPreferenceItem)
}
