package lib.phonograph.preferencedsl

import de.Maxr1998.modernpreferences.PreferenceScreen
import de.Maxr1998.modernpreferences.PreferencesAdapter
import de.Maxr1998.modernpreferences.preferences.TwoStatePreference
import mt.pref.primaryColor
import mt.util.color.secondaryTextColor
import player.phonograph.R
import android.widget.CompoundButton

class ThemeCheckBoxPreference(key: String) : TwoStatePreference(key) {
    override fun getWidgetLayoutResource() = R.layout.widget_checkbox_compat
    override fun bindViews(holder: PreferencesAdapter.ViewHolder) {
        val primaryColor = holder.root.context.primaryColor()
        val defaultColor = holder.root.context.secondaryTextColor()
        holder.icon?.setColorFilter(primaryColor)
        (holder.widget as? CompoundButton)?.buttonTintList =
            generateColorStateList(primaryColor, defaultColor)
        super.bindViews(holder)
    }
}

inline fun PreferenceScreen.Appendable.checkBoxColored(key: String, block: ThemeCheckBoxPreference.() -> Unit): ThemeCheckBoxPreference {
    return ThemeCheckBoxPreference(key).apply(block).also(::addPreferenceItem)
}
