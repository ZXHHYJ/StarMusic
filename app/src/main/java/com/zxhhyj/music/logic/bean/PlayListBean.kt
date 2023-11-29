package com.zxhhyj.music.logic.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlayListBean(
    val uuid: String,
    val name: String,
    val songs: List<PlayListSongBean>
) : Parcelable
