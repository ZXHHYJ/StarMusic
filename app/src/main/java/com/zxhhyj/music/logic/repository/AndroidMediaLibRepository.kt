package com.zxhhyj.music.logic.repository

import android.provider.MediaStore
import com.funny.data_saver.core.mutableDataSaverListStateOf
import com.zxhhyj.music.MainApplication
import com.zxhhyj.music.logic.bean.Folder
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.config.DataSaverUtils
import com.zxhhyj.music.logic.utils.toSongBeanLocal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Android 媒体库仓库类，用于管理媒体库中的歌曲和文件夹数据。
 */
object AndroidMediaLibRepository {

    /**
     * 歌曲列表
     */
    var songs by mutableDataSaverListStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "local_songs_v3",
        initialValue = listOf<SongBean.Local>()
    )
        private set

    /**
     * 隐藏的歌曲列表
     */
    var hideSongs by mutableDataSaverListStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "hide_songs",
        initialValue = listOf<SongBean.Local>()
    )
        private set

    /**
     * 文件夹列表
     */
    var folders by mutableDataSaverListStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "folders",
        initialValue = listOf<Folder>()
    )
        private set

    /**
     * 隐藏的文件夹列表
     */
    var hideFolders by mutableDataSaverListStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "hide_folders",
        initialValue = listOf<Folder>()
    )
        private set

    /**
     * 扫描媒体库中的歌曲
     */
    suspend fun scanMedia() {
        withContext(Dispatchers.IO) {
            val scanSongs = mutableListOf<SongBean.Local>()
            val formatCollection = listOf(
                "audio/x-wav",
                "audio/ogg",
                "audio/aac",
                "audio/midi"
            )
            val selectionBuilder = StringBuilder("${MediaStore.Audio.Media.IS_MUSIC}!=0")
            for (i in formatCollection) {
                selectionBuilder.append(" or ${MediaStore.Audio.Media.MIME_TYPE}='$i'")
            }
            val selection = selectionBuilder.toString()
            val query = MainApplication.context.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                selection,
                null,
                null
            )
            query?.use { cursor ->
                while (cursor.moveToNext()) {
                    cursor.toSongBeanLocal()?.let {
                        scanSongs.add(it)
                    }
                }
            }
            scanSongs.removeAll(hideSongs)
            songs = scanSongs
            updateLibs()
        }
    }

    /**
     * 更新文件夹和歌曲列表
     */
    private fun updateLibs() {
        hideFolders = hideFolders.mapNotNull {
            if (it.songs.isEmpty()) null else it
        }.distinctBy { it.path }

        folders = songs
            .map { it.data.substringBeforeLast("/") }
            .distinct()
            .map { path ->
                Folder(path, songs.filter { it.data.startsWith(path) })
            }
            .filter { it !in hideFolders }

        refreshSongs()
    }

    /**
     * 刷新歌曲列表
     */
    private fun refreshSongs() {
        songs = folders
            .flatMap { it.songs }
            .filter {
                if (SettingRepository.EnableExcludeSongsUnderOneMinute && it.duration != null) it.duration > 60000 else true
            }
            .distinctBy { it.data }
    }

    /**
     * 隐藏指定文件夹
     *
     * @param folder 要隐藏的文件夹
     */
    fun hideFolder(folder: Folder) {
        if (folders.any { it.path == folder.path }) {
            folders = folders - folder
            hideFolders = hideFolders + folder
            refreshSongs()
        }
    }

    /**
     * 取消隐藏指定文件夹
     *
     * @param folder 要取消隐藏的文件夹
     */
    fun unHideFolder(folder: Folder) {
        if (hideFolders.any { it.path == folder.path }) {
            folders = folders + folder
            hideFolders = hideFolders - folder
            refreshSongs()
        }
    }

    /**
     * 隐藏指定歌曲
     *
     * @param song 要隐藏的歌曲
     */
    fun hideSong(song: SongBean.Local) {
        songs = songs - song
        hideSongs = hideSongs + song
    }

    /**
     * 取消隐藏指定歌曲
     *
     * @param song 要取消隐藏的歌曲
     */
    fun unHideSong(song: SongBean.Local) {
        hideSongs = hideSongs - song
        songs = songs + song
    }

    /**
     * 添加歌曲到媒体库
     *
     * @param song 要添加的歌曲
     */
    fun addSong(song: SongBean.Local) {
        songs = songs + song
        hideSongs = hideSongs - song
        updateLibs()
    }

    /**
     * 从媒体库中移除指定歌曲
     *
     * @param song 要移除的歌曲
     */
    fun removeSong(song: SongBean.Local) {
        songs = songs - song
        hideSongs = hideSongs - song
        updateLibs()
    }

    /**
     * 清空媒体库
     */
    fun clear() {
        songs = emptyList()
        hideSongs = emptyList()
        folders = emptyList()
        hideFolders = emptyList()
    }

}