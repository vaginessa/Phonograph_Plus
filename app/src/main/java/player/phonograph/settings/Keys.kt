/*
 *  Copyright (c) 2022~2023 chr_56
 */

package player.phonograph.settings

import player.phonograph.actions.click.mode.SongClickMode
import player.phonograph.model.sort.SortMode
import player.phonograph.model.sort.SortRef
import player.phonograph.model.time.Duration
import player.phonograph.model.time.TimeIntervalCalculationMode
import androidx.datastore.preferences.core.booleanPreferencesKey as booleanPK
import androidx.datastore.preferences.core.intPreferencesKey as intPK
import androidx.datastore.preferences.core.longPreferencesKey as longPK
import androidx.datastore.preferences.core.stringPreferencesKey as stringPK

/**
 * Container Object for all available registered [PreferenceKey]
 */
@Suppress("ClassName")
object Keys {

    // Appearance
    data object homeTabConfigJsonString :
            PrimitiveKey<String>(stringPK(HOME_TAB_CONFIG), { "" })

    data object coloredNotification :
            PrimitiveKey<Boolean>(booleanPK(COLORED_NOTIFICATION), { true })

    data object classicNotification :
            PrimitiveKey<Boolean>(booleanPK(CLASSIC_NOTIFICATION), { false })

    data object coloredAppShortcuts :
            PrimitiveKey<Boolean>(booleanPK(COLORED_APP_SHORTCUTS), { true })

    data object fixedTabLayout :
            PrimitiveKey<Boolean>(booleanPK(FIXED_TAB_LAYOUT), { false })

    // Behavior-Retention
    data object rememberLastTab :
            PrimitiveKey<Boolean>(booleanPK(REMEMBER_LAST_TAB), { true })

    data object lastPage :
            PrimitiveKey<Int>(intPK(LAST_PAGE), { 0 })

    data object lastMusicChooser :
            PrimitiveKey<Int>(intPK(LAST_MUSIC_CHOOSER), { 0 })

    data object nowPlayingScreenIndex :
            PrimitiveKey<Int>(intPK(NOW_PLAYING_SCREEN_ID), { 0 })

    // Database

    // Behavior-File
    data object imageSourceConfigJsonString :
            PrimitiveKey<String>(stringPK(IMAGE_SOURCE_CONFIG), { "{}" })

    // Behavior-Playing
    data object songItemClickMode :
            PrimitiveKey<Int>(intPK(SONG_ITEM_CLICK_MODE), { SongClickMode.SONG_PLAY_NOW })

    data object songItemClickExtraFlag :
            PrimitiveKey<Int>(intPK(SONG_ITEM_CLICK_EXTRA_FLAG), { SongClickMode.FLAG_MASK_PLAY_QUEUE_IF_EMPTY })

    data object keepPlayingQueueIntact :
            PrimitiveKey<Boolean>(booleanPK(KEEP_PLAYING_QUEUE_INTACT), { true })

    data object rememberShuffle :
            PrimitiveKey<Boolean>(booleanPK(REMEMBER_SHUFFLE), { true })

    data object gaplessPlayback :
            PrimitiveKey<Boolean>(booleanPK(GAPLESS_PLAYBACK), { false })

    data object audioDucking :
            PrimitiveKey<Boolean>(booleanPK(AUDIO_DUCKING), { true })

    data object resumeAfterAudioFocusGain :
            PrimitiveKey<Boolean>(booleanPK(RESUME_AFTER_AUDIO_FOCUS_GAIN), { false })

    data object enableLyrics :
            PrimitiveKey<Boolean>(booleanPK(ENABLE_LYRICS), { true })

    data object broadcastSynchronizedLyrics :
            PrimitiveKey<Boolean>(booleanPK(BROADCAST_SYNCHRONIZED_LYRICS), { true })

    data object useLegacyStatusBarLyricsApi :
            PrimitiveKey<Boolean>(booleanPK(USE_LEGACY_STATUS_BAR_LYRICS_API), { false })

    data object broadcastCurrentPlayerState :
            PrimitiveKey<Boolean>(booleanPK(BROADCAST_CURRENT_PLAYER_STATE), { true })

    // Behavior-Lyrics
    data object synchronizedLyricsShow :
            PrimitiveKey<Boolean>(booleanPK(SYNCHRONIZED_LYRICS_SHOW), { true })

    data object displaySynchronizedLyricsTimeAxis :
            PrimitiveKey<Boolean>(booleanPK(DISPLAY_LYRICS_TIME_AXIS), { true })

    data object _lastAddedCutOffMode :
            PrimitiveKey<Int>(intPK(LAST_ADDED_CUTOFF_MODE), { TimeIntervalCalculationMode.PAST.value })

    data object _lastAddedCutOffDuration :
            PrimitiveKey<String>(stringPK(LAST_ADDED_CUTOFF_DURATION), { Duration.Week(3).serialise() })

