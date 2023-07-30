package com.zxhhyj.music.ui.screen

import android.os.Parcelable
import com.zxhhyj.music.logic.bean.PlayListModel
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.ui.screen.search.SearchScreenTabs
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


sealed class ScreenDestination : Parcelable {
    /**
     * 主页
     */
    @Parcelize
    data object Main : ScreenDestination()

    /**
     * 搜索
     */
    @Parcelize
    data class Search(val start: SearchScreenTabs) : ScreenDestination()

    /**
     * 主题
     */
    @Parcelize
    data object Theme : ScreenDestination()

    /**
     * 歌词设置
     */
    @Parcelize
    data object Lyric : ScreenDestination()

    /**
     * 隐藏的歌曲
     */
    @Parcelize
    data object HiddenSong : ScreenDestination()

    /**
     * 专辑
     */
    @Parcelize
    data object Album : ScreenDestination()

    /**
     * 设置
     */
    @Parcelize
    data object Setting : ScreenDestination()

    /**
     * 关于
     */
    @Parcelize
    data object About : ScreenDestination()

    /**
     * 媒体库
     */
    @Parcelize
    data object MediaLibs : ScreenDestination()

    /**
     * 歌手
     */
    @Parcelize
    data object Singer : ScreenDestination()

    /**
     * 播放列表
     */
    @Parcelize
    data object PlayList : ScreenDestination()

    /**
     * 实验室
     */
    @Parcelize
    data object Lab : ScreenDestination()

    /**
     * 歌手详情
     */
    @Parcelize
    data class SingerCnt(val artist: @RawValue SongBean.Artist) : ScreenDestination()

    /**
     * 专辑详情
     */
    @Parcelize
    data class AlbumCnt(val album: @RawValue SongBean.Album) : ScreenDestination()

    /**
     * 专辑详情
     */
    @Parcelize
    data class PlayListCnt(val model: @RawValue PlayListModel) : ScreenDestination()


}