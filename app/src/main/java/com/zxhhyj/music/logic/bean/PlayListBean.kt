package com.zxhhyj.music.logic.bean

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class PlayListBean(
    val uuid: String,
    val name: String,
    val songs: List<PlayListSongBean>
) : Parcelable
