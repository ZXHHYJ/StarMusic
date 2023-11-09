package com.zxhhyj.music.logic.utils

import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine
import com.zxhhyj.music.logic.repository.SettingRepository

object SardineUtils {
    fun createSardine(): OkHttpSardine {
        return OkHttpSardine().apply {
            setCredentials(
                SettingRepository.WebDavConfig.username,
                SettingRepository.WebDavConfig.password
            )
        }
    }
}