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


object AndroidMediaLibsRepository {

    var artists by mutableDataSaverListStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "local_artists",
        initialValue = listOf<SongBean.Artist>()
    )
        private set

    var albums by mutableDataSaverListStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "local_albums",
        initialValue = listOf<SongBean.Album>()
    )
        private set

    var songs by mutableDataSaverListStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "local_songs",
        initialValue = listOf<SongBean>()
    )
        private set

    var folders by mutableDataSaverListStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "folders",
        initialValue = listOf<Folder>()
    )
        private set

    var hideSongs by mutableDataSaverListStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "hide_songs",
        initialValue = listOf<SongBean>()
    )
        private set

    val SongBean.Album.songs: List<SongBean>
        get() = AndroidMediaLibsRepository.songs.filter { it.album.id == this.id }

    val SongBean.Artist.songs: List<SongBean>
        get() = AndroidMediaLibsRepository.songs.filter { it.artist.id == this.id }

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
            val scanSongs = mutableListOf<SongBean>()
            query?.use { cursor ->
                val albumIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
                val albumIdIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
                val artistIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                val artistIdIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID)
                val durationIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                val dataIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                val songNameIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                val sizeIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
                val idIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)

                while (cursor.moveToNext()) {
                    val album = cursor.getString(albumIndex)
                    val albumId = cursor.getLong(albumIdIndex)
                    val artist = cursor.getString(artistIndex)
                    val artistId = cursor.getLong(artistIdIndex)
                    val duration = cursor.getLong(durationIndex)
                    val data = cursor.getString(dataIndex)
                    val songName = cursor.getString(songNameIndex)
                    val size = cursor.getLong(sizeIndex)
                    val id = cursor.getLong(idIndex)
                    val metadata = Metadata.getMetadata(data)

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
                                    SongBean(
                                        album = SongBean.Album(albumId, album),
                                        artist = SongBean.Artist(artistId, track.performer),
                                        duration = endPosition - startPosition,
                                        data = data,
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
                                SongBean(
                                    album = SongBean.Album(albumId, album),
                                    artist = SongBean.Artist(artistId, artist),
                                    duration = duration,
                                    data = data,
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
                            SongBean(
                                album = SongBean.Album(albumId, album),
                                artist = SongBean.Artist(artistId, artist),
                                duration = duration,
                                data = data,
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
            folders = scanSongs.map { it.data.substringBeforeLast("/") }.distinct().map { path ->
                Folder(
                    path,
                    scanSongs.filter { it.data.startsWith(path) },
                    folders.find { it.path == path }?.enabled ?: true
                )
            }
            updateLibs()
        }
    }

    private fun updateLibs() {
        songs = emptyList()
        folders.forEach { folder ->
            if (folder.enabled) {
                songs += folder.songs
            }
        }
        songs = if (SettingRepository.EnableExcludeSongsUnderOneMinute) {
            songs.filter { it.duration > 60 }
        } else {
            songs
        }
        albums = songs.map { it.album }
            .distinctBy { it.id }
            .map { it.copy() }
        artists = songs.map { it.artist }
            .distinctBy { it.id }
            .map { it.copy() }
    }

    fun hideFolder(folder: Folder) {
        folders.find { it.path == folder.path }?.enabled = false
        updateLibs()
    }

    fun unHideFolder(folder: Folder) {
        folders.find { it.path == folder.path }?.enabled = true
        updateLibs()
    }

    /**
     * 从媒体库中隐藏某个歌曲
     */
    fun hide(song: SongBean) {
        songs = songs - song
        hideSongs = hideSongs + song
        updateLibs()
    }

    /**
     * 取消隐藏某个歌曲
     */
    fun unHide(song: SongBean) {
        hideSongs = hideSongs - song
        songs = songs + song
        updateLibs()
    }

    /**
     * 从媒体库中删除某个歌曲
     */
    fun delete(song: SongBean) {
        songs = songs - song
        hideSongs = hideSongs - song
        updateLibs()
    }

    /**
     * 清空媒体库
     */
    fun clear() {
        songs = emptyList()
        artists = emptyList()
        albums = emptyList()
        hideSongs = emptyList()
    }

}