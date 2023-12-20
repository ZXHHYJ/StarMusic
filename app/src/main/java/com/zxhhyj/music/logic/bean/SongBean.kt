package com.zxhhyj.music.logic.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed interface SongBean : Parcelable {
    // 歌曲封面路径
    val coverUrl: String?

    // 歌曲所属专辑信息
    val albumName: String?

    // 歌曲所属艺术家信息
    val artistName: String?

    // 歌曲时长
    val duration: Long?

    // 歌曲文件路径
    val data: String

    // 歌曲最后修改时间
    val dateModified: Long

    // 歌曲名称
    val songName: String

    // 歌曲文件大小
    val size: Long

    // 歌曲比特率
    val bitrate: Int?

    // 歌曲采样率
    val samplingRate: Int?

    // 歌曲歌词
    val lyric: String?

    @Parcelize
    data class Local(
        override val coverUrl: String?,
        override val albumName: String?,
        override val artistName: String?,
        override val duration: Long?,
        override val data: String,
        override val dateModified: Long,
        override val songName: String,
        override val size: Long,
        val id: Long?,
        override val bitrate: Int?,
        override val samplingRate: Int?,
        override val lyric: String?
    ) : SongBean

    @Parcelize
    data class WebDav(
        val webDavSource: WebDavSource,
        override val coverUrl: String?,
        override val albumName: String?,
        override val artistName: String?,
        override val duration: Long?,
        override val data: String,
        override val dateModified: Long,
        override val songName: String,
        override val size: Long,
        override val bitrate: Int?,
        override val samplingRate: Int?,
        override val lyric: String?
    ) : SongBean

}