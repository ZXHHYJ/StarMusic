package com.zxhhyj.music.logic.repository

import com.funny.data_saver.core.mutableDataSaverStateOf
import com.zxhhyj.music.logic.bean.WebDavConfig
import com.zxhhyj.music.logic.config.DataSaverUtils
import com.zxhhyj.music.service.playmanager.PlayManager

object SettingRepository {

    /**
     * Android媒体库
     */
    var EnableAndroidMediaLibs by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "EnableAndroidMediaLibs",
        initialValue = false
    )

    /**
     * 对cue文件的支持，CUE 文件是一种用于描述音频 CD 的文本文件格式。它通常与一个或多个音频轨道文件
     */
    var EnableCueSupport by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "EnableCueSupport",
        initialValue = false
    )

    /**
     * 首次启动APP(同意隐私政策)
     */
    var AgreePrivacyPolicy by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "AgreePrivacyPolicy",
        initialValue = false
    )

    /**
     * 显示歌词翻译
     */
    var EnableLyricsTranslation by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "EnableLyricsTranslation",
        initialValue = false
    )

    /**
     * StarMusic Pro
     */
    var EnableStarMusicPro by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "EnableStarMusicPro",
        initialValue = false
    )

    /**
     * 墨水屏支持
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
        initialValue = SongSortEnum.SONG_NAME.value
    )

    enum class SongSortEnum(val value: Int) {
        SONG_NAME(0), SONG_DURATION(1), SINGER_NAME(2), DATE_MODIFIED(3)
    }

    /**
     * 是否是降序
     */
    var Descending by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "Descending",
        initialValue = false
    )

    /**
     * 歌词字体大小
     */
    var LyricFontSize by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "LyricFontSize_V2",
        initialValue = 26
    )

    /**
     * 歌词字体加粗
     */
    var LyricFontBold by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "LyricFontBold",
        initialValue = false
    )

    /**
     * WebDav设置
     */
    var EnableWebDav by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "EnableWebDav",
        initialValue = false
    )

    /**
     * WebDav账户配置
     */
    var WebDavConfig by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "WebDavConfig",
        initialValue = WebDavConfig(String(), String(), String())
    )

    /**
     * 与其他App同时播放
     */
    var EnableIsPlayingWithOtherApps by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "EnableIsPlayingWithOtherApps",
        initialValue = false
    )

    /**
     * 均衡器
     */
    var EnableEqualizer by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "EnableEqualizer",
        initialValue = false
    )

    /**
     * 均衡器配置
     */
    var EqualizerConfig by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "EqualizerConfig",
        initialValue = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    )

    /**
     * 播放模式
     */
    var PlayMode by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "PlayMode",
        initialValue = PlayManager.PlayMode.LIST_LOOP
    )

    enum class ThemeModeEnum(val value: Int) {
        AUTO(0), LIGHT(1), DARK(2)
    }

    var ThemeMode by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "ThemeMode",
        initialValue = ThemeModeEnum.AUTO.value
    )

}