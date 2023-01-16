/*
 * Copyright (c) 2022 chr_56
 */

package player.phonograph.actions.menu

import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.github.chr56.android.menu_dsl.attach
import com.github.chr56.android.menu_dsl.menuItem
import mt.pref.ThemeColor
import player.phonograph.R
import player.phonograph.actions.*
import player.phonograph.model.playlist.FilePlaylist
import player.phonograph.model.playlist.Playlist
import player.phonograph.model.playlist.PlaylistType
import player.phonograph.model.playlist.ResettablePlaylist
import player.phonograph.model.playlist.SmartPlaylist
import player.phonograph.notification.ErrorNotification
import player.phonograph.settings.Setting
import player.phonograph.util.ImageUtil.getTintedDrawable
import androidx.annotation.ColorInt
import android.content.Context
import android.view.Menu
import android.view.MenuItem

fun playlistToolbar(
    menu: Menu,
    context: Context,
    playlist: Playlist,
    @ColorInt iconColor: Int,
    enterEditMode: () -> Unit,
    refresh: () -> Unit,
) =
    context.run {
        attach(menu) {
            menuItem {
                title = getString(R.string.action_play)
                icon = getTintedDrawable(R.drawable.ic_play_arrow_white_24dp, iconColor)
                showAsActionFlag = MenuItem.SHOW_AS_ACTION_ALWAYS
                onClick { playlist.actionPlay(context) }
            }
            menuItem {
                title = getString(R.string.action_shuffle_playlist)
                icon = getTintedDrawable(R.drawable.ic_shuffle_white_24dp, iconColor)
                showAsActionFlag = MenuItem.SHOW_AS_ACTION_ALWAYS
                onClick { playlist.actionShuffleAndPlay(context) }
            }
            menuItem {
                title = getString(R.string.action_play_next)
                icon = getTintedDrawable(R.drawable.ic_redo_white_24dp, iconColor)
                showAsActionFlag = MenuItem.SHOW_AS_ACTION_IF_ROOM
                onClick { playlist.actionPlayNext(context) }
            }

            menuItem {
                title = getString(R.string.refresh)
                icon = getTintedDrawable(R.drawable.ic_refresh_white_24dp, iconColor)
                showAsActionFlag = MenuItem.SHOW_AS_ACTION_IF_ROOM
                onClick {
                    refresh()
                    true
                }
            }
            menuItem {
                title = getString(R.string.action_add_to_playing_queue)
                showAsActionFlag = MenuItem.SHOW_AS_ACTION_NEVER
                onClick { playlist.actionAddToCurrentQueue(context) }
            }
            menuItem {
                title = getString(R.string.action_add_to_playlist)
                showAsActionFlag = MenuItem.SHOW_AS_ACTION_NEVER
                onClick {
                    fragmentActivity(context) {
                        playlist.actionAddToPlaylist(it)
                        true
                    }
                }
            }

            // File Playlist
            if (playlist !is SmartPlaylist) {
                menuItem {
                    title = getString(R.string.edit)
                    itemId = R.id.action_edit_playlist
                    showAsActionFlag = MenuItem.SHOW_AS_ACTION_NEVER
                    onClick {
                        if (playlist is FilePlaylist) {
                            enterEditMode()
                            true
                        } else {
                            false
                        }
                    }
                }
                menuItem {
                    title = getString(R.string.rename_action)
                    showAsActionFlag = MenuItem.SHOW_AS_ACTION_NEVER
                    onClick {
                        fragmentActivity(context) {
                            playlist.actionRenamePlaylist(it)
                            true
                        }
                    }
                }
            }

            // Resettable
            if (playlist is ResettablePlaylist) {
                menuItem {
                    title = getString(
                        if (playlist is FilePlaylist) R.string.delete_action else R.string.clear_action
                    )
                    showAsActionFlag = MenuItem.SHOW_AS_ACTION_NEVER
                    onClick {
                        fragmentActivity(context) {
                            playlist.actionDeletePlaylist(it)
                            true
                        }
                    }
                }
            }

            menuItem {
                title = getString(R.string.save_playlist_title)
                showAsActionFlag = MenuItem.SHOW_AS_ACTION_NEVER
                onClick {
                    fragmentActivity(context) {
                        playlist.actionSavePlaylist(it)
                        true
                    }
                }
            }

            // shortcut
            if (playlist.type == PlaylistType.LAST_ADDED) {
                menuItem {
                    itemId = R.id.action_setting_last_added_interval
                    title = getString(R.string.pref_title_last_added_interval)
                    icon = getTintedDrawable(R.drawable.ic_timer_white_24dp, iconColor)
                    onClick {
                        val prefValue = Setting.INTERVAL_ARRAY
                        val currentChoice = prefValue.indexOf(Setting.instance.lastAddedCutoffPref)
                        MaterialDialog(context)
                            .listItemsSingleChoice(
                                res = R.array.pref_playlists_last_added_interval_titles,
                                initialSelection = currentChoice.let { if (it == -1) 0 else it },
                                checkedColor = ThemeColor.accentColor(context)
                            ) { dialog, index, _ ->
                                try {
                                    Setting.instance.lastAddedCutoffPref = prefValue[index]
                                    refresh()
                                } catch (e: Exception) {
                                    ErrorNotification.postErrorNotification(e)
                                }
                                dialog.dismiss()
                            }
                            .title(R.string.pref_title_last_added_interval)
                            .show()
                        true
                    }
                }
            }
        }
    }

fun playlistPopupMenu(menu: Menu, context: Context, playlist: Playlist) = context.run {
    attach(menu) {
        menuItem {
            title = getString(R.string.action_play)
            onClick { playlist.actionPlay(context) }
        }
        menuItem {
            title = getString(R.string.action_play_next)
            onClick { playlist.actionPlayNext(context) }
        }
        menuItem {
            title = getString(R.string.action_add_to_playing_queue)
            onClick { playlist.actionAddToCurrentQueue(context) }
        }
        menuItem {
            title = getString(R.string.add_playlist_title)
            onClick {
                fragmentActivity(context) {
                    playlist.actionAddToPlaylist(it)
                    true
                }
            }
        }
        if (playlist is FilePlaylist) {
            menuItem {
                title = getString(R.string.rename_action)
                onClick {
                    fragmentActivity(context) {
                        playlist.actionRenamePlaylist(it)
                        true
                    }
                }
            }
        }
        if (playlist is ResettablePlaylist) {
            menuItem {
                title =
                    if (playlist is FilePlaylist) getString(R.string.delete_action)
                    else getString(R.string.clear_action)
                onClick {
                    fragmentActivity(context) {
                        playlist.actionDeletePlaylist(it)
                        true
                    }
                }
            }
        }
        menuItem {
            title = getString(R.string.save_playlist_title)
            onClick {
                fragmentActivity(context) {
                    playlist.actionSavePlaylist(it)
                    true
                }
            }
        }
    }
}

