@file:UnstableApi
@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.zxhhyj.mediaplayer

import android.content.Context
import android.media.audiofx.Equalizer
import android.util.Range
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.zxhhyj.mediaplayer.impl.CueMediaInfo
import com.zxhhyj.mediaplayer.impl.DataSource
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

/**
 * 管理使用 MediaPlayer 播放歌曲。
 */
class CueMediaPlayer(
    context: Context,
    private val source: DataSource = object : DataSource {
        override fun getDataSource(data: String): String {
            return data
        }
    }
) {

    private val listener = object : Player.Listener {

        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
            errorListener?.invoke(this@CueMediaPlayer, error)
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            super.onPlaybackStateChanged(playbackState)
            when (playbackState) {
                Player.STATE_READY -> {
                    if (playWhenReady) {
                        start()
                    }
                }

                Player.STATE_BUFFERING -> {
                }

                Player.STATE_ENDED -> {
                    this@CueMediaPlayer.pause()
                    completionListener?.invoke()
                }

                Player.STATE_IDLE -> {
                }
            }
        }

        override fun onEvents(player: Player, events: Player.Events) {
            super.onEvents(player, events)
            if (player.isPlaying) {
                timer.apply {
                    start(
                        0,
                        player.duration.toInt(),
                        player.currentPosition.toInt()
                    )
                    _songDurationFlow.value = currentSong?.duration?.toInt() ?: endTime
                }
            } else {
                timer.pause()
            }
            _pauseFlow.value = !player.isPlaying
        }
    }

    private val exoPlayer = ExoPlayer.Builder(context).build().apply {
        addListener(listener)
    }

    private var equalizer = Equalizer(0, exoPlayer.audioSessionId).apply {
        enabled = true
    }

    private var playWhenReady = false

    private var currentSong: CueMediaInfo? = null

    private val timer by lazy {
        Timer().apply {
            setOnFinishedListener {
                currentSong?.takeIf { it.startPosition != null }?.let {
                    this@CueMediaPlayer.seekTo(it.startPosition!!.toInt())
                } ?: run {
                    this@CueMediaPlayer.seekTo(0)
                }
                completionListener?.invoke()
            }
            setOnUpdateListener { newTime ->
                currentSong?.takeIf { it.startPosition != null }?.let {
                    _currentProgressFlow.value = (it.startPosition!! + newTime).toInt()
                } ?: run {
                    _currentProgressFlow.value = newTime
                }
            }
        }
    }

    private val _pauseFlow = MutableStateFlow(true)
    val pauseFlow: StateFlow<Boolean> = _pauseFlow

    private val _currentProgressFlow = MutableStateFlow(0)
    val currentProgressFlow: StateFlow<Int> = _currentProgressFlow

    private val _songDurationFlow = MutableStateFlow(0)
    val songDurationFlow: StateFlow<Int> = _songDurationFlow

    var errorListener: (CueMediaPlayer.(PlaybackException) -> Unit)? = null

    var completionListener: (() -> Unit)? = null

    fun preparePlay(song: CueMediaInfo) {
        preparePlay(song, true)
    }

    fun prepare(song: CueMediaInfo) {
        preparePlay(song, false)
    }

    private fun preparePlay(song: CueMediaInfo, playWhenReady: Boolean) {
        this.playWhenReady = playWhenReady
        currentSong = song
        _currentProgressFlow.value = 0
        exoPlayer.setMediaItem(MediaItem.fromUri(source.getDataSource(song.data)))
        exoPlayer.prepare()
    }

    /**
     * 开始播放音乐。
     */
    fun start() {
        exoPlayer.play()
    }

    /**
     * 暂停播放音乐。
     */
    fun pause() {
        playWhenReady = false
        exoPlayer.pause()
    }

    /**
     * 释放MediaPlayer
     */
    fun release() {
        exoPlayer.pause()
        exoPlayer.release()
    }

    /**
     * 设置音乐播放进度。
     */
    fun seekTo(position: Int) {
        val currentSong = currentSong ?: return
        when {
            currentSong.startPosition != null && currentSong.endPosition != null -> {
                val currentPosition = when (currentSong.startPosition) {
                    0L -> position
                    else -> position + currentSong.startPosition!!
                }.toLong()
                exoPlayer.seekTo(currentPosition)
                _currentProgressFlow.value = currentPosition.toInt()
            }

            else -> {
                exoPlayer.seekTo(position.toLong())
                _currentProgressFlow.value = position
            }
        }
    }

    fun setEnableEqualizer(enabled: Boolean) {
        equalizer.enabled = enabled
    }

    /**
     * 设置指定频段的级别
     * @param band 频段索引
     * @param level 频段级别
     */
    fun setBandLevel(band: Int, level: Int) {
        equalizer.setBandLevel(band.toShort(), level.toShort())
    }

    /**
     * 获取指定频段的级别
     * @param band 频段索引
     * @return 频段级别
     */
    fun getBandLevel(band: Int): Int {
        return equalizer.getBandLevel(band.toShort()).toInt()
    }

    /**
     * 获取频段级别范围
     * @return 频段级别范围
     */
    fun getBandRange(): Range<Int> {
        val minLevel = equalizer.bandLevelRange?.min() ?: 0
        val maxLevel = equalizer.bandLevelRange?.max() ?: 0
        return Range(minLevel.toInt(), maxLevel.toInt())
    }

    /**
     * 获取频段数量
     * @return 频段数量
     */
    fun getNumberOfBands(): Int {
        return equalizer.numberOfBands.toInt()
    }


    fun getBandFreqRange(band: Int): IntArray {
        return equalizer.getBandFreqRange(band.toShort()) ?: IntArray(0)
    }

    /**
     * 定时器
     */
    class Timer {
        companion object {
            private const val updateTimeout = 100
        }

        var startTime: Int = 0
            private set
        var endTime: Int = 0
            private set

        private var job: Job? = null
        private var currentTime = startTime
        private var onUpdateListener: ((Int) -> Unit)? = null
        private var onFinishedListener: (() -> Unit)? = null


        @OptIn(DelicateCoroutinesApi::class)
        fun start(startTime: Int, endTime: Int, currentTime: Int) {
            this.startTime = startTime
            this.endTime = endTime
            this.currentTime = currentTime
            job?.cancel()
            job = GlobalScope.launch(Dispatchers.Main) {
                flow {
                    var time = currentTime
                    while (time < endTime) {
                        delay(updateTimeout.toLong())
                        time += startTime + updateTimeout
                        emit(time)
                    }
                }.flowOn(Dispatchers.Main)
                    .collect { time ->
                        this@Timer.currentTime = time
                        onUpdateListener?.invoke(time)
                    }
                onFinishedListener?.invoke()
                job?.cancel()
            }
            onUpdateListener?.invoke(currentTime)
        }

        /**
         * 设置更新监听器。
         */
        fun setOnUpdateListener(listener: (Int) -> Unit) {
            onUpdateListener = listener
        }

        /**
         * 设置完成监听器。
         */
        fun setOnFinishedListener(listener: () -> Unit) {
            onFinishedListener = listener
        }

        /**
         * 暂停定时器。
         */
        fun pause() {
            job?.cancel()
            job = null
        }
    }
}