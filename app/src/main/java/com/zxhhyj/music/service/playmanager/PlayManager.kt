package com.zxhhyj.music.service.playmanager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zxhhyj.music.logic.bean.SongBean

/**
 * 播放管理器
 */
object PlayManager {

    private var mSongPlayer = SongPlayer()

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
     * 获取播放列表的 LiveData
     */
    fun playListLiveData(): LiveData<List<SongBean>?> {
        return mPlayList
    }

    /**
     * 获取当前播放进度的 LiveData
     */
    fun progressLiveData(): LiveData<Int> {
        return mSongPlayer.currentProgress
    }

    /**
     * 获取当前歌曲时长的 LiveData
     */
    fun durationLiveData(): LiveData<Int> {
        return mSongPlayer.songDuration
    }

    /**
     * 获取当前播放的歌曲的 LiveData
     */
    fun currentSongLiveData(): LiveData<SongBean?> {
        return mCurrentSong
    }

    /**
     * 获取暂停状态的 LiveData
     */
    fun pauseLiveData(): LiveData<Boolean> {
        return mSongPlayer.pause
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
        mSongPlayer.seekTo(position)
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
        updateIndex(mIndex.value!! + 1)
    }

    /**
     * 开始播放音乐
     */
    fun start() {
        mSongPlayer.start()
    }

    /**
     * 暂停播放音乐
     */
    fun pause() {
        mSongPlayer.pause()
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
        stop()
        mCurrentSong.value = null
        mPlayList.value = null
    }

    init {
        // 观察当前播放歌曲的下标，如果播放列表不为空，则根据下标播放歌曲
        mIndex.observeForever {
            mPlayList.value?.let { list ->
                val song = list.getOrNull(it) ?: return@let
                mCurrentSong.value = song
                mSongPlayer.play(song)
            }
        }
    }

    private fun updateIndex(index: Int) {
        // 根据下标切换歌曲，如果下标越界，则暂停播放
        mPlayList.value?.getOrNull(index)?.let {
            mIndex.value = index
            return
        }
        pause()
    }

    /**
     * 停止播放音乐
     */
    fun stop() {
        mCurrentSong.value = null
        mSongPlayer.stop()
    }

    init {
        // 设置播放器的完成监听器和错误监听器，用于切换到下一首歌曲
        mSongPlayer.apply {
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