    data object lastAddedCutoffTimeStamp :
            CompositeKey<Long>(LastAddedCutOffDurationPreferenceProvider)

    // Upgrade
    data object checkUpgradeAtStartup :
            PrimitiveKey<Boolean>(booleanPK(CHECK_UPGRADE_AT_STARTUP), { false })

    data object _checkUpdateInterval :
            PrimitiveKey<String>(stringPK(CHECK_UPGRADE_INTERVAL), { Duration.Day(1).serialise() })

    data object checkUpdateInterval :
            CompositeKey<Duration>(CheckUpdateIntervalPreferenceProvider)

    data object lastCheckUpgradeTimeStamp :
            PrimitiveKey<Long>(longPK(LAST_CHECK_UPGRADE_TIME), { 0 })

    // List-SortMode
    data object _songSortMode :
            PrimitiveKey<String>(stringPK(SONG_SORT_MODE), { SortMode(SortRef.ID, false).serialize() })

    data object songSortMode :
            CompositeKey<SortMode>(SortModePreferenceProvider.SongSortMode)


    data object _albumSortMode :
            PrimitiveKey<String>(stringPK(ALBUM_SORT_MODE), { SortMode(SortRef.ID, false).serialize() })

    data object albumSortMode :
            CompositeKey<SortMode>(SortModePreferenceProvider.AlbumSortMode)


    data object _artistSortMode :
            PrimitiveKey<String>(stringPK(ARTIST_SORT_MODE), { SortMode(SortRef.ID, false).serialize() })

    data object artistSortMode :
            CompositeKey<SortMode>(SortModePreferenceProvider.ArtistSortMode)


    data object _genreSortMode :
            PrimitiveKey<String>(stringPK(GENRE_SORT_MODE), { SortMode(SortRef.ID, false).serialize() })

    data object genreSortMode :
            CompositeKey<SortMode>(SortModePreferenceProvider.GenreSortMode)


    data object _fileSortMode :
            PrimitiveKey<String>(stringPK(FILE_SORT_MODE), { SortMode(SortRef.ID, false).serialize() })

    data object fileSortMode :
            CompositeKey<SortMode>(SortModePreferenceProvider.FileSortMode)


    data object _collectionSortMode :
            PrimitiveKey<String>(stringPK(SONG_COLLECTION_SORT_MODE), { SortMode(SortRef.ID, false).serialize() })

    data object collectionSortMode :
            CompositeKey<SortMode>(SortModePreferenceProvider.CollectionSortMode)


    data object _playlistSortMode :
            PrimitiveKey<String>(stringPK(PLAYLIST_SORT_MODE), { SortMode(SortRef.ID, false).serialize() })

    data object playlistSortMode :
            CompositeKey<SortMode>(SortModePreferenceProvider.PlaylistSortMode)


    // List-Appearance

    data object albumArtistColoredFooters :
            PrimitiveKey<Boolean>(booleanPK(ALBUM_ARTIST_COLORED_FOOTERS), { true })

    data object showFileImages :
            PrimitiveKey<Boolean>(booleanPK(SHOW_FILE_IMAGINES), { false })

    // SleepTimer
    data object lastSleepTimerValue :
            PrimitiveKey<Int>(intPK(LAST_SLEEP_TIMER_VALUE), { 30 })

    data object nextSleepTimerElapsedRealTime :
            PrimitiveKey<Long>(longPK(NEXT_SLEEP_TIMER_ELAPSED_REALTIME), { -1L })

    data object sleepTimerFinishMusic :
            PrimitiveKey<Boolean>(booleanPK(SLEEP_TIMER_FINISH_SONG), { false })

    // Misc
    data object ignoreUpgradeDate :
            PrimitiveKey<Long>(longPK(IGNORE_UPGRADE_DATE), { 0 })

    data object pathFilterExcludeMode :
            PrimitiveKey<Boolean>(booleanPK(PATH_FILTER_EXCLUDE_MODE), { true })

    // Compatibility
    data object useLegacyFavoritePlaylistImpl :
            PrimitiveKey<Boolean>(booleanPK(USE_LEGACY_FAVORITE_PLAYLIST_IMPL), { false })

    data object useLegacyListFilesImpl :
            PrimitiveKey<Boolean>(booleanPK(USE_LEGACY_LIST_FILES_IMPL), { false })

    data object playlistFilesOperationBehaviour :
            PrimitiveKey<String>(stringPK(PLAYLIST_FILES_OPERATION_BEHAVIOUR), { PLAYLIST_OPS_BEHAVIOUR_AUTO })

    data object useLegacyDetailDialog :
            PrimitiveKey<Boolean>(booleanPK(USE_LEGACY_DETAIL_DIALOG), { false })

    data object disableRealTimeSearch :
            PrimitiveKey<Boolean>(booleanPK(DISABLE_REAL_TIME_SEARCH), { false })

}