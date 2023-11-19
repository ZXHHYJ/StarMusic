package com.zxhhyj.music.logic.bean

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class PlayListSongBean(val songName: String, val artistName: String, val albumName: String) :
    Parcelable