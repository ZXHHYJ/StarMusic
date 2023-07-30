package com.zxhhyj.music.logic.repository

import android.provider.MediaStore
import com.funny.data_saver.core.mutableDataSaverListStateOf
import com.kyant.tag.Metadata
import com.zxhhyj.music.MainApplication
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.config.DataSaverUtils
import com.zxhhyj.music.logic.utils.CueParser
import com.zxhhyj.music.logic.utils.FileUtils
import com.zxhhyj.music.logic.utils.toMillis
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


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
        songs = suspendCancellableCoroutine { it ->
            val query = MainApplication.context.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                null
            )
            val songs = mutableListOf<SongBean>()
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
                                songs.add(
                                    SongBean(
                                        album = SongBean.Album(albumId, album),
                                        artist = SongBean.Artist(artistId, track.performer),
                                        duration = endPosition - track.startPosition.toMillis(),
                                        data = data,
                                        songName = track.title,
                                        size = size,
                                        id = id,
                                        bitrate = metadata?.bitrate,
                                        samplingRate = metadata?.sampleRate,
                                        lyric = metadata?.properties?.get("LYRICS")?.get(0),
                                    )
                                )
                            }
                        }.getOrElse {
                            songs.add(
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
                                )
                            )
                        }
                    } else {
                        songs.add(
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
                            )
                        )
                    }
                }
            }
            query?.close()
            songs.removeAll(hideSongs)
            it.resume(songs)
        }
        dataUpdate()
    }

    private fun dataUpdate() {
        albums = songs.map { it.album }
            .distinctBy { it.id }
            .map { it.copy() }
        artists = songs.map { it.artist }
            .distinctBy { it.id }
            .map { it.copy() }
    }

    /**
     * 从媒体库中隐藏某个歌曲
     */
    fun hide(song: SongBean) {
        songs = songs - song
        hideSongs = hideSongs + song
        dataUpdate()
    }

    /**
     * 取消隐藏某个歌曲
     */
    fun unHide(song: SongBean) {
        hideSongs = hideSongs - song
        songs = songs + song
        dataUpdate()
    }

    /**
     * 从媒体库中删除某个歌曲
     */
    fun delete(song: SongBean) {
        songs = songs - song
        hideSongs = hideSongs - song
        dataUpdate()
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