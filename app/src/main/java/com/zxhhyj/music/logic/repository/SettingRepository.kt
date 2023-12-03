package com.zxhhyj.music.logic.repository

import com.funny.data_saver.core.mutableDataSaverStateOf
import com.zxhhyj.music.logic.bean.PlayMemoryBean
import com.zxhhyj.music.logic.bean.WebDavConfig
import com.zxhhyj.music.logic.config.DataSaverUtils
import com.zxhhyj.music.service.playermanager.PlayerManager
import com.zxhhyj.music.ui.theme.PaletteStyle

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
        initialValue = SongSortEnum.SONG_NAME.ordinal
    )

    enum class SongSortEnum {
        SONG_NAME, SONG_DURATION, SINGER_NAME, DATE_MODIFIED
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
    var EqualizerConfig: IntArray? by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "EqualizerConfig",
        initialValue = null
    )

    /**
     * 播放模式
     */
    var PlayMode by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "PlayMode",
        initialValue = PlayerManager.PlayMode.LIST_LOOP
    )

    enum class DarkModeEnum {
        AUTO, LIGHT, DARK
    }

    /**
     * 深色模式
     */
    var DarkMode: DarkModeEnum by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "DarkMode_V2",
        initialValue = DarkModeEnum.AUTO
    )

    /**
     * 播放记忆
     */
    var PlayMemory: PlayMemoryBean? by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "PlayMemory",
        initialValue = null
    )

    /**
     * 是否启用播放记忆
     */
    var EnablePlayMemory by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "EnablePlayMemory",
        initialValue = true
    )

    enum class ThemeModeEnum {
        DEFAULT, MONET
    }

    /**
     * 主题
     */
    var ThemeMode: ThemeModeEnum by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "ThemeMode_V3",
        initialValue = ThemeModeEnum.DEFAULT
    )

    /**
     * M3调色板
     */
    var MonetPaletteStyle: PaletteStyle by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "MonetPaletteStyle",
        initialValue = PaletteStyle.TonalSpot
    )

    /**
     * M3对比度等级
     */
    var MonetContrastLevel by mutableDataSaverStateOf(
        dataSaverInterface = DataSaverUtils,
        key = "MonetContrastLevel",
        initialValue = 0.0
    )
}