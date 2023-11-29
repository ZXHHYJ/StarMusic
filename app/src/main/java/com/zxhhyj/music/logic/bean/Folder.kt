package com.zxhhyj.music.logic.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Folder(val path: String, val songs: List<SongBean.Local>) : Parcelable
