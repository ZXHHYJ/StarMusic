package studio.mandysa.music.logic.helper

import com.funny.data_saver.core.DataSaverInterface
import com.funny.data_saver_mmkv.DefaultDataSaverMMKV

/**
 * @author 黄浩
 */
// See https://github.com/FunnySaltyFish/ComposeDataSaver | 在 Jetpack Compose 中优雅完成数据持久化
val DataSaverUtils : DataSaverInterface = DefaultDataSaverMMKV