package com.zxhhyj.music.logic.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WebDavConfig(val username: String, val password: String, val address: String) :
    Parcelable