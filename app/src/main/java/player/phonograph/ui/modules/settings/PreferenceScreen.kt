/*
 * Copyright (c) 2022 chr_56
 */

package player.phonograph.ui.modules.settings

import com.afollestad.materialdialogs.MaterialDialog
import de.Maxr1998.modernpreferences.PreferenceScreen
import de.Maxr1998.modernpreferences.helpers.onClick
import de.Maxr1998.modernpreferences.helpers.pref
import de.Maxr1998.modernpreferences.helpers.screen
import de.Maxr1998.modernpreferences.helpers.singleChoice
import de.Maxr1998.modernpreferences.preferences.choice.SelectionItem
import lib.phonograph.localization.LanguageSettingDialog
import lib.phonograph.preferencedsl.categoryHeaderColored
import lib.phonograph.preferencedsl.switchColored
import player.phonograph.R
import player.phonograph.preferences.HomeTabConfigDialog
import player.phonograph.settings.Setting
import player.phonograph.ui.dialogs.ClickModeSettingDialog
import player.phonograph.ui.dialogs.PathFilterDialog
import player.phonograph.ui.modules.settings.subdialogs.NowPlayingScreenPreferenceDialog
import player.phonograph.util.NavigationUtil
import player.phonograph.util.preferences.HomeTabConfig
import androidx.fragment.app.FragmentActivity
import android.app.Activity
import android.content.Context

