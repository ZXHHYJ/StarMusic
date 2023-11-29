package com.zxhhyj.music.logic.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlayListSongBean(val songName: String, val artistName: String, val albumName: String) :
    Parcelable