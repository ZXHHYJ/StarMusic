package com.zxhhyj.music.logic.repository

import android.provider.MediaStore
import com.funny.data_saver.core.mutableDataSaverListStateOf
import com.kyant.tag.Metadata
import com.zxhhyj.music.MainApplication
import com.zxhhyj.music.logic.bean.Folder
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.config.DataSaverUtils
import com.zxhhyj.music.logic.utils.CueParser
import com.zxhhyj.music.logic.utils.FileUtils
import com.zxhhyj.music.logic.utils.toMillis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


object AndroidMediaLibRepository {

    var songs by mutableDataSaverListStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "local_songs_v2",
        initialValue = listOf<SongBean.Local>()
    )
        private set

    var hideSongs by mutableDataSaverListStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "hide_songs",
        initialValue = listOf<SongBean.Local>()
    )
        private set

    var folders by mutableDataSaverListStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "folders",
        initialValue = listOf<Folder>()
    )
        private set

    var hideFolders by mutableDataSaverListStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "hide_folders",
        initialValue = listOf<Folder>()
    )
        private set

    /**
     * 扫描媒体
     */
    suspend fun scanMedia() {
        withContext(Dispatchers.IO) {
            val query = MainApplication.context.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                null
            )
            val scanSongs = mutableListOf<SongBean.Local>()
            query?.use { cursor ->
                val albumIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
                val albumIdIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
                val artistIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                val durationIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                val dataIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                val songNameIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                val sizeIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
                val idIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val dateModifiedIndex =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_MODIFIED)

                while (cursor.moveToNext()) {
                    val album = cursor.getString(albumIndex)
                    val albumId = cursor.getLong(albumIdIndex)
                    val artist = cursor.getString(artistIndex)
                    val duration = cursor.getLong(durationIndex)
                    val data = cursor.getString(dataIndex)
                    val songName = cursor.getString(songNameIndex)
                    val size = cursor.getLong(sizeIndex)
                    val id = cursor.getLong(idIndex)
                    val dateModified = query.getLong(dateModifiedIndex)
                    val metadata = Metadata.getMetadata(data)
                    val coverUrl = "content://media/external/audio/albumart/${albumId}"

                    if (SettingRepository.EnableCueSupport) {
                        runCatching {
                            val cuePath = data.substringBeforeLast(".") + ".cue"
                            val cueContent = FileUtils.readFromFile(cuePath)
                            val cueData = CueParser.parseCueContent(cueContent)
                            cueData.tracks.forEach { track ->
                                val endPosition =
                                    track.endPosition.toMillis().takeIf { it != 0L } ?: duration
                                val startPosition = track.startPosition.toMillis()
                                scanSongs.add(
                                    SongBean.Local(
                                        coverUrl = coverUrl,
                                        album = SongBean.Album(album),
                                        artist = SongBean.Artist(track.performer),
                                        duration = endPosition - startPosition,
                                        data = data,
                                        dateModified = dateModified,
                                        songName = track.title,
                                        size = size,
                                        id = id,
                                        bitrate = metadata?.bitrate,
                                        samplingRate = metadata?.sampleRate,
                                        lyric = metadata?.properties?.get("LYRICS")?.get(0),
                                        startPosition = startPosition,
                                        endPosition = endPosition,
                                    )
                                )
                            }
                        }.getOrElse {
                            scanSongs.add(
                                SongBean.Local(
                                    coverUrl = coverUrl,
                                    album = SongBean.Album(album),
                                    artist = SongBean.Artist(artist),
                                    duration = duration,
                                    data = data,
                                    dateModified = dateModified,
                                    songName = songName,
                                    size = size,
                                    id = id,
                                    bitrate = metadata?.bitrate,
                                    samplingRate = metadata?.sampleRate,
                                    lyric = metadata?.properties?.get("LYRICS")?.get(0),
                                    startPosition = 0,
                                    endPosition = duration,
                                )
                            )
                        }
                    } else {
                        scanSongs.add(
                            SongBean.Local(
                                coverUrl = coverUrl,
                                album = SongBean.Album(album),
                                artist = SongBean.Artist(artist),
                                duration = duration,
                                data = data,
                                dateModified = dateModified,
                                songName = songName,
                                size = size,
                                id = id,
                                bitrate = metadata?.bitrate,
                                samplingRate = metadata?.sampleRate,
                                lyric = metadata?.properties?.get("LYRICS")?.get(0),
                                startPosition = 0,
                                endPosition = duration,
                            )
                        )
                    }
                }
            }
            query?.close()
            scanSongs.removeAll(hideSongs)
            songs = scanSongs
            updateLibs()
        }
    }

    private fun updateLibs() {
        folders = songs
            .map { it.data.substringBeforeLast("/") }
            .distinct()
            .map { path ->
                Folder(path, songs.filter { it.data.startsWith(path) })
            }
            .filter { it !in hideFolders }

        refreshSongs()
    }

    private fun refreshSongs() {
        songs = folders
            .flatMap { it.songs }
            .filter {
                if (SettingRepository.EnableExcludeSongsUnderOneMinute) it.duration > 60000 else true
            }
    }

    fun hideFolder(folder: Folder) {
        if (folders.any { it.path == folder.path }) {
            folders = folders - folder
            hideFolders = hideFolders + folder
            refreshSongs()
        }
    }

    fun unHideFolder(folder: Folder) {
        if (hideFolders.any { it.path == folder.path }) {
            folders = folders + folder
            hideFolders = hideFolders - folder
            refreshSongs()
        }
    }

    /**
     * 从媒体库中隐藏某个歌曲
     */
    fun hideSong(song: SongBean.Local) {
        songs = songs - song
        hideSongs = hideSongs + song
    }

    /**
     * 取消隐藏某个歌曲
     */
    fun unHideSong(song: SongBean.Local) {
        hideSongs = hideSongs - song
        songs = songs + song
    }

    /**
     * 从媒体库中删除某个歌曲
     */
    fun delete(song: SongBean.Local) {
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