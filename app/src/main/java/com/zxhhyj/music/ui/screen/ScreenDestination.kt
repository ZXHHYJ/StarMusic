package com.zxhhyj.music.ui.screen

import android.os.Parcelable
import com.zxhhyj.music.logic.bean.PlayListModel
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
     * 歌词设置
     */
    @Parcelize
    object Lyric : ScreenDestination()

    /**
     * 隐藏的歌曲
     */
    @Parcelize
    object HiddenSong : ScreenDestination()

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
     * 媒体库
     */
    @Parcelize
    object MediaLibs : ScreenDestination()

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
     * 专辑详情
     */
    @Parcelize
    data class PlayListCnt(val model: PlayListModel) : ScreenDestination()


}