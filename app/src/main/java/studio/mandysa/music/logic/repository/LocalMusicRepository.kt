package studio.mandysa.music.logic.repository

import android.provider.MediaStore
import studio.mandysa.music.logic.config.mainApplication
import studio.mandysa.music.service.playmanager.bean.SongBean

/**
 * @author 黄浩
 */
object LocalMusicRepository {

    fun getLocalArtists(): List<SongBean.Local.Artist> {
        val artistKVHashMap = LinkedHashMap<Long, SongBean.Local.Artist>()
        val localSongs = getLocalSongs()
        for (song in localSongs) {
            if (artistKVHashMap.containsKey(song.artistId)) {
                (artistKVHashMap[song.artistId]!!.songs as ArrayList).add(song)
                continue
            }
            artistKVHashMap[song.artistId] = SongBean.Local.Artist(
                song.artistId, song.artist,
                arrayListOf(song)
            )
        }
        val list = arrayListOf<SongBean.Local.Artist>()
        for (entry in artistKVHashMap) {
            list.add(entry.value)
        }
        return list
    }

    fun getLocalAlbums(): List<SongBean.Local.Album> {
        val albumKVHashMap = LinkedHashMap<Long, SongBean.Local.Album>()
        val localSongs = getLocalSongs()
        for (song in localSongs) {
            if (albumKVHashMap.containsKey(song.albumId)) {
                (albumKVHashMap[song.albumId]!!.songs as ArrayList).add(song)
                continue
            }
            albumKVHashMap[song.albumId] = SongBean.Local.Album(
                song.albumId, song.album, song.artistId, song.artist,
                arrayListOf(song)
            )
        }
        val list = arrayListOf<SongBean.Local.Album>()
        for (entry in albumKVHashMap) {
            list.add(entry.value)
        }
        return list
    }

    fun getLocalSongs(): List<SongBean.Local> {
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
            list.add(SongBean.Local(album, albumId, artist, artistId, duration, data, songName))
        }
        query?.close()
        return list
    }
}