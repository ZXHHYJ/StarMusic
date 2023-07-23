package com.zxhhyj.music.logic.repository

import com.funny.data_saver.core.mutableDataSaverStateOf
import com.zxhhyj.music.logic.config.DataSaverUtils

object SettingRepository {

    /**
     * 是否启用Monet
     */
    var EnableMonet by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "EnableMonet",
        initialValue = true
    )

    /**
     * 是否启用Android媒体库
     */
    var EnableAndroidMediaLibs by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "EnableAndroidMediaLibs",
        initialValue = false
    )

    /**
     * 是否首次启动APP(同意隐私政策)
     */
    var AgreePrivacyPolicy by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "AgreePrivacyPolicy",
        initialValue = false
    )

    /**
     * 是否启用显示歌词翻译
     */
    var EnableLyricsTranslation by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "EnableLyricsTranslation",
        initialValue = false
    )

}