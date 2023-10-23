package com.zxhhyj.music.logic.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SongBean(
    // 歌曲所属专辑信息
    val album: Album,
    // 歌曲所属艺术家信息
    val artist: Artist,
    // 歌曲时长
    val duration: Long,
    // 歌曲文件路径
    val data: String,
    // 歌曲最后修改时间
    val dateModified: Long,
    // 歌曲名称
    val songName: String,
    // 歌曲文件大小
    val size: Long,
    // 歌曲ID
    val id: Long,
    // 歌曲比特率
    val bitrate: Int?,
    // 歌曲采样率
    val samplingRate: Int?,
    // 歌曲歌词
    val lyric: String?,
    // 开始位置
    val startPosition: Long,
    // 结束位置
    val endPosition: Long
) : Parcelable {

    // 艺术家信息
    @Parcelize
    data class Artist(val id: Long, val name: String) : Parcelable

    // 专辑信息
    @Parcelize
    data class Album(val id: Long, val name: String) : Parcelable

}