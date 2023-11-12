package com.zxhhyj.music.logic.bean

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
open class SongBean(
    // 歌曲封面路径
    open val coverUrl: String,
    // 歌曲所属专辑信息
    open val album: Album,
    // 歌曲所属艺术家信息
    open val artist: Artist,
    // 歌曲时长
    open val duration: Long,
    // 歌曲文件路径
    open val data: String,
    // 歌曲最后修改时间
    open val dateModified: Long,
    // 歌曲名称
    open val songName: String,
    // 歌曲文件大小
    open val size: Long,
    // 歌曲比特率
    open val bitrate: Int?,
    // 歌曲采样率
    open val samplingRate: Int?,
    // 歌曲歌词
    open val lyric: String?,
    // 开始位置
    open val startPosition: Long,
    // 结束位置
    open val endPosition: Long
) : Parcelable {

    @Parcelize
    @JsonClass(generateAdapter = true)
    data class Local(
        override val coverUrl: String,
        override val album: Album,
        override val artist: Artist,
        override val duration: Long,
        override val data: String,
        override val dateModified: Long,
        override val songName: String,
        override val size: Long,
        val id: Long,
        override val bitrate: Int?,
        override val samplingRate: Int?,
        override val lyric: String?,
        override val startPosition: Long,
        override val endPosition: Long
    ) : SongBean(
        coverUrl,
        album,
        artist,
        duration,
        data,
        dateModified,
        songName,
        size,
        bitrate,
        samplingRate,
        lyric,
        startPosition,
        endPosition
    )

    @Parcelize
    @JsonClass(generateAdapter = true)
    data class WebDav(
        val webDavFile: WebDavFile,
        override val coverUrl: String,
        override val album: Album,
        override val artist: Artist,
        override val duration: Long,
        override val data: String,
        override val dateModified: Long,
        override val songName: String,
        override val size: Long,
        override val bitrate: Int?,
        override val samplingRate: Int?,
        override val lyric: String?,
        override val startPosition: Long,
        override val endPosition: Long
    ) : SongBean(
        coverUrl,
        album,
        artist,
        duration,
        data,
        dateModified,
        songName,
        size,
        bitrate,
        samplingRate,
        lyric,
        startPosition,
        endPosition
    )

    // 艺术家信息
    @Parcelize
    @JsonClass(generateAdapter = true)
    data class Artist(val name: String) : Parcelable

    // 专辑信息
    @Parcelize
    @JsonClass(generateAdapter = true)
    data class Album(val name: String) : Parcelable

}