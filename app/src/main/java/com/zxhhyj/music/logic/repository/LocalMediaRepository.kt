package com.zxhhyj.music.logic.repository

import android.provider.MediaStore
import com.funny.data_saver.core.mutableDataSaverListStateOf
import com.zxhhyj.music.logic.config.DataSaverUtils
import com.zxhhyj.music.logic.config.application
import com.zxhhyj.music.service.playmanager.bean.SongBean
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


object LocalMediaRepository {

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

    val SongBean.Album.artist: SongBean.Artist
        get() {
            for (song in this@LocalMediaRepository.songs) {
                if (song.album.id == this.id)
                    return song.artist
            }
            throw java.lang.NullPointerException("artist is null")
        }

    val SongBean.Album.songs: List<SongBean>
        get() {
            val list = arrayListOf<SongBean>()
            for (song in this@LocalMediaRepository.songs) {
                if (song.album.id == this.id) {
                    list.add(song)
                }
            }
            return list
        }

    val SongBean.Artist.songs: List<SongBean>
        get() {
            val list = arrayListOf<SongBean>()
            for (song in this@LocalMediaRepository.songs) {
                if (song.artist.id == this.id) {
                    list.add(song)
                }
            }
            return list
        }

    /**
     * 扫描媒体
     */
    suspend fun scanMedia() {
        songs = suspendCancellableCoroutine {
            val query = application.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                arrayOf(
                    MediaStore.Audio.AudioColumns.ALBUM,
                    MediaStore.Audio.AudioColumns.ALBUM_ID,
                    MediaStore.Audio.AudioColumns.ARTIST,
                    MediaStore.Audio.AudioColumns.ARTIST_ID,
                    MediaStore.Audio.AudioColumns.DURATION,
                    MediaStore.Audio.AudioColumns.DATA,
                    MediaStore.Audio.AudioColumns.TITLE,
                    MediaStore.Audio.AudioColumns.SIZE,
                ),
                null,
                null,
                null
            )
            val songs = arrayListOf<SongBean>()
            while (query != null && query.moveToNext()) {
                val album =
                    query.getString(query.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM))
                val albumId =
                    query.getLong(query.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_ID))
                val artist =
                    query.getString(query.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST))
                val artistId =
                    query.getLong(query.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST_ID))
                val duration =
                    query.getLong(query.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION))
                val data =
                    query.getString(query.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATA))
                val songName =
                    query.getString(query.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE))
                val size =
                    query.getLong(query.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.SIZE))
                songs.add(
                    SongBean(
                        album = SongBean.Album(albumId.toString(), album),
                        artist = SongBean.Artist(artistId.toString(), artist),
                        duration = duration,
                        data = data,
                        songName = songName,
                        size = size
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
        val albumKVHashMap = LinkedHashMap<String, SongBean.Album>()
        for (song in songs) {
            if (albumKVHashMap.containsKey(song.album.id)) {
                continue
            }
            albumKVHashMap[song.album.id] = song.album.copy()
        }
        val albums = arrayListOf<SongBean.Album>()
        for (entry in albumKVHashMap) {
            albums.add(entry.value)
        }
        this.albums = albums
    }

    private fun updateArtist() {
        val artistKVHashMap = LinkedHashMap<String, SongBean.Artist>()
        for (song in songs) {
            if (artistKVHashMap.containsKey(song.artist.id)) {
                continue
            }
            artistKVHashMap[song.artist.id] = song.artist.copy()
        }
        val artists = arrayListOf<SongBean.Artist>()
        for (entry in artistKVHashMap) {
            artists.add(entry.value)
        }
        this.artists = artists
    }

    fun delete(song: SongBean) {
        when (val index = songs.indexOf(song)) {
            -1 -> {}
            else -> {
                val newSongs = songs.toMutableList()
                newSongs.removeAt(index)
                songs = newSongs
                updateAlbum()
                updateArtist()
            }
        }
    }

}