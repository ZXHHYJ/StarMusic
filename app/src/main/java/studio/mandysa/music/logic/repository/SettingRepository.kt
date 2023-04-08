package studio.mandysa.music.logic.repository

import com.funny.data_saver.core.mutableDataSaverStateOf
import studio.mandysa.music.logic.config.DataSaverUtils

object SettingRepository {
    enum class ColorSource {
        Album, Wallpaper
    }

    var colorSource by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "MotionBlurSetting",
        initialValue = ColorSource.Album
    )

}