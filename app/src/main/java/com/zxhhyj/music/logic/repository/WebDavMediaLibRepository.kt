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

    var songs by mutableDataSaverListStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "webdav_songs_v2",
        initialValue = listOf<SongBean.WebDav>()
    )
        private set

    var WebDavSources by mutableDataSaverListStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "WebDavAccountConfigs",
        initialValue = listOf<WebDavSource>()
    )
        private set

    fun createWebSource(): Int {
        val newWebDavSource = WebDavSource(
            String(),
            String(),
            String(),
            String(),
            emptyList()
        )
        WebDavSources += newWebDavSource
        return WebDavSources.lastIndex
    }

    fun deleteWebSource(index: Int) {
        WebDavSources = WebDavSources.toMutableList().apply { removeAt(index) }
    }

    fun WebDavSource.addFolder(folder: String) {
        val index = WebDavSources.indexOf(this)
        if (index != -1) {
            WebDavSources = WebDavSources.toMutableList().apply {
                set(
                    index,
                    copy(folders = folders.toMutableList() + folder)
                )
            }
        }
    }

    fun WebDavSource.removeFolder(folder: String) {
        val index = WebDavSources.indexOf(this)
        if (index != -1) {
            WebDavSources = WebDavSources.toMutableList().apply {
                set(
                    index,
                    copy(folders = folders.toMutableList().apply { remove(folder) })
                )
            }
        }
    }

    fun replace(index: Int, webDavSource: WebDavSource) {
        WebDavSources = WebDavSources.toMutableList().apply { set(index, webDavSource) }
    }

    fun replaceSong(song: SongBean.WebDav) {
        songs = songs.toMutableList().apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                replaceAll { if (it.data == song.data && it.webDavSource == song.webDavSource) song else it }
            }
        }
    }

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


    private fun containsSongExtension(fileName: String): Boolean {
        val songExtensions = listOf(".mp3", ".flac")
        return songExtensions.any { fileName.contains(it, ignoreCase = true) }
    }

}