package com.zxhhyj.music.logic.repository

import com.funny.data_saver.core.mutableDataSaverStateOf
import com.zxhhyj.music.logic.config.DataSaverUtils

object SettingRepository {

    /**
     * 是否启用Android媒体库
     */
    var EnableAndroidMediaLibs by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "EnableAndroidMediaLibs",
        initialValue = false
    )

    /**
     * 是否启用对cue文件的支持，CUE 文件是一种用于描述音频 CD 的文本文件格式。它通常与一个或多个音频轨道文件
     */
    var EnableCueSupport by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "EnableCueSupport",
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

    var EnableNewPlayerUI by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "EnableNewPlayerUI",
        initialValue = false
    )

}