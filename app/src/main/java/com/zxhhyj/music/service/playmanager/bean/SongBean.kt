package com.zxhhyj.music.service.playmanager.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SongBean(
    val album: Album,
    val artist: Artist,
    val duration: Long,
    val data: String,
    val songName: String,
    val size: Long,
    val id: Long,
    val bitrate: Long,
    val samplingRate: Long,
) : Parcelable {

    @Parcelize
    data class Artist(val id: Long, val name: String) : Parcelable

    @Parcelize
    data class Album(val id: Long, val name: String) : Parcelable

}