fun setupPreferenceScreen(context: Context): PreferenceScreen = screen(context) {
    titleRes = R.string.action_settings
    collapseIcon = true
    val fm = (context as FragmentActivity).supportFragmentManager
    //
    categoryHeaderColored("pref_header_appearance") {
        titleRes = R.string.pref_header_appearance
    }
    singleChoice(Setting.GENERAL_THEME, Setting.THEME_SELECTIONS) {
        titleRes = R.string.pref_title_general_theme
    }
    // switchColored(Setting.SHOULD_COLOR_NAVIGATION_BAR) {
    //     defaultValue = true
    //     persistent = false
    //     summaryRes = R.string.pref_summary_colored_navigation_bar
    //     titleRes = R.string.pref_title_navigation_bar
    // }
    // switchColored(Setting.SHOULD_COLOR_APP_SHORTCUTS) {
    //     defaultValue = true
    //     summaryRes = R.string.pref_summary_colored_app_shortcuts
    //     titleRes = R.string.pref_title_app_shortcuts
    // }
    pref(context.getString(R.string.preference_key_app_language)) {
        titleRes = R.string.app_language
        summary = "TODO" //todo
        persistent = false
        onClick {
            LanguageSettingDialog().show(fm, "LANGUAGE_SETTING_DIALOG")
            true
        }
    }
    //
    categoryHeaderColored("pref_header_library") {
        titleRes = R.string.pref_header_library
    }
    pref(context.getString(R.string.preference_key_home_tab_config)) {
        titleRes = R.string.library_categories
        summaryRes = R.string.pref_summary_library_categories
        persistent = false
        onClick {
            HomeTabConfigDialog().show(fm, "LANGUAGE_SETTING_DIALOG")
            true
        }
    } //todo
    pref("reset_home_pages_tab_config") {
        persistent = false
        titleRes = R.string.pref_title_reset_home_pages_tab_config
        summaryRes = R.string.pref_summary_reset_home_pages_tab_config
        onClick {
            MaterialDialog(context)
                .title(R.string.pref_title_reset_home_pages_tab_config)
                .message(
                    text = "${context.getString(R.string.pref_summary_reset_home_pages_tab_config)}\n" +
                            "${context.getString(R.string.are_you_sure)}\n"
                )
                .positiveButton { HomeTabConfig.resetHomeTabConfig() }
                .negativeButton { it.dismiss() }
                .show()
            true
        }
    }
    switchColored(Setting.REMEMBER_LAST_TAB) {
        defaultValue = true
        titleRes = R.string.pref_title_remember_last_tab
        summaryRes = R.string.pref_summary_remember_last_tab
    }
    switchColored(Setting.FIXED_TAB_LAYOUT) {
        defaultValue = false
        titleRes = R.string.perf_title_fixed_tab_layout
        summaryRes = R.string.pref_summary_fixed_tab_layout
    }
    //
    categoryHeaderColored("pref_head_path_filter") {
        titleRes = R.string.path_filter
    }
    pref(context.getString(R.string.preference_key_blacklist)) {
        titleRes = R.string.path_filter
        summary = "TODO" //todo
        persistent = false
        onClick {
            PathFilterDialog().show(fm, "PATH_FILTER_DIALOG")
            true
        }
    }
    //
    categoryHeaderColored("pref_header_notification") {
        titleRes = R.string.pref_header_notification
    }
    switchColored(Setting.CLASSIC_NOTIFICATION) {
        titleRes = R.string.pref_title_classic_notification
        summaryRes = R.string.pref_summary_classic_notification
        defaultValue = false
    }
    switchColored(Setting.COLORED_NOTIFICATION) {
        titleRes = R.string.pref_title_colored_notification
        summaryRes = R.string.pref_summary_colored_notification
        defaultValue = true
        dependency = Setting.CLASSIC_NOTIFICATION
    }
    //
    categoryHeaderColored("pref_header_now_playing_screen") {
        titleRes = R.string.pref_header_now_playing_screen
    }
    pref(context.getString(R.string.preference_key_now_playing_screen)) {
        titleRes = R.string.pref_title_now_playing_screen_appearance
        summary = "TODO" //todo
        persistent = false
        onClick {
            NowPlayingScreenPreferenceDialog().show(fm, "NOW_PLAYING_SCREEN")
            true
        }
    }
    switchColored(Setting.DISPLAY_LYRICS_TIME_AXIS) {
        titleRes = R.string.pref_title_display_lyrics_time_axis
        summaryRes = R.string.pref_summary_display_lyrics_time_axis
        defaultValue = true
    }
    switchColored(Setting.SYNCHRONIZED_LYRICS_SHOW) {
        titleRes = R.string.pref_title_synchronized_lyrics_show
        summaryRes = R.string.pref_summary_synchronized_lyrics_show
        defaultValue = true
    }
    //
    categoryHeaderColored("pref_header_images") {
        titleRes = R.string.pref_header_images
    }
    switchColored(Setting.IGNORE_MEDIA_STORE_ARTWORK) {
        titleRes = R.string.pref_title_ignore_media_store_artwork
        summaryRes = R.string.pref_summary_ignore_media_store_artwork
        defaultValue = false
    }
    singleChoice(
        Setting.AUTO_DOWNLOAD_IMAGES_POLICY,
        Setting.AUTO_DOWNLOAD_IMAGES_POLICY_SELECTIONS
    ) { //todo
        titleRes = R.string.pref_title_auto_download_metadata
    }
    //
    categoryHeaderColored("pref_header_lockscreen") {
        titleRes = R.string.pref_header_lockscreen
    }
    switchColored(Setting.ALBUM_ART_ON_LOCKSCREEN) {
        titleRes = R.string.pref_title_album_art_on_lockscreen
        summaryRes = R.string.pref_summary_album_art_on_lockscreen
        defaultValue = true
    }
    switchColored(Setting.BLURRED_ALBUM_ART) {
        titleRes = R.string.pref_title_blurred_album_art
        summaryRes = R.string.pref_summary_blurred_album_art
        defaultValue = false
        dependency = Setting.ALBUM_ART_ON_LOCKSCREEN
    }
    //
    categoryHeaderColored("pref_header_player_behaviour") {
        titleRes = R.string.pref_header_player_behaviour
    }
    pref(context.getString(R.string.preference_key_click_behavior)) {
        persistent = false
        titleRes = R.string.pref_title_click_behavior
        summaryRes = R.string.pref_summary_click_behavior
        onClick {
            ClickModeSettingDialog().show(fm, "CLICK_MODE_SETTING_DIALOG")
            true
        }
    }
    switchColored(Setting.AUDIO_DUCKING) {
        summaryRes = R.string.pref_summary_audio_ducking
        titleRes = R.string.pref_title_audio_ducking
        defaultValue = true
    }
    switchColored(Setting.GAPLESS_PLAYBACK) {
        summaryRes = R.string.pref_summary_gapless_playback
        titleRes = R.string.pref_title_gapless_playback
        defaultValue = false
    }
    switchColored(Setting.ENABLE_LYRICS) {
        summaryRes = R.string.pref_summary_load_lyrics
        titleRes = R.string.pref_title_load_lyrics
        defaultValue = true
    }
    switchColored(Setting.BROADCAST_SYNCHRONIZED_LYRICS) {
        summaryRes = R.string.pref_summary_send_lyrics
        titleRes = R.string.pref_title_send_lyrics
        defaultValue = true
    }
    switchColored(Setting.BROADCAST_CURRENT_PLAYER_STATE) {
        summaryRes = R.string.pref_summary_broadcast_current_player_state
        titleRes = R.string.pref_title_broadcast_current_player_state
        defaultValue = true
    }
    pref("equalizer") {
        persistent = false
        titleRes = R.string.equalizer
        onClick {
            NavigationUtil.openEqualizer(context as Activity)
            true //todo
        }
    }
    //
    categoryHeaderColored("pref_header_playlists") {
        titleRes = R.string.pref_header_playlists
    }
    singleChoice(Setting.LAST_ADDED_CUTOFF, lastAddedIntervalValues) { //todo
        titleRes = R.string.pref_title_last_added_interval
    }
    //
    categoryHeaderColored("pref_head_check_upgrade") {
        titleRes = R.string.check_upgrade
    }
    switchColored(Setting.CHECK_UPGRADE_AT_STARTUP) { //todo
        titleRes = R.string.auto_check_upgrade
        summaryRes = R.string.auto_check_upgrade_summary
        defaultValue = true
    }
    //
    categoryHeaderColored("pref_header_compatibility") {
        titleRes = R.string.pref_header_compatibility
    }
    switchColored(Setting.USE_LEGACY_FAVORITE_PLAYLIST_IMPL) {
        titleRes = R.string.pref_title_use_legacy_favorite_playlist_impl
        summaryRes = R.string.pref_summary_use_legacy_favorite_playlist_impl
        defaultValue = false
    }
    switchColored(Setting.USE_LEGACY_LIST_FILES_IMPL) {
        titleRes = R.string.use_legacy_list_Files
        defaultValue = false
    }
    switchColored(Setting.USE_LEGACY_DETAIL_DIALOG) {
        titleRes = R.string.pref_title_use_legacy_detail_dialog
        summaryRes = R.string.pref_summary_use_legacy_detail_dialog
        defaultValue = false
    }
    singleChoice(
        Setting.PLAYLIST_FILES_OPERATION_BEHAVIOUR,
        playlistFilesOperationBehaviourValues
    ) {
        titleRes = R.string.pref_title_playlist_files_operation_behaviour
        summaryRes = R.string.pref_summary_playlist_files_operation_behaviour
    }
}

// todo
val lastAddedIntervalValues: List<SelectionItem>
    get() = listOf(
        SelectionItem("today", R.string.today),
        SelectionItem("past_seven_days", R.string.past_seven_days),
        SelectionItem("past_fourteen_days", R.string.past_fourteen_days),
        SelectionItem("past_one_month", R.string.past_one_month),
        SelectionItem("past_three_months", R.string.past_three_months),
        SelectionItem("this_week", R.string.this_week),
        SelectionItem("this_month", R.string.this_month),
        SelectionItem("this_year", R.string.this_year),
    )
val playlistFilesOperationBehaviourValues: List<SelectionItem>
    get() = listOf(
        SelectionItem("auto", R.string.behaviour_auto),
        SelectionItem("force_saf", R.string.behaviour_force_saf),
        SelectionItem("force_legacy", R.string.behaviour_force_legacy),
    )
