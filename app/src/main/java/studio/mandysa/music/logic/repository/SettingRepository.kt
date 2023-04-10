package studio.mandysa.music.logic.repository

import com.funny.data_saver.core.mutableDataSaverStateOf
import studio.mandysa.music.logic.config.DataSaverUtils

object SettingRepository {

    var enableAlbumGetColor by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "EnableAlbumGetColor",
        initialValue = true
    )

}