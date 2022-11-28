/*
 * Copyright (c) 2022 chr_56
 */

package player.phonograph.ui.fragments.player

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState
import mt.pref.ThemeColor
import mt.util.color.secondaryTextColor
import player.phonograph.R
import player.phonograph.adapter.base.MediaEntryViewHolder
import player.phonograph.adapter.display.PlayingQueueAdapter
import player.phonograph.adapter.display.initMenu
import player.phonograph.databinding.FragmentPlayingQueuePanelBinding
import player.phonograph.model.Song
import player.phonograph.model.buildInfoString
import player.phonograph.model.getReadableDurationString
import player.phonograph.model.infoString
import player.phonograph.service.MusicPlayerRemote
import player.phonograph.ui.fragments.AbsMusicServiceFragment
import player.phonograph.util.ImageUtil.getTintedDrawable
import player.phonograph.util.Util.isLandscape
import player.phonograph.util.ViewUtil
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu

class PlayingQueueFragment : AbsMusicServiceFragment() {

    private var _binding: FragmentPlayingQueuePanelBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayingQueuePanelBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _currentSongViewHolder = MediaEntryViewHolder(binding.playingQueueCurrentSong.root)
        if (isLandscape(resources)) {
            binding.playingQueueCurrentSong.root.visibility = GONE
        } else {
            bindCurrentSongViewHolder()
            updateCurrentSong(MusicPlayerRemote.currentSong)
        }
        initRecyclerView()
        setupSubHeaderColor()
    }

    override fun onDestroyView() {
        _recyclerViewDragDropManager?.let {
            recyclerViewDragDropManager.release()
            _recyclerViewDragDropManager = null
        }
        _wrappedAdapter?.let {
            WrapperAdapterUtils.releaseAll(wrappedAdapter)
            _wrappedAdapter = null
        }
        _currentSongViewHolder = null
        super.onDestroyView()
        _binding = null
    }

    private var _currentSongViewHolder: MediaEntryViewHolder? = null
    val currentSongViewHolder: MediaEntryViewHolder get() = _currentSongViewHolder!!

    private fun bindCurrentSongViewHolder() {
        currentSongViewHolder.apply {
            separator!!.visibility = View.VISIBLE
            shortSeparator!!.visibility = View.GONE
            image!!.scaleType = ImageView.ScaleType.CENTER
            image!!.setImageDrawable(
                requireContext().getTintedDrawable(
                    R.drawable.ic_volume_up_white_24dp,
                    requireContext().secondaryTextColor(
                        ViewUtil.isWindowBackgroundDarkSafe(requireActivity())
                    )
                )
            )
        }
        currentSongViewHolder.itemView.setOnClickListener(::toggleThePanel)
        currentSongViewHolder.menu?.let { menuView ->
            menuView.setOnClickListener {
                PopupMenu(requireContext(), it).apply {
                    MusicPlayerRemote.currentSong
                        .initMenu(
                            requireContext(),
                            this.menu,
                            index = MusicPlayerRemote.position
                        )
                }.show()
            }
        }
    }

    fun updateCurrentSong(song: Song) {
        currentSongViewHolder.title!!.text = song.title
        currentSongViewHolder.text!!.text = song.infoString()
    }

    internal lateinit var layoutManager: LinearLayoutManager
    private lateinit var playingQueueAdapter: PlayingQueueAdapter
    private var _wrappedAdapter: RecyclerView.Adapter<*>? = null
    private val wrappedAdapter: RecyclerView.Adapter<*> get() = _wrappedAdapter!!
    private var _recyclerViewDragDropManager: RecyclerViewDragDropManager? = null
    private val recyclerViewDragDropManager: RecyclerViewDragDropManager get() = _recyclerViewDragDropManager!!

    private fun initRecyclerView() {
        layoutManager = LinearLayoutManager(requireActivity())
        playingQueueAdapter = PlayingQueueAdapter(
            requireActivity() as AppCompatActivity,
            MusicPlayerRemote.playingQueue,
            MusicPlayerRemote.position
        ) {}
        _recyclerViewDragDropManager = RecyclerViewDragDropManager()
        _wrappedAdapter = recyclerViewDragDropManager.createWrappedAdapter(playingQueueAdapter)

        val animator: GeneralItemAnimator = RefactoredDefaultItemAnimator()
        binding.playingQueueRecyclerview.layoutManager = layoutManager
        binding.playingQueueRecyclerview.adapter = wrappedAdapter
        binding.playingQueueRecyclerview.itemAnimator = animator
        recyclerViewDragDropManager.attachRecyclerView(binding.playingQueueRecyclerview)
        layoutManager.scrollToPositionWithOffset(MusicPlayerRemote.position + 1, 0)
    }

    fun updateQueue(playingQueue: List<Song>, position: Int) {
        playingQueueAdapter.dataset = playingQueue
        updateQueuePosition(position)
    }

    fun updateQueuePosition(position: Int) {
        playingQueueAdapter.current = position
        layoutManager.scrollToPositionWithOffset(position + 1, 0)
        binding.playingQueueSubHeader.text = upNextAndQueueTime
    }

    fun toggleThePanel(view: View) {
        val playerSlidingLayout: SlidingUpPanelLayout? = callback?.getPlayerSlidingLayout()
        playerSlidingLayout?.panelState =
            when (playerSlidingLayout?.panelState) {
                PanelState.COLLAPSED -> PanelState.EXPANDED
                PanelState.EXPANDED  -> PanelState.COLLAPSED
                else                 -> PanelState.COLLAPSED
            }
    }

    private fun setupSubHeaderColor() {
        updateSubHeaderColor(ThemeColor.primaryColor(requireContext()))
    }

    fun updateSubHeaderColor(color: Int) {
        binding.playingQueueSubHeader.setTextColor(color)
    }

    override fun onServiceConnected() {
        updateQueue(MusicPlayerRemote.playingQueue, MusicPlayerRemote.position)
        updateQueuePosition(MusicPlayerRemote.position)
    }

    override fun onPlayingMetaChanged() {
        updateCurrentSong(MusicPlayerRemote.currentSong)
        updateQueuePosition(MusicPlayerRemote.position)
    }

    override fun onQueueChanged() {
        updateQueue(MusicPlayerRemote.playingQueue, MusicPlayerRemote.position)
        updateQueuePosition(MusicPlayerRemote.position)
    }

    override fun onMediaStoreChanged() {
        updateQueue(MusicPlayerRemote.playingQueue, MusicPlayerRemote.position)
    }

    override fun onShuffleModeChanged() {
        updateQueue(MusicPlayerRemote.playingQueue, MusicPlayerRemote.position)
    }

    private val upNextAndQueueTime: String
        get() {
            val duration = MusicPlayerRemote.getQueueDurationMillis(MusicPlayerRemote.position)
            return buildInfoString(
                resources.getString(R.string.up_next),
                getReadableDurationString(duration)
            )
        }

    private var callback: Callback? = null

    interface Callback {
        fun getPlayerSlidingLayout(): SlidingUpPanelLayout?
    }
}