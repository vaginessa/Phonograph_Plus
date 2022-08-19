/*
 * Copyright (c) 2022 chr_56 & Abou Zeid (kabouzeid) (original author)
 */

package player.phonograph.dialogs

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Html
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import player.phonograph.R
import player.phonograph.misc.SAFCallbackHandlerActivity
import player.phonograph.model.playlist.FilePlaylist
import player.phonograph.model.playlist.Playlist
import player.phonograph.model.playlist.ResettablePlaylist
import player.phonograph.model.playlist.SmartPlaylist
import util.mdcolor.pref.ThemeColor
import util.phonograph.m3u.PlaylistsManager

class ClearPlaylistDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val playlists: List<Playlist> = requireArguments().getParcelableArrayList(KEY)!!

        // classify
        val smartLists = ArrayList<SmartPlaylist>()
        val filesLists = ArrayList<FilePlaylist>()
        for (playlist in playlists) {
            when (playlist) {
                is FilePlaylist -> {
                    filesLists.add(playlist)
                }
                is SmartPlaylist -> {
                    if (playlist is ResettablePlaylist) smartLists.add(playlist)
                }
            }
        }

        // generate msg
        val message =
            StringBuilder().append(
                resources.getQuantityString(R.plurals.msg_header_delete_items, playlists.size)
            ).append("<br />")

        if (filesLists.isNotEmpty()) {
            message.append(
                resources.getQuantityString(R.plurals.item_playlists, filesLists.size)
            ).append("<br />")
            for (playlist in filesLists) {
                message.append("* <b>${playlist.name}</b>").append("<br />")
            }
        }
        if (smartLists.isNotEmpty()) {
            message.append(
                resources.getQuantityString(R.plurals.item_playlists_generated, smartLists.size)
            ).append("<br />")
            for (playlist in smartLists) {
                message.append("* <b>${playlist.name}</b>").append("<br />")
            }
        }

        // extra permission check on R(11)
        val hasPermission =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R &&
                requireActivity().checkSelfPermission(Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.i(TAG, "No MANAGE_EXTERNAL_STORAGE Permission")
                message.append("<br />")
                    .append(requireActivity().resources.getString(R.string.permission_manage_external_storage_denied)) //todo res
                false
            } else {
                true
            }


        // build dialog
        val dialog = MaterialDialog(requireActivity())
            .title(R.string.delete_action)
            .message(text = Html.fromHtml(message.toString(), Html.FROM_HTML_MODE_LEGACY))
            .negativeButton(android.R.string.cancel) { dismiss() }
            .positiveButton(R.string.delete_action) {
                it.dismiss()
                // smart
                smartLists.forEach { playlist ->
                    if (playlist is ResettablePlaylist) playlist.clear(requireContext())
                }
                // files
                val attachedActivity: Activity = requireActivity()
                PlaylistsManager(
                    attachedActivity, if (attachedActivity is SAFCallbackHandlerActivity) attachedActivity else null
                )
                    .deletePlaylistWithGuide(filesLists)
            }.also {
                // grant permission button for R
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !hasPermission) {
                    it.neutralButton(R.string.grant_permission) {
                        val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                            data = Uri.parse("package:${requireContext().packageName}")
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        startActivity(intent)
                    }
                }
                // set button color
                it.getActionButton(WhichButton.POSITIVE).updateTextColor(ThemeColor.accentColor(requireContext()))
                it.getActionButton(WhichButton.NEGATIVE).updateTextColor(ThemeColor.accentColor(requireContext()))
                it.getActionButton(WhichButton.NEUTRAL).updateTextColor(ThemeColor.accentColor(requireContext()))
            }

        return dialog
    }

    companion object {
        private const val KEY = "playlists"
        private const val TAG = "ClearPlaylistDialog"

        @JvmStatic
        fun create(playlists: List<Playlist>): ClearPlaylistDialog =
            ClearPlaylistDialog().apply {
                arguments = Bundle().apply { putParcelableArrayList("playlists", ArrayList(playlists)) }
            }
    }
}
