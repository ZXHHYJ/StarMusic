@file:Suppress("UNUSED")

package com.zxhhyj.music.service.playmanager

import android.media.MediaPlayer
import android.media.audiofx.Equalizer
import android.util.Range
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zxhhyj.music.logic.bean.SongBean
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

/**
 * 管理使用 MediaPlayer 播放歌曲。
 */
class CueMediaPlayer {

    private val mMediaPlayer = MediaPlayer()

    private var mEqualizer = Equalizer(0, mMediaPlayer.audioSessionId).apply {
        enabled = true
    }

    private var mCurrentSong: SongBean? = null

    private var mTimer: Timer? = null

    private val mPause = MutableLiveData(true)
    val pause: LiveData<Boolean> = mPause

    private val mCurrentProgress = MutableLiveData<Int>()
    val currentProgress: LiveData<Int> = mCurrentProgress

    private val mSongDuration = MutableLiveData<Int>()
    val songDuration: LiveData<Int> = mSongDuration

    private var mPreparedListener: MediaPlayer.OnPreparedListener? = null
    private var mSeekCompleteListener: MediaPlayer.OnSeekCompleteListener? = null
    private var mCompletionListener: MediaPlayer.OnCompletionListener? = null
    private var mErrorListener: MediaPlayer.OnErrorListener? = null

    /**
     * 设置 MediaPlayer 的 OnPreparedListener。
     */
    fun setOnPreparedListener(preparedListener: MediaPlayer.OnPreparedListener) {
        this.mPreparedListener = preparedListener
    }

    /**
     * 设置 MediaPlayer 的 OnSeekCompleteListener。
     */
    fun setOnSeekCompleteListener(seekCompleteListener: MediaPlayer.OnSeekCompleteListener) {
        this.mSeekCompleteListener = seekCompleteListener
    }

    /**
     * 设置 MediaPlayer 的 OnCompletionListener。
     */
    fun setOnCompletionListener(completionListener: MediaPlayer.OnCompletionListener) {
        this.mCompletionListener = completionListener
    }

    /**
     * 设置 MediaPlayer 的 OnErrorListener。
     */
    fun setOnErrorListener(errorListener: MediaPlayer.OnErrorListener) {
        this.mErrorListener = errorListener
    }

    /**
     * 播放音乐。
     */
    fun play(song: SongBean) {
        this.mCurrentSong = song
        mCurrentProgress.value = 0
        mSongDuration.value = song.duration.toInt()
        mTimer?.pause()
        mTimer = null
        mTimer = Timer(0, song.duration.toInt()).apply {
            setOnFinishedListener {
                this@CueMediaPlayer.seekTo(song.startPosition.toInt())
                mCompletionListener?.onCompletion(mMediaPlayer)
            }
            setOnUpdateListener {
                mCurrentProgress.value = it
            }
        }
        mMediaPlayer.apply {
            reset()
            setOnSeekCompleteListener {
                val currentPosition = when (song.startPosition) {
                    0L -> it.currentPosition
                    else -> it.currentPosition - song.startPosition
                }.toInt()
                mTimer?.setCurrentTime(currentPosition)
                mSeekCompleteListener?.onSeekComplete(it)
            }
            setOnPreparedListener {
                it.seekTo(song.startPosition.toInt())
                this@CueMediaPlayer.start()
                mPreparedListener?.onPrepared(it)
            }
            setOnCompletionListener {
                mTimer?.pause()
                mCompletionListener?.onCompletion(mMediaPlayer)
            }
            setOnErrorListener { mediaPlayer, i, i2 ->
                mTimer?.pause()
                mPause.value = true
                mErrorListener?.onError(mediaPlayer, i, i2) ?: false
            }
            try {
                setDataSource(song.data)
                prepareAsync()
            } catch (_: Exception) {
                mCompletionListener?.onCompletion(mMediaPlayer)
            }
        }
    }

    /**
     * 开始播放音乐。
     */
    fun start() {
        mTimer?.start()
        mPause.value = false
        mMediaPlayer.start()
    }

    /**
     * 暂停播放音乐。
     */
    fun pause() {
        mTimer?.pause()
        mPause.value = true
        mMediaPlayer.pause()
    }

    /**
     * 设置音乐播放进度。
     */
    fun seekTo(position: Int) {
        val currentSong = mCurrentSong ?: return
        mCurrentProgress.value = position
        val currentPosition = when (currentSong.startPosition) {
            0L -> position
            else -> position + currentSong.startPosition
        }.toInt()
        mMediaPlayer.seekTo(currentPosition)
    }

    /**
     * 释放 MediaPlayer 资源。
     */
    fun release() {
        mTimer?.pause()
        mTimer = null
        mPause.value = false
        mCurrentProgress.value = 0
        mSongDuration.value = 0
        mMediaPlayer.release()
        mEqualizer.release()
    }

    fun setEnableEqualizer(enabled: Boolean) {
        mEqualizer.enabled = enabled
    }

    /**
     * 设置指定频段的级别
     * @param band 频段索引
     * @param level 频段级别
     */
    fun setBandLevel(band: Int, level: Int) {
        mEqualizer.setBandLevel(band.toShort(), level.toShort())
    }

    /**
     * 获取指定频段的级别
     * @param band 频段索引
     * @return 频段级别
     */
    fun getBandLevel(band: Int): Int {
        return mEqualizer.getBandLevel(band.toShort()).toInt()
    }

    /**
     * 获取频段级别范围
     * @return 频段级别范围
     */
    fun getBandRange(): Range<Int> {
        val minLevel = mEqualizer.bandLevelRange?.min() ?: 0
        val maxLevel = mEqualizer.bandLevelRange?.max() ?: 0
        return Range(minLevel.toInt(), maxLevel.toInt())
    }

    /**
     * 获取频段数量
     * @return 频段数量
     */
    fun getNumberOfBands(): Int {
        return mEqualizer.numberOfBands.toInt()
    }


    fun getBandFreqRange(band: Int): IntArray {
        return mEqualizer.getBandFreqRange(band.toShort()) ?: IntArray(0)
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
                        time = startTime + mMediaPlayer.currentPosition
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