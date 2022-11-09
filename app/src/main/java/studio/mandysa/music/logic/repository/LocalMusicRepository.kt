package studio.mandysa.music.logic.repository

import android.provider.MediaStore
import studio.mandysa.music.logic.config.mainApplication
import studio.mandysa.music.service.playmanager.bean.Song


object LocalMusicRepository {

    fun getAudioFiles(): List<Song.LocalBean> {
        val query = mainApplication.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            arrayOf(
                MediaStore.Audio.AudioColumns.ALBUM,
                MediaStore.Audio.AudioColumns.ALBUM_ID,
                MediaStore.Audio.AudioColumns.ARTIST,
                MediaStore.Audio.AudioColumns.ARTIST_ID,
                MediaStore.Audio.AudioColumns.DURATION,
                MediaStore.Audio.AudioColumns.IS_MUSIC,
                MediaStore.Audio.AudioColumns.DATA,
                MediaStore.Audio.Media.TITLE
            ),
            MediaStore.Audio.AudioColumns.IS_MUSIC + "!=0",
            null,
            MediaStore.Audio.Media.DEFAULT_SORT_ORDER
        )
        val list = ArrayList<Song.LocalBean>()
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
                query.getString(query.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
            list.add(Song.LocalBean(album, albumId, artist, artistId, duration, data, songName))
        }
        query?.close()
        return list
    }
}