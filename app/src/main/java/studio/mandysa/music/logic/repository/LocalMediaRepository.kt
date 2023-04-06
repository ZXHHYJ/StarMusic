package studio.mandysa.music.logic.repository

import android.provider.MediaStore
import com.funny.data_saver.core.mutableDataSaverListStateOf
import kotlinx.coroutines.suspendCancellableCoroutine
import studio.mandysa.music.logic.config.application
import studio.mandysa.music.logic.config.DataSaverUtils
import studio.mandysa.music.service.playmanager.bean.SongBean
import kotlin.coroutines.resume

/**
 * @author 黄浩
 */
object LocalMediaRepository {

    var artists by mutableDataSaverListStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "local_artists",
        initialValue = listOf<SongBean.Local.Artist>()
    )
        private set

    var albums by mutableDataSaverListStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "local_albums",
        initialValue = listOf<SongBean.Local.Album>()
    )
        private set

    var songs by mutableDataSaverListStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "local_songs",
        initialValue = listOf<SongBean.Local>()
    )
        private set

    val SongBean.Local.Album.artist: SongBean.Local.Artist
        get() {
            for (song in this@LocalMediaRepository.songs) {
                if (song.album.id == this.id)
                    return song.artist
            }
            throw java.lang.NullPointerException("artist is null")
        }

    val SongBean.Local.Album.songs: List<SongBean.Local>
        get() {
            val list = arrayListOf<SongBean.Local>()
            for (song in this@LocalMediaRepository.songs) {
                if (song.album.id == this.id) {
                    list.add(song)
                }
            }
            return list
        }

    val SongBean.Local.Artist.songs: List<SongBean.Local>
        get() {
            val list = arrayListOf<SongBean.Local>()
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
            val songs = arrayListOf<SongBean.Local>()
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
                    SongBean.Local(
                        album = SongBean.Local.Album(albumId.toString(), album),
                        artist = SongBean.Local.Artist(artistId.toString(), artist),
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
        albums = suspendCancellableCoroutine {
            val albumKVHashMap = LinkedHashMap<String, SongBean.Local.Album>()
            for (song in songs) {
                if (albumKVHashMap.containsKey(song.album.id)) {
                    continue
                }
                albumKVHashMap[song.album.id] = song.album.copy()
            }
            val albums = arrayListOf<SongBean.Local.Album>()
            for (entry in albumKVHashMap) {
                albums.add(entry.value)
            }
            it.resume(albums)
        }
        artists = suspendCancellableCoroutine {
            val artistKVHashMap = LinkedHashMap<String, SongBean.Local.Artist>()
            for (song in songs) {
                if (artistKVHashMap.containsKey(song.artist.id)) {
                    continue
                }
                artistKVHashMap[song.artist.id] = song.artist.copy()
            }
            val artists = arrayListOf<SongBean.Local.Artist>()
            for (entry in artistKVHashMap) {
                artists.add(entry.value)
            }
            it.resume(artists)
        }
    }



}