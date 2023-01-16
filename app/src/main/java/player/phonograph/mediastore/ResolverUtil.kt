/*
 * Copyright (c) 2022 chr_56
 */

package player.phonograph.mediastore

import player.phonograph.model.Song
import player.phonograph.model.file.FileEntity
import player.phonograph.model.file.Location
import android.annotation.SuppressLint
import android.database.Cursor
import android.os.Build
import android.os.Build.VERSION.SDK_INT


/**
 * read [Song] from cursor safely
 */
fun Cursor?.getFirstSong(): Song {
    return this?.use {
        if (moveToFirst()) {
            parseSong(this)
        } else {
            Song.EMPTY_SONG
        }
    } ?: Song.EMPTY_SONG
}

fun Cursor?.getSongs(): List<Song> {
    val songs: MutableList<Song> = ArrayList()
    if (this != null && moveToFirst()) {
        do {
            songs.add(parseSong(this))
        } while (moveToNext())
        close()
    }
    return songs
}

/**
 * convert song cursor to [Song]
 * (**require [cursor] not empty**)
 * @see [BASE_SONG_PROJECTION]
 */
fun parseSong(cursor: Cursor): Song {
    val id = cursor.getLong(0)
    val title = cursor.getString(1)
    val trackNumber = cursor.getInt(2)
    val year = cursor.getInt(3)
    val duration = cursor.getLong(4)
    val data = cursor.getString(5)
    val dateAdded = cursor.getLong(6)
    val dateModified = cursor.getLong(7)
    val albumId = cursor.getLong(8)
    val albumName = cursor.getString(9)
    val artistId = cursor.getLong(10)
    val artistName = cursor.getString(11)
    val albumArtist =
        if (SDK_INT >= Build.VERSION_CODES.R) cursor.getString(12) else null
    return Song(
        id = id,
        title = title,
        trackNumber = trackNumber,
        year = year,
        duration = duration,
        data = data,
        dateAdded = dateAdded,
        dateModified = dateModified,
        albumId = albumId,
        albumName = albumName,
        artistId = artistId,
        artistName = artistName,
        albumArtistName = albumArtist
    )
}

/**
 * convert audio file cursor to [FileEntity]
 * (**require [cursor] not empty**)
 * @param currentLocation location where treats as base
 * @see [BASE_FILE_PROJECTION]
 */
@SuppressLint("Range")
fun parseFileEntity(cursor: Cursor, currentLocation: Location): FileEntity {
    val id = cursor.getLong(0)
    val displayName = cursor.getString(1)
    val absolutePath = cursor.getString(2)
    val size = cursor.getLong(3)
    val dateAdded = cursor.getLong(4)
    val dateModified = cursor.getLong(5)

    val songRelativePath = absolutePath.stripToRelativePath(currentLocation.absolutePath)
    val basePath = currentLocation.basePath.let { if (it == "/") "" else it } // root

    return if (songRelativePath.contains('/')) {
        val folderName = songRelativePath.substringBefore('/')
        // folder
        FileEntity.Folder(
            location = currentLocation.changeTo("$basePath/$folderName"),
            name = folderName,
            dateAdded = dateAdded,
            dateModified = dateModified
        )
    } else {
        // file
        FileEntity.File(
            location = currentLocation.changeTo("$basePath/$songRelativePath"),
            name = displayName,
            id = id,
            size = size,
            dateAdded = dateAdded,
            dateModified = dateModified
        )
    }
}

private fun String.stripToRelativePath(currentLocationAbsolutePath: String) =
    substringAfter(currentLocationAbsolutePath).removePrefix("/")