package com.zxhhyj.music.ui.screen

import android.os.Parcelable
import com.zxhhyj.music.service.playmanager.bean.SongBean
import kotlinx.parcelize.Parcelize


sealed class ScreenDestination : Parcelable {
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
    data class SingerCnt(val artist: SongBean.Artist) : ScreenDestination()

    /**
     * 专辑详情
     */
    @Parcelize
    data class AlbumCnt(val album: SongBean.Album) : ScreenDestination()

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
     * 媒体源
     */
    @Parcelize
    object MediaSource : ScreenDestination()

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