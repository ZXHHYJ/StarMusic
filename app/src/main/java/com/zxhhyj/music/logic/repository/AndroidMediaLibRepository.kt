package com.zxhhyj.music.logic.repository

import android.provider.MediaStore
import androidx.core.database.getStringOrNull
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
                val albumIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
                val albumIdIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
                val artistIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                val durationIndex =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                val dataIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                val songNameIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                val sizeIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
                val idIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val dateModifiedIndex =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_MODIFIED)

                while (cursor.moveToNext()) {
                    val album = cursor.getStringOrNull(albumIndex) ?: "<unknown>"
                    val albumId = cursor.getLong(albumIdIndex)
                    val artist = cursor.getStringOrNull(artistIndex) ?: "<unknown>"
                    val duration = cursor.getLong(durationIndex)
                    val data = cursor.getString(dataIndex)
                    val songName = cursor.getString(songNameIndex)
                    val size = cursor.getLong(sizeIndex)
                    val id = cursor.getLong(idIndex)
                    val dateModified = cursor.getLong(dateModifiedIndex)
                    val metadata = Metadata.getMetadata(data) ?: continue
                    val bitrate = metadata.bitrate
                    val sampleRate = metadata.sampleRate
                    val lyric = metadata.properties["LYRICS"]?.getOrNull(0)?.trim()
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
                                        bitrate = bitrate,
                                        samplingRate = sampleRate,
                                        lyric = lyric,
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
                                    bitrate = bitrate,
                                    samplingRate = sampleRate,
                                    lyric = lyric,
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
                                bitrate = bitrate,
                                samplingRate = sampleRate,
                                lyric = lyric,
                                startPosition = 0,
                                endPosition = duration,
                            )
                        )
                    }
                }
            }
            scanSongs.removeAll(hideSongs)
            songs = scanSongs
            updateLibs()
        }
    }

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

    private fun refreshSongs() {
        songs = folders
            .flatMap { it.songs }
            .filter {
                if (SettingRepository.EnableExcludeSongsUnderOneMinute) it.duration > 60000 else true
            }
            .distinctBy { it.data }
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
     * 从媒体库中移除某个歌曲
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