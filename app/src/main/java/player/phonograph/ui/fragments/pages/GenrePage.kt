/*
 * Copyright (c) 2022 chr_56 & Abou Zeid (kabouzeid) (original author)
 */

package player.phonograph.ui.fragments.pages

import player.phonograph.R
import player.phonograph.adapter.display.DisplayAdapter
import player.phonograph.adapter.display.GenreDisplayAdapter
import player.phonograph.model.Genre
import player.phonograph.model.sort.SortRef
import player.phonograph.repo.mediastore.loaders.GenreLoader
import player.phonograph.ui.fragments.pages.util.DisplayConfigTarget
import androidx.fragment.app.viewModels
import android.content.Context
import kotlinx.coroutines.CoroutineScope

class GenrePage : AbsDisplayPage<Genre, DisplayAdapter<Genre>>() {

    override val viewModel: AbsDisplayPageViewModel<Genre> get() = _viewModel

    private val _viewModel: GenrePageViewModel by viewModels()

    class GenrePageViewModel : AbsDisplayPageViewModel<Genre>() {
        override suspend fun loadDataSetImpl(context: Context, scope: CoroutineScope): Collection<Genre> {
            return GenreLoader.all(context)
        }

        override val headerTextRes: Int get() = R.plurals.item_genres
    }


    override val displayConfigTarget get() = DisplayConfigTarget.GenrePage

    override fun initAdapter(): DisplayAdapter<Genre> {
        return GenreDisplayAdapter(
            hostFragment.mainActivity,
            ArrayList(), // empty until Genre loaded
            R.layout.item_list_no_image
        ) {
            showSectionName = true
        }
    }

    override val availableSortRefs: Array<SortRef>
        get() = arrayOf(
            SortRef.DISPLAY_NAME,
            SortRef.SONG_COUNT,
        )

    override fun allowColoredFooter(): Boolean = false

    companion object {
        const val TAG = "GenrePage"
    }
}
