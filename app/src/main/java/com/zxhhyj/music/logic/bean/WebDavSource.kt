package com.zxhhyj.music.logic.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WebDavSource(
    val username: String,
    val password: String,
    val address: String,
    val remark: String,
    val folders: List<String>
) : Parcelable