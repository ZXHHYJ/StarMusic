package com.zxhhyj.music.logic.utils

import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine
import com.zxhhyj.music.logic.bean.WebDavSource

object SardineUtils {

    fun WebDavSource.toSardine() = OkHttpSardine().apply {
        setCredentials(
            username,
            password
        )
    }

}