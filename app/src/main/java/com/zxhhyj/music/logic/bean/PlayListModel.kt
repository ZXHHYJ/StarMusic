package com.zxhhyj.music.logic.bean

import android.os.Parcelable
import com.funny.data_saver.core.mutableDataSaverListStateOf
import com.funny.data_saver.core.mutableDataSaverStateOf
import com.squareup.moshi.JsonClass
import com.zxhhyj.music.logic.config.DataSaverUtils
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
@JsonClass(generateAdapter = true)
data class PlayListModel(
    val uuid: String = UUID.randomUUID().toString(),
) : Parcelable {

    @IgnoredOnParcel
    val nameKey = "name:$uuid"

    @IgnoredOnParcel
    val songsKey = "songs:$uuid"

    @IgnoredOnParcel
    @delegate:Transient
    var name by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = nameKey,
        initialValue = ""
    )
        private set

    @IgnoredOnParcel
    @delegate:Transient
    var songs by mutableDataSaverListStateOf(
        dataSaverInterface = DataSaverUtils,
        key = songsKey,
        initialValue = listOf<SongBean>()
    )
        private set

    /**
     * 添加一首歌曲到播放列表
     */
    fun addSong(songBean: SongBean) {
        songs = songs.plus(songBean)
    }

    /**
     * 从播放列表中移除一首歌曲
     */
    fun removeSong(songBean: SongBean) {
        songs = songs.minus(songBean)
    }

    /**
     * 重命名播放列表的名称
     */
    fun rename(newName: String) {
        name = newName
    }

}