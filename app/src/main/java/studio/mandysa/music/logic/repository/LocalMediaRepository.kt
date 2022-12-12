package studio.mandysa.music.logic.repository

import android.provider.MediaStore
import studio.mandysa.music.logic.config.mainApplication
import studio.mandysa.music.service.playmanager.bean.SongBean

/**
 * @author 黄浩
 */
object LocalMediaRepository {

    fun getArtists(): List<SongBean.Local.Artist> {
        val artistKVHashMap = LinkedHashMap<String, SongBean.Local.Artist>()
        for (song in getSongs()) {
            if (artistKVHashMap.containsKey(song.artist.id)) {
                continue
            }
            artistKVHashMap[song.artist.id] = song.artist.copy()
        }
        val list = arrayListOf<SongBean.Local.Artist>()
        for (entry in artistKVHashMap) {
            list.add(entry.value)
        }
        return list
    }

    fun getAlbums(): List<SongBean.Local.Album> {
        val hashMap = LinkedHashMap<String, SongBean.Local.Album>()
        val localSongs = getSongs()
        for (song in localSongs) {
            if (hashMap.containsKey(song.album.id)) {
                continue
            }
            hashMap[song.album.id] = song.album.copy()
        }
        val list = arrayListOf<SongBean.Local.Album>()
        for (entry in hashMap) {
            list.add(entry.value)
        }
        return list
    }

    fun getSongs(): List<SongBean.Local> {
        val query = mainApplication.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            arrayOf(
                MediaStore.Audio.AudioColumns.ALBUM,
                MediaStore.Audio.AudioColumns.ALBUM_ID,
                MediaStore.Audio.AudioColumns.ARTIST,
                MediaStore.Audio.AudioColumns.ARTIST_ID,
                MediaStore.Audio.AudioColumns.DURATION,
                MediaStore.Audio.AudioColumns.DATA,
                MediaStore.Audio.AudioColumns.TITLE
            ),
            MediaStore.Audio.AudioColumns.DURATION + ">300",
            null,
            null
        )
        val list = arrayListOf<SongBean.Local>()
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
            list.add(
                SongBean.Local(
                    SongBean.Local.Album(albumId.toString(), album),
                    SongBean.Local.Artist(artistId.toString(), artist),
                    duration,
                    data,
                    songName
                )
            )
        }
        query?.close()
        return list
    }

    val SongBean.Local.Album.artist: SongBean.Local.Artist
        get() {
            for (song in getSongs()) {
                if (song.album.id == this.id)
                    return song.artist.copy()
            }
            throw java.lang.NullPointerException("artist is null")
        }

    val SongBean.Local.Album.songs: List<SongBean.Local>
        get() {
            val list = arrayListOf<SongBean.Local>()
            for (song in getSongs()) {
                if (song.album.id == this.id) {
                    list.add(song.copy())
                }
            }
            return list
        }

    val SongBean.Local.Artist.songs: List<SongBean.Local>
        get() {
            val list = arrayListOf<SongBean.Local>()
            for (song in getSongs()) {
                if (song.artist.id == this.id) {
                    list.add(song.copy())
                }
            }
            return list
        }
}