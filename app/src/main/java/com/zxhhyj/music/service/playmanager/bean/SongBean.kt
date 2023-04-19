package com.zxhhyj.music.service.playmanager.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class SongBean(
    val album: Album,
    val artist: Artist,
    val duration: Long,
    val data: String,
    val songName: String,
    val size: Long
) {

    @Parcelize
    data class Artist(val id: String, val name: String) : Parcelable

    @Parcelize
    data class Album(
        val id: String,
        val name: String,
        val coverUrl: String = "content://media/external/audio/albumart/${id}"
    ) : Parcelable

}