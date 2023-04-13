package com.zxhhyj.music.logic.repository

import com.funny.data_saver.core.mutableDataSaverStateOf
import com.zxhhyj.music.logic.config.DataSaverUtils

object SettingRepository {

    var enableAlbumGetColor by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "EnableAlbumGetColor",
        initialValue = true
    )

}