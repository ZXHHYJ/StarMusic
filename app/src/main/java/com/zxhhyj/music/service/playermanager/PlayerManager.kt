@file:Suppress("UNUSED")

package com.zxhhyj.music.service.playermanager

import android.util.Range
import com.zxhhyj.music.MainApplication
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.ui.common.ComposeToast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.FileNotFoundException
import kotlin.random.Random

/**
 * 播放管理器
 */
object PlayerManager {

    enum class PlayMode {
        SINGLE_LOOP, LIST_LOOP, RANDOM
    }

    private val cueMediaPlayer = CueMediaPlayer()

    private val _playModeFlow = MutableStateFlow(PlayMode.LIST_LOOP)
    val playModeFlow: StateFlow<PlayMode> = _playModeFlow

    private val _currentSongFlow = MutableStateFlow<SongBean?>(null)
    val currentSongFlow: StateFlow<SongBean?> = _currentSongFlow

    private val _playListFlow = MutableStateFlow<List<SongBean>?>(null)
    val playListFlow: StateFlow<List<SongBean>?> = _playListFlow

    private val _indexFlow = MutableStateFlow<Int?>(null)
    val indexFlow: StateFlow<Int?> = _indexFlow

    val durationFlow = cueMediaPlayer.songDurationFlow

    val progressFlow = cueMediaPlayer.currentProgressFlow

    val pauseFlow = cueMediaPlayer.pauseFlow

    fun setPlayMode(playMode: PlayMode) {
        _playModeFlow.value = playMode
    }

    /**
     * 播放歌曲列表中的指定歌曲
     */
    fun play(list: List<SongBean>, index: Int) {
        _playListFlow.value = list
        val song = _playListFlow.value?.getOrNull(index)
        _indexFlow.value = index
        song?.let {
            _currentSongFlow.value = it
            cueMediaPlayer.preparePlay(it)
        }
    }

    fun install(list: List<SongBean>, index: Int) {
        _playListFlow.value = list
        list.getOrNull(index)?.let {
            _indexFlow.value = index
            _currentSongFlow.value = it
            cueMediaPlayer.prepare(it)
        }
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
        val index = _indexFlow.value?.let { it - 1 } ?: return
        val song = _playListFlow.value?.getOrNull(index)
        song?.let {
            _indexFlow.value = index
            _currentSongFlow.value = it
            cueMediaPlayer.preparePlay(it)
        }
    }

    /**
     * 切换到下一首歌曲
     */
    fun skipToNext() {
        when (_playModeFlow.value) {
            PlayMode.SINGLE_LOOP -> {
                _currentSongFlow.value?.startPosition?.let {
                    seekTo(it.toInt())
                    start()
                }
            }

            PlayMode.LIST_LOOP -> {
                val index = _indexFlow.value!! + 1
                val song = _playListFlow.value?.getOrNull(index)
                song?.let {
                    _indexFlow.value = index
                    _currentSongFlow.value = it
                    cueMediaPlayer.preparePlay(it)
                }
            }

            PlayMode.RANDOM -> {
                _playListFlow.value?.let { playlist ->
                    if (playlist.size >= 2) {
                        val randomNumber = if (playlist.size == 2) {
                            if (_indexFlow.value == 0) 1 else 0
                        } else {
                            var newRandomNumber: Int
                            do {
                                newRandomNumber = Random.nextInt(0, playlist.size - 1)
                            } while (newRandomNumber == _indexFlow.value)
                            newRandomNumber
                        }
                        val song = _playListFlow.value?.getOrNull(randomNumber)
                        _indexFlow.value = randomNumber
                        song?.let {
                            _currentSongFlow.value = it
                            cueMediaPlayer.preparePlay(it)
                        }
                    }
                }
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
        _playListFlow.value = _playListFlow.value?.toMutableList()?.apply {
            add((_indexFlow.value ?: return) + 1, song)
        }
    }

    /**
     * 移除指定歌曲
     */
    fun removeSong(songIndex: Int) {
        val song = _playListFlow.value?.get(songIndex) ?: return
        _currentSongFlow.value?.takeIf { it == song }?.run {
            skipToNext()
        }
        _playListFlow.value = _playListFlow.value?.minus(song)
        when {
            songIndex == _indexFlow.value -> {

            }

            songIndex > _indexFlow.value!! -> {

            }

            songIndex < _indexFlow.value!! -> {
                _indexFlow.value = (_indexFlow.value!! - 1).coerceAtLeast(0)
            }
        }
    }

    /**
     * 清空播放列表
     */
    fun clearPlayList() {
        cueMediaPlayer.pause()
        _currentSongFlow.value = null
        _playListFlow.value = null
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

    init {
        cueMediaPlayer.apply {
            completionListener = {
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