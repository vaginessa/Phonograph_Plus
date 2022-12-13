/*
 * Copyright (c) 2022 chr_56
 */

package player.phonograph.ui.modules.settings

import de.Maxr1998.modernpreferences.PreferenceScreen
import de.Maxr1998.modernpreferences.helpers.screen
import lib.phonograph.preferencedsl.categoryHeaderColored
import lib.phonograph.preferencedsl.switchColored
import player.phonograph.R
import player.phonograph.settings.Setting
import android.content.Context

fun setupPreferenceScreen(context: Context): PreferenceScreen = screen(context) {
    categoryHeaderColored("pref_header_notification") {
        titleRes = R.string.pref_header_notification
    }
    switchColored(Setting.CLASSIC_NOTIFICATION) {
        collapseIcon = true
        titleRes = R.string.pref_title_classic_notification
        summaryRes = R.string.pref_summary_classic_notification
        defaultValue = false
    }
    switchColored(Setting.COLORED_NOTIFICATION) {
        collapseIcon = true
        titleRes = R.string.pref_title_colored_notification
        summaryRes = R.string.pref_summary_colored_notification
        defaultValue = true
        dependency = Setting.CLASSIC_NOTIFICATION
    }
}