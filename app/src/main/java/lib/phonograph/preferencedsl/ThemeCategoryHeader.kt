/*
 * Copyright (c) 2022 chr_56
 */

package lib.phonograph.preferencedsl

import de.Maxr1998.modernpreferences.Preference
import de.Maxr1998.modernpreferences.PreferenceScreen
import de.Maxr1998.modernpreferences.PreferencesAdapter
import mt.pref.primaryColor
import android.annotation.SuppressLint

class ThemeCategoryHeader(key: String) : Preference(key) {

    @SuppressLint("ResourceType")
    override fun getWidgetLayoutResource() = -2

    override fun bindViews(holder: PreferencesAdapter.ViewHolder) {
        super.bindViews(holder)
        holder.title.apply {
            setTextColor(context.primaryColor())
        }
    }
}

inline fun PreferenceScreen.Appendable.categoryHeaderColored(key: String, block: ThemeCategoryHeader.() -> Unit): ThemeCategoryHeader {
    return ThemeCategoryHeader(key).apply(block).also(::addPreferenceItem)
}