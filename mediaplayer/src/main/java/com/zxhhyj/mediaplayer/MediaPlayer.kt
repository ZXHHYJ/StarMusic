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
import com.zxhhyj.mediaplayer.impl.DataSource
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * 管理使用 MediaPlayer 播放歌曲。
 */
class MediaPlayer(
    context: Context,
    private val source: DataSource = object : DataSource {
        override fun getDataSource(data: String): String {
            return data
        }
    }
) {

    @OptIn(DelicateCoroutinesApi::class)
    private val listener = object : Player.Listener {

        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
            errorListener?.invoke(this@MediaPlayer, error)
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
                    this@MediaPlayer.pause()
                    completionListener?.invoke()
                }

                Player.STATE_IDLE -> {
                }
            }
        }

        override fun onEvents(player: Player, events: Player.Events) {
            super.onEvents(player, events)
            if (player.isPlaying) {
                if (positionUpdateJob == null) {
                    positionUpdateJob = GlobalScope.launch(Dispatchers.Main) {
                        while (true) {
                            _currentProgressFlow.value = player.currentPosition.toInt()
                            delay(100)
                        }
                    }
                }
            } else {
                positionUpdateJob?.cancel()
            }
            _pauseFlow.value = !player.isPlaying
            _songDurationFlow.value = player.duration.toInt()
        }
    }

    private val exoPlayer = ExoPlayer.Builder(context).build().apply {
        addListener(listener)
    }

    private var equalizer = Equalizer(0, exoPlayer.audioSessionId).apply {
        enabled = true
    }

    private var playWhenReady = false

    private val _pauseFlow = MutableStateFlow(true)
    val pauseFlow: StateFlow<Boolean> = _pauseFlow

    private val _currentProgressFlow = MutableStateFlow(0)
    val currentProgressFlow: StateFlow<Int> = _currentProgressFlow

    private val _songDurationFlow = MutableStateFlow(0)
    val songDurationFlow: StateFlow<Int> = _songDurationFlow

    private var positionUpdateJob: Job? = null

    var errorListener: (MediaPlayer.(PlaybackException) -> Unit)? = null

    var completionListener: (() -> Unit)? = null

    fun preparePlay(data: String) {
        preparePlay(data, true)
    }

    fun prepare(data: String) {
        preparePlay(data, false)
    }

    private fun preparePlay(data: String, playWhenReady: Boolean) {
        this.playWhenReady = playWhenReady
        _currentProgressFlow.value = 0
        exoPlayer.setMediaItem(MediaItem.fromUri(source.getDataSource(data)))
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
        positionUpdateJob?.cancel()
        positionUpdateJob = null
    }

    /**
     * 设置音乐播放进度。
     */
    fun seekTo(position: Int) {
        exoPlayer.seekTo(position.toLong())
        _currentProgressFlow.value = position
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

}