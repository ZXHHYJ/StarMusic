package studio.mandysa.music.logic.repository

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import androidx.annotation.RequiresApi
import studio.mandysa.music.logic.bean.LocalMusicBean
import studio.mandysa.music.logic.config.mainApplication
import java.io.IOException


object LocalMusicRepository {

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Throws(IOException::class)
    private fun getAlbumArtwork(resolver: ContentResolver, albumId: Long): Bitmap {
        val contentUri: Uri = ContentUris.withAppendedId(
            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
            albumId
        )
        return resolver.loadThumbnail(contentUri, Size(640, 480), null)
    }

    @SuppressLint("Range")
    fun getAudioFiles(): List<LocalMusicBean> {
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
        val list = ArrayList<LocalMusicBean>()
        while (query != null && query.moveToNext()) {
            val album = query.getString(query.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM))
            val albumId =
                query.getLong(query.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_ID))
            val artist = query.getString(query.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST))
            val artistId =
                query.getLong(query.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST_ID))
            val duration =
                query.getLong(query.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION))
            val data = query.getString(query.getColumnIndex(MediaStore.Audio.AudioColumns.DATA))
            val songName = query.getString(query.getColumnIndex(MediaStore.Audio.Media.TITLE))
            list.add(LocalMusicBean(album, albumId, artist, artistId, duration, data, songName))
        }
        query?.close()
        return list
    }
}