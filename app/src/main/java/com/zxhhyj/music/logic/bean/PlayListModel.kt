package com.zxhhyj.music.logic.bean

import android.os.Parcelable
import com.funny.data_saver.core.mutableDataSaverListStateOf
import com.funny.data_saver.core.mutableDataSaverStateOf
import com.zxhhyj.music.logic.config.DataSaverUtils
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class PlayListModel(
    val uuid: String = UUID.randomUUID().toString(),
) : Parcelable {

    @IgnoredOnParcel
    val nameKey = "name:$uuid"

    @IgnoredOnParcel
    val songsKey = "songs:$uuid"

    @IgnoredOnParcel
    private val _name = mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = nameKey,
        initialValue = ""
    )

    @IgnoredOnParcel
    val name by _name

    @IgnoredOnParcel
    private val _songs = mutableDataSaverListStateOf(
        dataSaverInterface = DataSaverUtils,
        key = songsKey,
        initialValue = listOf<SongBean>()
    )

    @IgnoredOnParcel
    val songs by _songs

    /**
     * 添加一首歌曲到播放列表
     */
    fun addSong(songBean: SongBean) {
        _songs.value = _songs.value.plus(songBean)
    }

    /**
     * 从播放列表中移除一首歌曲
     */
    fun removeSong(songBean: SongBean) {
        _songs.value = _songs.value.minus(songBean)
    }

    /**
     * 重命名播放列表的名称
     */
    fun rename(newName: String) {
        _name.value = newName
    }

}