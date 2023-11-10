package com.zxhhyj.music.logic.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
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
    data class Local(
        @SerializedName("localCoverUrl")
        override val coverUrl: String,
        @SerializedName("localAlbum")
        override val album: Album,
        @SerializedName("localArtist")
        override val artist: Artist,
        @SerializedName("localDuration")
        override val duration: Long,
        @SerializedName("localData")
        override val data: String,
        @SerializedName("localDateModified")
        override val dateModified: Long,
        @SerializedName("localSongName")
        override val songName: String,
        @SerializedName("localSize")
        override val size: Long,
        val id: Long,
        @SerializedName("localBitrate")
        override val bitrate: Int?,
        @SerializedName("localSamplingRate")
        override val samplingRate: Int?,
        @SerializedName("localLyric")
        override val lyric: String?,
        @SerializedName("localStartPosition")
        override val startPosition: Long,
        @SerializedName("localEndPosition")
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
    data class WebDav(
        val webDavFile: WebDavFile,
        @SerializedName("webdavCoverUrl")
        override val coverUrl: String,
        @SerializedName("webdavAlbum")
        override val album: Album,
        @SerializedName("webdavArtist")
        override val artist: Artist,
        @SerializedName("webdavDuration")
        override val duration: Long,
        @SerializedName("webdavData")
        override val data: String,
        @SerializedName("webdavDateModified")
        override val dateModified: Long,
        @SerializedName("webdavSongName")
        override val songName: String,
        @SerializedName("webdavSize")
        override val size: Long,
        @SerializedName("webdavBitrate")
        override val bitrate: Int?,
        @SerializedName("webdavSamplingRate")
        override val samplingRate: Int?,
        @SerializedName("webdavLyric")
        override val lyric: String?,
        @SerializedName("webdavStartPosition")
        override val startPosition: Long,
        @SerializedName("webdavEndPosition")
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
    data class Artist(val name: String) : Parcelable

    // 专辑信息
    @Parcelize
    data class Album(val name: String) : Parcelable

}