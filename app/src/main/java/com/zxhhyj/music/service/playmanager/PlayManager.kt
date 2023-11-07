@file:Suppress("UNUSED")

package com.zxhhyj.music.service.playmanager

import android.util.Range
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zxhhyj.music.logic.bean.SongBean
import kotlin.random.Random

/**
 * 播放管理器
 */
object PlayManager {

    enum class PlayMode {
        SINGLE_LOOP, LIST_LOOP, RANDOM
    }

    /**
     * 播放模式
     */
    private val mPlayMode = MutableLiveData(PlayMode.LIST_LOOP)

    /**
     * MediaPlayer
     */
    private val mCueMediaPlayer = CueMediaPlayer()

    /**
     * 当前播放的歌曲
     */
    private val mCurrentSong = MutableLiveData<SongBean?>()

    /**
     * 播放列表
     */
    private val mPlayList = MutableLiveData<List<SongBean>?>()

    /**
     * 当前播放歌曲的下标
     */
    private val mIndex = MutableLiveData(0)

    /**
     * 获取播放模式的 LiveData
     */
    fun playModeLiveData(): LiveData<PlayMode> = mPlayMode

    /**
     * 获取播放列表的 LiveData
     */
    fun playListLiveData(): LiveData<List<SongBean>?> = mPlayList

    /**
     * 获取当前播放进度的 LiveData
     */
    fun progressLiveData(): LiveData<Int> = mCueMediaPlayer.currentProgress

    /**
     * 获取当前歌曲时长的 LiveData
     */
    fun durationLiveData(): LiveData<Int> = mCueMediaPlayer.songDuration

    /**
     * 获取当前播放的歌曲的 LiveData
     */
    fun currentSongLiveData(): LiveData<SongBean?> = mCurrentSong

    /**
     * 获取暂停状态的 LiveData
     */
    fun pauseLiveData(): LiveData<Boolean> = mCueMediaPlayer.pause

    /**
     * 修改播放模式
     */
    fun setPlayMode(playMode: PlayMode) {
        mPlayMode.value = playMode
    }

    /**
     * 播放歌曲列表中的指定歌曲
     */
    fun play(list: List<SongBean>, index: Int) {
        mPlayList.value = list
        updateIndex(index)
    }

    /**
     * 设置音乐播放进度
     */
    fun seekTo(position: Int) {
        mCueMediaPlayer.seekTo(position)
    }

    /**
     * 切换到上一首歌曲
     */
    fun skipToPrevious() {
        updateIndex(mIndex.value!! - 1)
    }

    /**
     * 切换到下一首歌曲
     */
    fun skipToNext() {
        when (mPlayMode.value) {
            PlayMode.SINGLE_LOOP -> {
                updateIndex(mIndex.value!!)
            }

            PlayMode.LIST_LOOP -> {
                updateIndex(mIndex.value!! + 1)
            }

            PlayMode.RANDOM -> {
                mPlayList.value?.let { playlist ->
                    if (playlist.size >= 2) {
                        val randomNumber = if (playlist.size == 2) {
                            if (mIndex.value == 0) 1 else 0 // 只有两首歌曲，直接选择另外一首
                        } else {
                            var newRandomNumber = Random.nextInt(0, playlist.size - 1)
                            while (newRandomNumber == mIndex.value) {
                                newRandomNumber = Random.nextInt(0, playlist.size - 1)
                            }
                            newRandomNumber
                        }
                        updateIndex(randomNumber)
                    }
                }
            }

            null -> {

            }
        }
    }

    /**
     * 开始播放音乐
     */
    fun start() {
        mCueMediaPlayer.start()
    }

    /**
     * 暂停播放音乐
     */
    fun pause() {
        mCueMediaPlayer.pause()
    }

    /**
     * 添加下一首播放的歌曲
     */
    fun addNextPlay(song: SongBean) {
        mPlayList.value?.let {
            mPlayList.value = it.toMutableList().apply {
                add((mIndex.value ?: return) + 1, song)
            }
        }
    }

    /**
     * 移除指定歌曲
     */
    fun removeSong(song: SongBean) {
        val currentSong = mCurrentSong.value
        if (currentSong == song) {
            skipToNext()
        }
        mPlayList.value = mPlayList.value?.minus(song)
    }

    /**
     * 清空播放列表
     */
    fun clearPlayList() {
        mCueMediaPlayer.pause()
        mCurrentSong.value = null
        mPlayList.value = null
    }

    init {
        // 观察当前播放歌曲的下标，如果播放列表不为空，则根据下标播放歌曲
        mIndex.observeForever {
            mPlayList.value?.let { list ->
                val song = list.getOrNull(it) ?: return@let
                mCurrentSong.value = song
                mCueMediaPlayer.play(song)
            }
        }
    }

    fun setEnableEqualizer(enabled: Boolean) {
        mCueMediaPlayer.setEnableEqualizer(enabled)
    }

    fun setBandLevel(band: Int, level: Int) {
        mCueMediaPlayer.setBandLevel(band, level)
    }

    fun getBandLevel(band: Int): Int {
        return mCueMediaPlayer.getBandLevel(band)
    }

    fun getBandRange(): Range<Int> {
        return mCueMediaPlayer.getBandRange()
    }

    fun getNumberOfBands(): Int {
        return mCueMediaPlayer.getNumberOfBands()
    }

    fun getBandFreqRange(band: Int): IntArray {
        return mCueMediaPlayer.getBandFreqRange(band)
    }

    private fun updateIndex(index: Int) {
        // 根据下标切换歌曲，如果下标越界，则暂停播放
        mPlayList.value?.getOrNull(index)?.let {
            mIndex.value = index
            return
        }
        pause()
    }

    init {
        // 设置播放器的完成监听器和错误监听器，用于切换到下一首歌曲
        mCueMediaPlayer.apply {
            setOnCompletionListener {
                skipToNext()
            }
            setOnErrorListener { _, _, _ ->
                skipToNext()
                true
            }
        }
    }

}