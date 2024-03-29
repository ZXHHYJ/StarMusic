package com.zxhhyj.music.ui.screen

import android.os.Parcelable
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.ui.screen.search.SearchScreenTabs
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 * 定义所有屏幕级别的导航
 */
sealed class ScreenDestination : Parcelable {

    /**
     * 主页
     */
    @Parcelize
    data object Main : ScreenDestination()

    /**
     * 付款界面
     */
    @Parcelize
    data object Pay : ScreenDestination()

    /**
     * 搜索
     */
    @Parcelize
    data class Search(val start: SearchScreenTabs) : ScreenDestination()

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
     * 媒体库配置
     */
    @Parcelize
    data object MediaLibConfig : ScreenDestination()

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
     * 文件夹管理
     */
    @Parcelize
    data object FolderManager : ScreenDestination()

    /**
     * 文件夹
     */
    @Parcelize
    data object Folder : ScreenDestination()

    /**
     * 文件夹内容预览
     */
    @Parcelize
    data class SongsPreview(val songs: List<SongBean>) : ScreenDestination()

    /**
     * 文件夹内容
     */
    @Parcelize
    data class FolderCnt(val folder: @RawValue com.zxhhyj.music.logic.bean.Folder) :
        ScreenDestination()

    /**
     * 杂项
     */
    @Parcelize
    data object Misc : ScreenDestination()

    /**
     * Star Music PRO
     */
    @Parcelize
    data object Pro : ScreenDestination()

    /**
     * 个性化
     */
    @Parcelize
    data object Personalize : ScreenDestination()

    /**
     * 均衡器
     */
    @Parcelize
    data object Equalizer : ScreenDestination()

    /**
     * WebDav配置设置
     */
    @Parcelize
    data object WebDavConfig : ScreenDestination()

    /**
     * 主题
     */
    @Parcelize
    data object Theme : ScreenDestination()

    /**
     * 歌手详情
     */
    @Parcelize
    data class SingerCnt(val artistName: String) : ScreenDestination()

    /**
     * 专辑详情
     */
    @Parcelize
    data class AlbumCnt(val albumName: String) : ScreenDestination()

    /**
     * 专辑详情
     * @param id [com.zxhhyj.music.logic.bean.PlayListBean.uuid]
     */
    @Parcelize
    data class PlayListCnt(val id: String) : ScreenDestination()

    @Parcelize
    data class WebDavEdit(val indexInWebDavSourceList: Int) : ScreenDestination()

    @Parcelize
    data class WebDavFolderManager(val indexInWebDavSourceList: Int, val path: String) :
        ScreenDestination()

    @Parcelize
    data object AndroidMediaLibSetting : ScreenDestination()

    @Parcelize
    data object CacheManager : ScreenDestination()

    @Parcelize
    data object WebDav : ScreenDestination()
}