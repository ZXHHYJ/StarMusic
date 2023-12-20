@file:Suppress("UNUSED")

package com.zxhhyj.music.service.playermanager

import android.util.Range
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.danikula.videocache.CacheListener
import com.danikula.videocache.HttpProxyCacheServer
import com.danikula.videocache.headers.HeaderInjector
import com.thegrizzlylabs.sardineandroid.util.SardineUtil
import com.zxhhyj.mediaplayer.MediaPlayer
import com.zxhhyj.mediaplayer.impl.DataSource
import com.zxhhyj.music.MainApplication
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.config.musicFilesDir
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.logic.repository.WebDavMediaLibRepository
import com.zxhhyj.music.logic.utils.toSongBeanWebDav
import com.zxhhyj.music.ui.common.ComposeToast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.Credentials
import java.io.File
import kotlin.random.Random

/**
 * 播放管理器
 */
object PlayerManager {

    object MediaCacheManager {

        object WebDavHeadersInjector : HeaderInjector {
            override fun addHeaders(url: String): Map<String, String> {
                val webDavSongBean = _currentSongFlow.value as SongBean.WebDav?
                webDavSongBean?.let {
                    return hashMapOf(
                        Pair(
                            "Authorization",
                            Credentials.basic(
                                it.webDavSource.username,
                                it.webDavSource.password,
                                SardineUtil.standardUTF8()
                            )
                        )
                    )
                }
                return emptyMap()
            }
        }

        private val proxy by lazy {
            HttpProxyCacheServer.Builder(MainApplication.context)
                .cacheDirectory(musicFilesDir)
                .maxCacheSize((SettingRepository.AndroidVideoCacheSize * 1024 * 1024).toLong())
                .headerInjector(WebDavHeadersInjector)
                .build()
        }

        fun isCached(url: String) = proxy.isCached(url)

        fun getCacheFile(url: String): File? {
            if (proxy.isCached(url)) {
                return proxy.getProxyUrl(url).toUri().toFile()
            }
            return null
        }

        private val cacheListener = CacheListener { cacheFile, url, _ ->
            if (!cacheFile.path.endsWith(".download")) {
                (_currentSongFlow.value as SongBean.WebDav?)?.let { webDavSongBean ->
                    cacheFile.toSongBeanWebDav(
                        webDavSongBean.webDavSource,
                        url
                    )?.let { songBean ->
                        WebDavMediaLibRepository.replaceSong(songBean)
                        _playListFlow.value = _playListFlow.value?.toMutableList()?.apply {
                            _indexFlow.value?.let {
                                removeAt(it)
                                add(it, songBean)
                            }
                        }
                        _currentSongFlow.value = songBean
                    }
                }
            }
        }

        object WebDavProxySource : DataSource {
            override fun getDataSource(data: String): String {
                val file = File(data)
                return if (file.exists()) {
                    data
                } else {
                    proxy.registerCacheListener(cacheListener, data)
                    proxy.getProxyUrl(data)
                }
            }
        }
    }

    enum class PlayMode {
        SINGLE_LOOP, LIST_LOOP, RANDOM
    }

    private val mediaPlayer =
        MediaPlayer(MainApplication.context, MediaCacheManager.WebDavProxySource)

    private val _playModeFlow = MutableStateFlow(PlayMode.LIST_LOOP)
    val playModeFlow: StateFlow<PlayMode> = _playModeFlow

    private val _currentSongFlow = MutableStateFlow<SongBean?>(null)
    val currentSongFlow: StateFlow<SongBean?> = _currentSongFlow

    private val _playListFlow = MutableStateFlow<List<SongBean>?>(null)
    val playListFlow: StateFlow<List<SongBean>?> = _playListFlow

    private val _indexFlow = MutableStateFlow<Int?>(null)
    val indexFlow: StateFlow<Int?> = _indexFlow

    val durationFlow = mediaPlayer.songDurationFlow

    val progressFlow = mediaPlayer.currentProgressFlow

    val pauseFlow = mediaPlayer.pauseFlow

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
            mediaPlayer.preparePlay(it.data)
        }
    }

    fun install(list: List<SongBean>, index: Int) {
        _playListFlow.value = list
        list.getOrNull(index)?.let {
            _indexFlow.value = index
            _currentSongFlow.value = it
            mediaPlayer.prepare(it.data)
        }
    }

    /**
     * 设置音乐播放进度
     */
    fun seekTo(position: Int) {
        mediaPlayer.seekTo(position)
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
            mediaPlayer.preparePlay(it.data)
        }
    }

    /**
     * 切换到下一首歌曲
     */
    fun skipToNext() {
        when (_playModeFlow.value) {
            PlayMode.SINGLE_LOOP -> {
                seekTo(0)
                start()
            }

            PlayMode.LIST_LOOP -> {
                val index = _indexFlow.value!! + 1
                val song = _playListFlow.value?.getOrNull(index)
                song?.let {
                    _indexFlow.value = index
                    _currentSongFlow.value = it
                    mediaPlayer.preparePlay(it.data)
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
                            mediaPlayer.preparePlay(it.data)
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
        mediaPlayer.start()
    }

    /**
     * 暂停播放音乐
     */
    fun pause() {
        mediaPlayer.pause()
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
        mediaPlayer.pause()
        _currentSongFlow.value = null
        _playListFlow.value = null
    }

    fun setEnableEqualizer(enabled: Boolean) {
        mediaPlayer.setEnableEqualizer(enabled)
    }

    fun setBandLevel(band: Int, level: Int) {
        mediaPlayer.setBandLevel(band, level)
    }

    fun getBandLevel(band: Int): Int {
        return mediaPlayer.getBandLevel(band)
    }

    fun getBandRange(): Range<Int> {
        return mediaPlayer.getBandRange()
    }

    fun getNumberOfBands(): Int {
        return mediaPlayer.getNumberOfBands()
    }

    fun getBandFreqRange(band: Int): IntArray {
        return mediaPlayer.getBandFreqRange(band)
    }

    init {
        mediaPlayer.apply {
            completionListener = {
                skipToNext()
            }
            errorListener = {
                ComposeToast.postErrorToast(it.errorCodeName)
                pause()
            }
        }
    }

}