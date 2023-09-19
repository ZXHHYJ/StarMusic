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

    /**
     * 诚信付费了属于是
     */
    var EnableStarMusicPro by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "EnableStarMusicPro",
        initialValue = false
    )

    /**
     * 启用墨水屏支持
     */
    var EnableLinkUI by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "EnableLinkUI",
        initialValue = false
    )

    /**
     * app启动后自动播放音乐
     */
    var EnableAutoPlayMusic by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "EnableAutoPlayMusic",
        initialValue = false
    )

    /**
     * 排除一分钟以内的歌曲
     */
    var EnableExcludeSongsUnderOneMinute by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "EnableExcludeSongsUnderOneMinute",
        initialValue = true
    )

    /**
     * 在歌曲Item中显示音质标签
     */
    var EnableShowSoundQualityLabel by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "EnableShowSoundQualityLabel",
        initialValue = true
    )

    /**
     * 歌曲排序方式
     */
    var SongSort by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "SongSort",
        initialValue = SongSortType.SONG_NAME.value
    )

    enum class SongSortType(val value: Int) {
        SONG_NAME(0), DURATION(1), SINGER_NAME(2)
    }

}