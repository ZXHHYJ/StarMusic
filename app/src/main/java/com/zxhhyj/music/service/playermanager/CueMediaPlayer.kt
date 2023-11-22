package com.zxhhyj.music.service.playermanager

import android.media.MediaPlayer
import android.media.audiofx.Equalizer
import android.util.Range
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.service.playermanager.exception.MediaPlayerException
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
class CueMediaPlayer {

    private val mediaPlayer = MediaPlayer()

    private var equalizer = Equalizer(0, mediaPlayer.audioSessionId).apply {
        enabled = true
    }

    private var playWhenReady = false

    private var currentSong: SongBean? = null

    private var timer: Timer? = null

    private val _pauseFlow = MutableStateFlow(true)
    val pauseFlow: StateFlow<Boolean> = _pauseFlow

    private val _currentProgressFlow = MutableStateFlow(0)
    val currentProgressFlow: StateFlow<Int> = _currentProgressFlow

    private val _songDurationFlow = MutableStateFlow(0)
    val songDurationFlow: StateFlow<Int> = _songDurationFlow

    var errorListener: (CueMediaPlayer.(Exception) -> Unit)? = null

    var completionListener: (() -> Unit)? = null

    fun preparePlay(song: SongBean) {
        preparePlay(song, true)
    }

    fun prepare(song: SongBean) {
        preparePlay(song, false)
    }

    private fun preparePlay(song: SongBean, playWhenReady: Boolean) {
        this.playWhenReady = playWhenReady
        this.currentSong = song
        _currentProgressFlow.value = 0
        _songDurationFlow.value = song.duration.toInt()
        timer?.pause()
        timer = null
        timer = Timer(0, song.duration.toInt()).apply {
            setOnFinishedListener {
                this@CueMediaPlayer.seekTo(song.startPosition.toInt())
                completionListener?.invoke()
            }
            setOnUpdateListener {
                _currentProgressFlow.value = it
            }
        }
        mediaPlayer.apply {
            reset()
            setOnSeekCompleteListener {
                val currentPosition = when (song.startPosition) {
                    0L -> it.currentPosition
                    else -> it.currentPosition - song.startPosition
                }.toInt()
                timer?.setCurrentTime(currentPosition)
            }
            setOnPreparedListener {
                it.seekTo(song.startPosition.toInt())
                if (this@CueMediaPlayer.playWhenReady) {
                    this@CueMediaPlayer.start()
                }
            }
            setOnCompletionListener {
                this@CueMediaPlayer.pause()
                completionListener?.invoke()
            }
            setOnErrorListener { _, i, i2 ->
                timer?.pause()
                this@CueMediaPlayer._pauseFlow.value = true
                errorListener?.invoke(this@CueMediaPlayer, MediaPlayerException(i, i2))
                true
            }
            try {
                setDataSource(song.data)
                prepare()
            } catch (e: Exception) {
                errorListener?.invoke(this@CueMediaPlayer, e)
            }
        }
    }

    /**
     * 开始播放音乐。
     */
    fun start() {
        timer?.start()
        this._pauseFlow.value = false
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
        }
    }

    /**
     * 暂停播放音乐。
     */
    fun pause() {
        timer?.pause()
        this._pauseFlow.value = true
        playWhenReady = false
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    /**
     * 设置音乐播放进度。
     */
    fun seekTo(position: Int) {
        val currentSong = currentSong ?: return
        _currentProgressFlow.value = position
        val currentPosition = when (currentSong.startPosition) {
            0L -> position
            else -> position + currentSong.startPosition
        }.toInt()
        mediaPlayer.seekTo(currentPosition)
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
     * 定时器内部类。
     */
    private inner class Timer(private val startTime: Int, private val endTime: Int) {
        private var job: Job? = null
        private var currentTime = startTime
        private var onUpdateListener: ((Int) -> Unit)? = null
        private var onFinishedListener: (() -> Unit)? = null
        private val updateTimeout = 100

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
         * 启动定时器。
         */
        @OptIn(DelicateCoroutinesApi::class)
        fun start() {
            job?.cancel()
            job = GlobalScope.launch(Dispatchers.Main) {
                flow {
                    var time = currentTime
                    while (time < endTime) {
                        delay(updateTimeout.toLong())
                        time = startTime + mediaPlayer.currentPosition
                        emit(time)
                    }
                }.flowOn(Dispatchers.Main)
                    .collect { time ->
                        currentTime = time
                        onUpdateListener?.invoke(time)
                    }
                onFinishedListener?.invoke()
                job?.cancel()
            }
        }

        /**
         * 暂停定时器。
         */
        fun pause() {
            job?.cancel()
            job = null
        }

        /**
         * 设置当前时间并重新启动定时器。
         */
        fun setCurrentTime(newTime: Int) {
            if (job !== null) {
                job?.cancel()
                currentTime = newTime
                start()
            } else {
                currentTime = newTime
            }
            onUpdateListener?.invoke(newTime)
        }
    }
}