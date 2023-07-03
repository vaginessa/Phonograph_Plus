/*
 *  Copyright (c) 2022~2023 chr_56
 */

package player.phonograph.repo.mediastore.loaders

import player.phonograph.model.Album
import player.phonograph.model.Song
import player.phonograph.repo.mediastore.internal.intoSongs
import player.phonograph.repo.mediastore.internal.querySongs
import player.phonograph.repo.mediastore.toAlbumList
import android.content.Context
import android.provider.MediaStore.Audio.AudioColumns

/**
 * @author Karim Abou Zeid (kabouzeid)
 */
object AlbumLoader {

    fun all(context: Context): List<Album> {
        val songs = querySongs(context, sortOrder = null).intoSongs()
        return if (songs.isEmpty()) return emptyList() else songs.toAlbumList()
    }

    fun id(context: Context, albumId: Long): Album {
        val songs = querySongs(context, "${AudioColumns.ALBUM_ID}=?", arrayOf(albumId.toString()), null).intoSongs()
        return Album(albumId, albumTitle(songs), songs.toMutableList().sortedBy { it.trackNumber })
    }

    fun searchByName(context: Context, query: String): List<Album> {
        val songs = querySongs(context, "${AudioColumns.ALBUM} LIKE ?", arrayOf("%$query%"), null).intoSongs()
        return if (songs.isEmpty()) return emptyList() else songs.toAlbumList()
    }

    private fun albumTitle(list: List<Song>): String? {
        if (list.isEmpty()) return null
        return list[0].albumName
    }

    fun List<Album>.allSongs(): List<Song> =
        this.flatMap { it.songs }
}