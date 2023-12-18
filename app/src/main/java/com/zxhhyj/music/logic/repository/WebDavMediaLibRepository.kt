package com.zxhhyj.music.logic.repository

import android.os.Build
import com.funny.data_saver.core.mutableDataSaverListStateOf
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.bean.WebDavSource
import com.zxhhyj.music.logic.config.DataSaverUtils
import com.zxhhyj.music.logic.utils.SardineUtils.toSardine
import com.zxhhyj.music.logic.utils.toSongBeanWebDav
import com.zxhhyj.music.service.playermanager.PlayerManager.MediaCacheManager
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

object WebDavMediaLibRepository {

    /**
     * 全部歌曲的集合
     */
    var songs by mutableDataSaverListStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "webdav_songs_v2",
        initialValue = listOf<SongBean.WebDav>()
    )
        private set

    /**
     * WebDav账户配置
     */
    var WebDavSources by mutableDataSaverListStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "WebDavAccountConfigs",
        initialValue = listOf<WebDavSource>()
    )
        private set

    /**
     * 创建一个[WebDavSource]并返回它在[WebDavSources]中的下标
     */
    fun createWebSource(): Int {
        WebDavSources = WebDavSources.toMutableList().apply {
            add(WebDavSource(String(), String(), String(), String(), emptyList()))
        }
        return WebDavSources.lastIndex
    }

    /**
     * 从[WebDavSources]中删除对应下标[index]的元素
     */
    fun deleteWebSource(index: Int) {
        WebDavSources = WebDavSources.toMutableList().apply {
            removeAt(index)
        }
    }

    /**
     * 向指定的WebDavSource对象添加一个文件夹
     */
    fun WebDavSource.addFolder(folder: String) {
        val index = WebDavSources.indexOf(this)
        if (index != -1) {
            WebDavSources = WebDavSources.toMutableList().apply {
                set(
                    index,
                    copy(folders = folders.toMutableList().apply {
                        add(folder)
                    })
                )
            }
        }
    }

    /**
     * 从指定的WebDavSource对象中删除一个文件夹
     */
    fun WebDavSource.removeFolder(folder: String) {
        val index = WebDavSources.indexOf(this)
        if (index != -1) {
            WebDavSources = WebDavSources.toMutableList().apply {
                set(
                    index,
                    copy(folders = folders.toMutableList().apply {
                        remove(folder)
                    })
                )
            }
        }
    }

    /**
     * 替换指定下标的WebDavSource对象
     */
    fun replace(index: Int, webDavSource: WebDavSource) {
        WebDavSources = WebDavSources.toMutableList().apply {
            set(index, webDavSource)
        }
    }

    /**
     * 此函数仅允许[MediaCacheManager]缓存歌曲完成后调用！！！
     * 谨记
     */
    fun replaceSong(song: SongBean.WebDav) {
        songs = songs.toMutableList().apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                replaceAll {
                    if (it.data == song.data && it.webDavSource == song.webDavSource) song else it
                }
            }
        }
    }

    /**
     * 从歌曲列表中移除指定的歌曲对象
     */
    fun removeSong(song: SongBean.WebDav) {
        val index = songs.indexOf(song)
        if (index != -1) {
            songs = songs.toMutableList().apply {
                set(
                    index, SongBean.WebDav(
                        webDavSource = song.webDavSource,
                        coverUrl = null,
                        albumName = null,
                        artistName = null,
                        duration = null,
                        data = song.data,
                        dateModified = song.dateModified,
                        songName = song.data.substringAfterLast("/"),
                        size = song.size,
                        bitrate = null,
                        samplingRate = null,
                        lyric = null,
                    )
                )
            }
        }
    }

    /**
     * 扫描媒体文件，从WebDav账户中获取歌曲信息，并将其添加到歌曲列表中。
     */
    suspend fun scanMedia() {
        withContext(Dispatchers.IO) {
            val newSongs = mutableListOf<SongBean.WebDav>()
            val jobs = mutableListOf<Deferred<List<SongBean.WebDav>>>()

            WebDavSources.forEach { source ->
                val sardine = source.toSardine()
                (source.folders + listOf(source.address)).forEach { folder ->
                    val job = async {
                        val fileList = mutableListOf<SongBean.WebDav>()
                        try {
                            val files = sardine.list(folder)
                            for (file in files) {
                                if (!file.isDirectory && containsSongExtension(file.name)) {
                                    val data = folder + file.name
                                    fileList.add(
                                        MediaCacheManager.getCacheFile(data)
                                            ?.toSongBeanWebDav(source, data)
                                            ?: SongBean.WebDav(
                                                webDavSource = source,
                                                coverUrl = null,
                                                albumName = null,
                                                artistName = null,
                                                duration = null,
                                                data = data,
                                                dateModified = file.modified.time,
                                                songName = file.name,
                                                size = file.contentLength,
                                                bitrate = null,
                                                samplingRate = null,
                                                lyric = null
                                            )
                                    )
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        fileList
                    }
                    jobs.add(job)
                }
            }

            newSongs.addAll(jobs.awaitAll().flatten())
            songs = newSongs
        }
    }

    /**
     * 检查文件名是否包含支持的音频格式。
     */
    private fun containsSongExtension(fileName: String): Boolean {
        val songExtensions = listOf(".mp3", ".flac")
        for (extension in songExtensions) {
            if (fileName.contains(extension, ignoreCase = true)) {
                return true
            }
        }
        return false
    }

    /**
     * 清空媒体库
     */
    fun clear() {
        songs = emptyList()
    }

}