package com.zxhhyj.music.logic.bean

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class WebDavFile(
    val path: String,
    val name: String,
    val downloadPath: String,
    val eTag: String,
    val contentLength: Long
) : Parcelable