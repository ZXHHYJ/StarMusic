package studio.mandysa.music.logic.repository

import android.annotation.SuppressLint
import android.provider.MediaStore
import studio.mandysa.music.logic.config.mainApplication
import studio.mandysa.music.logic.bean.MusicBean

object LocalMusicRepository {
    @SuppressLint("Range")
    fun getAudioFiles(): List<MusicBean> {
        val projections = arrayOf(
            MediaStore.Audio.AudioColumns.ALBUM,
            MediaStore.Audio.AudioColumns.ALBUM_ID,
            MediaStore.Audio.AudioColumns.ARTIST,
            MediaStore.Audio.AudioColumns.ARTIST_ID,
            MediaStore.Audio.AudioColumns.DURATION,
            MediaStore.Audio.AudioColumns.IS_MUSIC,
            MediaStore.Audio.AudioColumns.DATA,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.TITLE
        )
        val query = mainApplication.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projections,
            MediaStore.Audio.AudioColumns.IS_MUSIC + "!=0",
            null,
            MediaStore.Audio.Media.DEFAULT_SORT_ORDER
        )
        val array = ArrayList<MusicBean>()
        while (query != null && query.moveToNext()) {
            val model = MusicBean()

            val album = query.getString(query.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM))
            val album_id =
                query.getLong(query.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_ID))


            val artist = query.getString(query.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST))
            val artist_id =
                query.getLong(query.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST_ID))
            val duration =
                query.getLong(query.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION))
            val data = query.getString(query.getColumnIndex(MediaStore.Audio.AudioColumns.DATA))
            val title = query.getString(query.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
            val songName = query.getString(query.getColumnIndex(MediaStore.Audio.Media.TITLE))
            array.add(model)
        }
        query?.close()
        return array
    }
}