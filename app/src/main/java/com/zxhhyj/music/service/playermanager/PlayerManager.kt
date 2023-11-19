@file:Suppress("UNUSED")

package com.zxhhyj.music.service.playermanager

import android.util.Range
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zxhhyj.music.MainApplication
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.ui.common.ComposeToast
import java.io.FileNotFoundException
import kotlin.random.Random

/**
 * 播放管理器
 */
object PlayerManager {

    enum class PlayMode {
        SINGLE_LOOP, LIST_LOOP, RANDOM
    }

    /**
     * 播放模式
     */
    private val playModeLiveData = MutableLiveData(PlayMode.LIST_LOOP)

    /**
     * MediaPlayer
     */
    private val cueMediaPlayer = CueMediaPlayer()

    /**
     * 当前播放的歌曲
     */
    private val currentSongLiveData = MutableLiveData<SongBean?>()

    /**
     * 播放列表
     */
    private val playListLiveData = MutableLiveData<List<SongBean>?>()

    /**
     * 当前播放歌曲的下标
     */
    private val indexLiveData = MutableLiveData(0)

    /**
     * 获取播放模式的 LiveData
     */
    fun playModeLiveData(): LiveData<PlayMode> = playModeLiveData

    /**
     * 获取播放列表的 LiveData
     */
    fun playListLiveData(): LiveData<List<SongBean>?> = playListLiveData

    /**
     * 获取当前播放进度的 LiveData
     */
    fun progressLiveData(): LiveData<Int> = cueMediaPlayer.currentProgress

    /**
     * 获取当前歌曲时长的 LiveData
     */
    fun durationLiveData(): LiveData<Int> = cueMediaPlayer.songDuration

    /**
     * 获取当前播放的歌曲的 LiveData
     */
    fun currentSongLiveData(): LiveData<SongBean?> = currentSongLiveData

    /**
     * 获取暂停状态的 LiveData
     */
    fun pauseLiveData(): LiveData<Boolean> = cueMediaPlayer.pause

    /**
     * 修改播放模式
     */
    fun setPlayMode(playMode: PlayMode) {
        playModeLiveData.value = playMode
    }

    /**
     * 播放歌曲列表中的指定歌曲
     */
    fun play(list: List<SongBean>, index: Int) {
        playListLiveData.value = list
        updateIndex(index)
    }

    /**
     * 设置音乐播放进度
     */
    fun seekTo(position: Int) {
        cueMediaPlayer.seekTo(position)
    }

    /**
     * 切换到上一首歌曲
     */
    fun skipToPrevious() {
        updateIndex(indexLiveData.value!! - 1)
    }

    /**
     * 切换到下一首歌曲
     */
    fun skipToNext() {
        when (playModeLiveData.value) {
            PlayMode.SINGLE_LOOP -> {
                updateIndex(indexLiveData.value!!)
            }

            PlayMode.LIST_LOOP -> {
                updateIndex(indexLiveData.value!! + 1)
            }

            PlayMode.RANDOM -> {
                playListLiveData.value?.let { playlist ->
                    if (playlist.size >= 2) {
                        val randomNumber = if (playlist.size == 2) {
                            if (indexLiveData.value == 0) 1 else 0 // 只有两首歌曲，直接选择另外一首
                        } else {
                            var newRandomNumber = Random.nextInt(0, playlist.size - 1)
                            while (newRandomNumber == indexLiveData.value) {
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
        cueMediaPlayer.start()
    }

    /**
     * 暂停播放音乐
     */
    fun pause() {
        cueMediaPlayer.pause()
    }

    /**
     * 添加下一首播放的歌曲
     */
    fun addNextPlay(song: SongBean) {
        playListLiveData.value?.let {
            playListLiveData.value = it.toMutableList().apply {
                add((indexLiveData.value ?: return) + 1, song)
            }
        }
    }

    /**
     * 移除指定歌曲
     */
    fun removeSong(song: SongBean) {
        val currentSong = currentSongLiveData.value
        if (currentSong == song) {
            skipToNext()
        }
        playListLiveData.value = playListLiveData.value?.minus(song)
    }

    /**
     * 清空播放列表
     */
    fun clearPlayList() {
        cueMediaPlayer.pause()
        currentSongLiveData.value = null
        playListLiveData.value = null
    }

    init {
        // 观察当前播放歌曲的下标，如果播放列表不为空，则根据下标播放歌曲
        indexLiveData.observeForever {
            playListLiveData.value?.let { list ->
                val song = list.getOrNull(it) ?: return@let
                currentSongLiveData.value = song
                cueMediaPlayer.play(song)
            }
        }
    }

    fun setEnableEqualizer(enabled: Boolean) {
        cueMediaPlayer.setEnableEqualizer(enabled)
    }

    fun setBandLevel(band: Int, level: Int) {
        cueMediaPlayer.setBandLevel(band, level)
    }

    fun getBandLevel(band: Int): Int {
        return cueMediaPlayer.getBandLevel(band)
    }

    fun getBandRange(): Range<Int> {
        return cueMediaPlayer.getBandRange()
    }

    fun getNumberOfBands(): Int {
        return cueMediaPlayer.getNumberOfBands()
    }

    fun getBandFreqRange(band: Int): IntArray {
        return cueMediaPlayer.getBandFreqRange(band)
    }

    private fun updateIndex(index: Int) {
        // 根据下标切换歌曲，如果下标越界，则暂停播放
        playListLiveData.value?.getOrNull(index)?.let {
            indexLiveData.value = index
            return
        }
        pause()
    }

    init {
        cueMediaPlayer.apply {
            setOnCompletionListener {
                skipToNext()
            }
            errorListener = {
                when (it) {
                    is FileNotFoundException -> {
                        ComposeToast.postErrorToast(MainApplication.context.getString(R.string.song_file_missing))
                    }

                    else -> {
                        ComposeToast.postErrorToast(MainApplication.context.getString(R.string.unknown_error))
                    }
                }
                pause()
            }
        }
    }

}