package com.zxhhyj.music.logic.repository

import android.annotation.SuppressLint
import android.provider.MediaStore
import com.funny.data_saver.core.mutableDataSaverListStateOf
import com.zxhhyj.music.logic.config.DataSaverUtils
import com.zxhhyj.music.logic.config.application
import com.zxhhyj.music.service.playmanager.bean.SongBean
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
        get() {
            val list = arrayListOf<SongBean>()
            for (song in AndroidMediaLibsRepository.songs) {
                if (song.album.id == this.id) {
                    list.add(song)
                }
            }
            return list
        }

    val SongBean.Artist.songs: List<SongBean>
        get() {
            val list = arrayListOf<SongBean>()
            for (song in AndroidMediaLibsRepository.songs) {
                if (song.artist.id == this.id) {
                    list.add(song)
                }
            }
            return list
        }

    /**
     * 扫描媒体
     */
    @SuppressLint("Range")
    suspend fun scanMedia() {
        songs = suspendCancellableCoroutine {
            val query = application.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                "${MediaStore.Audio.Media.IS_MUSIC} != 0",
                null,
                null
            )
            val songs = arrayListOf<SongBean>()
            while (query != null && query.moveToNext()) {
                val album =
                    query.getString(query.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
                val albumId =
                    query.getLong(query.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))
                val artist =
                    query.getString(query.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                val artistId =
                    query.getLong(query.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID))
                val duration =
                    query.getLong(query.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                val data =
                    query.getString(query.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                val songName =
                    query.getString(query.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                val size =
                    query.getLong(query.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE))
                val id =
                    query.getLong(query.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                val bitrate =
                    query.getLong(query.getColumnIndexOrThrow(MediaStore.Audio.Media.BITRATE))
                val samplingRate =
                    query.getLong(query.getColumnIndexOrThrow(MediaStore.Audio.Media.CAPTURE_FRAMERATE))
                songs.add(
                    SongBean(
                        album = SongBean.Album(albumId, album),
                        artist = SongBean.Artist(artistId, artist),
                        duration = duration,
                        data = data,
                        songName = songName,
                        size = size,
                        id = id,
                        bitrate = bitrate,
                        samplingRate = samplingRate,
                    )
                )
            }
            query?.close()
            it.resume(songs)
        }
        updateAlbum()
        updateArtist()

    }

    private fun updateAlbum() {
        val albumKVHashMap = LinkedHashMap<Long, SongBean.Album>()
        for (song in songs) {
            if (albumKVHashMap.containsKey(song.album.id)) {
                continue
            }
            albumKVHashMap[song.album.id] = song.album.copy()
        }
        albums = albumKVHashMap.values.toList()
    }

    private fun updateArtist() {
        val artistKVHashMap = LinkedHashMap<Long, SongBean.Artist>()
        for (song in songs) {
            if (artistKVHashMap.containsKey(song.artist.id)) {
                continue
            }
            artistKVHashMap[song.artist.id] = song.artist.copy()
        }
        artists = artistKVHashMap.values.toList()
    }

    /**
     * 从媒体库中隐藏某个歌曲
     */
    fun hide(song: SongBean) {
        val index = songs.indexOf(song)
        if (index != -1) {
            songs = songs.toMutableList().also {
                it.removeAt(index)
            }
            updateAlbum()
            updateArtist()
            hideSongs = hideSongs.toMutableList().also {
                it.add(song)
            }
        }
    }

    /**
     * 取消隐藏某个歌曲
     */
    fun unHide(song: SongBean) {
        val index = hideSongs.indexOf(song)
        if (index != -1) {
            hideSongs = hideSongs.toMutableList().also {
                it.removeAt(index)
            }
            songs = songs.toMutableList().also {
                it.add(song)
            }
            updateAlbum()
            updateArtist()
        }
    }

    /**
     * 从媒体库中删除某个歌曲
     */
    fun delete(song: SongBean) {
        val index = songs.indexOf(song)
        if (index != -1) {
            songs = songs.toMutableList().also {
                it.removeAt(index)
            }
            updateAlbum()
            updateArtist()
        }
    }

    /**
     * 清空媒体库
     */
    fun clear() {
        songs = listOf()
        artists = listOf()
        albums = listOf()
        hideSongs = listOf()
    }

}

