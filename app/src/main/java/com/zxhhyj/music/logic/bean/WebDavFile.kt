package com.zxhhyj.music.logic.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WebDavFile(
    val path: String,
    val name: String,
    val downloadPath: String,
    val eTag: String
) : Parcelable