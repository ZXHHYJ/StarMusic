package studio.mandysa.music.logic.repository

import com.funny.data_saver.core.mutableDataSaverStateOf
import studio.mandysa.music.logic.config.DataSaverUtils

object SettingRepository {
    enum class MotionBlurSetting {
        MotionBlur, AlbumColor
    }

    var motionBlurSetting by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "MotionBlurSetting",
        initialValue = MotionBlurSetting.AlbumColor
    )
}