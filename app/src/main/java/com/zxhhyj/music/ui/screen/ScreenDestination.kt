package com.zxhhyj.music.ui.screen

import android.os.Parcelable
import com.zxhhyj.music.service.playmanager.bean.SongBean
import kotlinx.parcelize.Parcelize


sealed class ScreenDestination : Parcelable {

    /**
     * 起始页
     */
    @Parcelize
    object Splash : ScreenDestination()

    /**
     * 主页
     */
    @Parcelize
    object Main : ScreenDestination()

    /**
     * 搜索
     */
    @Parcelize
    object Search : ScreenDestination()

    /**
     * 主题
     */
    @Parcelize
    object Theme : ScreenDestination()

    /**
     * 歌手详情
     */
    @Parcelize
    data class SingerCnt(val artist: SongBean.Local.Artist) : ScreenDestination()

    /**
     * 专辑详情
     */
    @Parcelize
    data class AlbumCnt(val album: SongBean.Local.Album) : ScreenDestination()

    /**
     * 专辑
     */
    @Parcelize
    object Album : ScreenDestination()

    /**
     * 设置
     */
    @Parcelize
    object Setting : ScreenDestination()

    /**
     * 关于
     */
    @Parcelize
    object About : ScreenDestination()

    /**
     * 扫描音乐
     */
    @Parcelize
    object ScanMusic : ScreenDestination()

    /**
     * 歌手
     */
    @Parcelize
    object Singer : ScreenDestination()

    /**
     * 播放列表
     */
    @Parcelize
    object PlayList : ScreenDestination()

}