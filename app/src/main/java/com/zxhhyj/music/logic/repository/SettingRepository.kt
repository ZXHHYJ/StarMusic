package com.zxhhyj.music.logic.repository

import com.funny.data_saver.core.mutableDataSaverStateOf
import com.zxhhyj.music.logic.config.DataSaverUtils

object SettingRepository {

    /**
     * 是否启用歌曲封面取色
     */
    var EnableAlbumGetColor by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "EnableAlbumGetColor",
        initialValue = true
    )

    /**
     * 是否首次启动APP
     */
    var IsFirstLaunch by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "IsFirstLaunch",
        initialValue = true
    )

}