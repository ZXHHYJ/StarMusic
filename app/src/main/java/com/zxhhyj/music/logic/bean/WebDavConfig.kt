package com.zxhhyj.music.logic.bean

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class WebDavConfig(val username: String, val password: String, val address: String) :
    Parcelable