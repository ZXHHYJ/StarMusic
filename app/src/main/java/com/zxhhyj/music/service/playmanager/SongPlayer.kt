@file:Suppress("UNUSED")

package com.zxhhyj.music.service.playmanager

import android.media.MediaPlayer
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

class SongPlayer {
    private val mediaPlayer = MediaPlayer()

    private lateinit var currentSong: SongBean

    private var timer: Timer? = null

    private val _pause = MutableLiveData(true)

    val pause: LiveData<Boolean> = _pause

    private val _currentProgress = MutableLiveData<Int>()

    val currentProgress: LiveData<Int> = _currentProgress

    private val _songDuration = MutableLiveData<Int>()

    val songDuration: LiveData<Int> = _songDuration

    private var preparedListener: MediaPlayer.OnPreparedListener? = null

    private var seekCompleteListener: MediaPlayer.OnSeekCompleteListener? = null

    private var completionListener: MediaPlayer.OnCompletionListener? = null

    private var errorListener: MediaPlayer.OnErrorListener? = null

    /**
     * 设置 MediaPlayer 的 OnPreparedListener
     */
    fun setOnPreparedListener(preparedListener: MediaPlayer.OnPreparedListener) {
        this.preparedListener = preparedListener
    }

    /**
     * 设置 MediaPlayer 的 OnSeekCompleteListener
     */
    fun setOnSeekCompleteListener(seekCompleteListener: MediaPlayer.OnSeekCompleteListener) {
        this.seekCompleteListener = seekCompleteListener
    }

    /**
     * 设置 MediaPlayer 的 OnCompletionListener
     */
    fun setOnCompletionListener(completionListener: MediaPlayer.OnCompletionListener) {
        this.completionListener = completionListener
    }

    /**
     * 设置 MediaPlayer 的 OnErrorListener
     */
    fun setOnErrorListener(errorListener: MediaPlayer.OnErrorListener) {
        this.errorListener = errorListener
    }

    /**
     * 播放音乐
     */
    fun play(song: SongBean) {
        this.currentSong = song
        _currentProgress.value = 0
        _songDuration.value = song.duration.toInt()
        timer?.pause()
        timer = null
        timer = Timer(0, currentSong.duration.toInt()).apply {
            setOnFinishedListener {
                this@SongPlayer.seekTo(song.startPosition.toInt())
                completionListener?.onCompletion(mediaPlayer)
            }
            setOnUpdateListener {
                _currentProgress.value = it
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
                seekCompleteListener?.onSeekComplete(it)
            }
            setOnPreparedListener {
                it.seekTo(song.startPosition.toInt())
                this@SongPlayer.start()
                preparedListener?.onPrepared(it)
            }
            setOnCompletionListener {
                _currentProgress.value = _songDuration.value
            }
            setOnErrorListener { mediaPlayer, i, i2 ->
                timer?.pause()
                _pause.value = true
                errorListener?.onError(mediaPlayer, i, i2) ?: false
            }
            try {
                setDataSource(song.data)
                prepareAsync()
            } catch (_: Exception) {
                completionListener?.onCompletion(mediaPlayer)
            }
        }
    }

    /**
     * 开始播放音乐
     */
    fun start() {
        timer?.start()
        _pause.value = false
        mediaPlayer.start()
    }

    /**
     * 暂停播放音乐
     */
    fun pause() {
        timer?.pause()
        _pause.value = true
        mediaPlayer.pause()
    }

    /**
     * 设置音乐播放进度
     */
    fun seekTo(position: Int) {
        _currentProgress.value = position
        val currentPosition = when (currentSong.startPosition) {
            0L -> position
            else -> position + currentSong.startPosition
        }.toInt()
        mediaPlayer.seekTo(currentPosition)
    }

    /**
     * 释放 MediaPlayer 资源
     */
    fun release() {
        timer?.pause()
        timer = null
        _pause.value = false
        _currentProgress.value = 0
        _songDuration.value = 0
        mediaPlayer.release()
    }

    fun getAudioSessionId(): Int {
        return mediaPlayer.audioSessionId
    }

    /**
     * 定时器内部类
     */
    private class Timer(startTime: Int, private val endTime: Int) {
        private var job: Job? = null
        private var currentTime = startTime
        private var onUpdateListener: ((Int) -> Unit)? = null
        private var onFinishedListener: (() -> Unit)? = null
        private val updateTimeout = 200

        /**
         * 设置更新监听器
         */
        fun setOnUpdateListener(listener: (Int) -> Unit) {
            onUpdateListener = listener
        }

        /**
         * 设置完成监听器
         */
        fun setOnFinishedListener(listener: () -> Unit) {
            onFinishedListener = listener
        }

        /**
         * 启动定时器
         */
        @OptIn(DelicateCoroutinesApi::class)
        fun start() {
            job?.cancel()
            job = GlobalScope.launch(Dispatchers.Main) {
                flow {
                    var time = currentTime
                    while (time < endTime) {
                        delay(updateTimeout.toLong())
                        time += updateTimeout
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
         * 暂停定时器
         */
        fun pause() {
            job?.cancel()
            job = null
        }

        /**
         * 设置当前时间并重新启动定时器
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