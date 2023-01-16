package player.phonograph.model

import androidx.annotation.Keep
import android.content.Context
import android.os.Parcel
import android.os.Parcelable

/**
 * @author Karim Abou Zeid (kabouzeid)
 */
open class Song : Parcelable, Displayable {
    @JvmField
    val id: Long
    @JvmField
    val title: String
    @JvmField
    val trackNumber: Int
    @JvmField
    val year: Int
    @JvmField
    val duration: Long
    @JvmField
    val data: String
    @JvmField
    val dateAdded: Long
    @JvmField
    val dateModified: Long
    @JvmField
    val albumId: Long
    @JvmField
    val albumName: String?
    @JvmField
    val artistId: Long
    @JvmField
    val artistName: String?
    //
    // Android R
    //
    @JvmField
    val albumArtistName: String?

    constructor(
        id: Long,
        title: String,
        trackNumber: Int,
        year: Int,
        duration: Long,
        data: String,
        dateAdded: Long,
        dateModified: Long,
        albumId: Long,
        albumName: String?,
        artistId: Long,
        artistName: String?,
        albumArtistName: String?
    ) {
        this.id = id
        this.title = title
        this.trackNumber = trackNumber
        this.year = year
        this.duration = duration
        this.data = data
        this.dateAdded = dateAdded
        this.dateModified = dateModified
        this.albumId = albumId
        this.albumName = albumName
        this.artistId = artistId
        this.artistName = artistName
        this.albumArtistName = albumArtistName ?: artistName
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Song) return false

        if (id != other.id) return false
        if (title != other.title) return false
        if (trackNumber != other.trackNumber) return false
        if (year != other.year) return false
        if (duration != other.duration) return false
        if (data != other.data) return false
        if (dateAdded != other.dateAdded) return false
        if (dateModified != other.dateModified) return false
        if (albumId != other.albumId) return false
        if (albumName != other.albumName) return false
        if (artistId != other.artistId) return false
        if (artistName != other.artistName) return false
        if (albumArtistName != other.albumArtistName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.toInt()
        result = 31 * result + title.hashCode()
        result = 31 * result + trackNumber
        result = 31 * result + year
        result = 31 * result + (duration xor (duration ushr 32)).toInt()
        result = 31 * result + data.hashCode()
        result = 31 * result + (dateModified xor (dateModified ushr 32)).toInt()
        result = 31 * result + (dateAdded xor (dateAdded ushr 32)).toInt()
        result = 31 * result + albumId.toInt()
        result = 31 * result + (albumName?.hashCode() ?: 0)
        result = 31 * result + artistId.toInt()
        result = 31 * result + (artistName?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Song{id=$id, title='$title', trackNumber=$trackNumber, year=$year, duration=$duration, data='$data', dateModified=$dateModified, dataAdded=$dateAdded, albumId=$albumId, albumName='$albumName', artistId=$artistId, artistName='$artistName', albumArtistName='$albumArtistName'}"
    }

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(title)
        dest.writeInt(trackNumber)
        dest.writeInt(year)
        dest.writeLong(duration)
        dest.writeString(data)
        dest.writeLong(dateAdded)
        dest.writeLong(dateModified)
        dest.writeLong(albumId)
        dest.writeString(albumName)
        dest.writeLong(artistId)
        dest.writeString(artistName)
        dest.writeString(albumArtistName)
    }

    protected constructor(parcel: Parcel) {
        id = parcel.readLong()
        title = parcel.readString()!!
        trackNumber = parcel.readInt()
        year = parcel.readInt()
        duration = parcel.readLong()
        data = parcel.readString()!!
        dateAdded = parcel.readLong()
        dateModified = parcel.readLong()
        albumId = parcel.readLong()
        albumName = parcel.readString()
        artistId = parcel.readLong()
        artistName = parcel.readString()
        albumArtistName = parcel.readString()
    }

    override fun getItemID(): Long = id

    override fun getDisplayTitle(context: Context): CharSequence = title

    override fun getDescription(context: Context): CharSequence? = infoString()

    companion object {
        @JvmField
        val EMPTY_SONG = Song(
            id = -1,
            title = "",
            trackNumber = -1,
            year = -1,
            duration = -1,
            data = "",
            dateAdded = -1,
            dateModified = -1,
            albumId = -1,
            albumName = "",
            artistId = -1,
            artistName = "",
            albumArtistName = ""
        )

        @Keep
        @JvmField
        val CREATOR: Parcelable.Creator<Song> = object : Parcelable.Creator<Song> {
            override fun createFromParcel(source: Parcel): Song {
                return Song(source)
            }
            override fun newArray(size: Int): Array<Song?> {
                return arrayOfNulls(size)
            }
        }
    }
}
