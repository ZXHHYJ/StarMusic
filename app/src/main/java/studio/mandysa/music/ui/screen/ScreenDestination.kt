package studio.mandysa.music.ui.screen

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import studio.mandysa.music.service.playmanager.bean.SongBean

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
     * 歌单内容
     */
    @Parcelize
    data class PlaylistCnt(val id: String) : ScreenDestination()

    /**
     * 歌手内容
     */
    @Parcelize
    data class SingerCnt(val artist: @RawValue SongBean.Local.Artist) : ScreenDestination()

    /**
     * 网易云的歌手内容
     */
    @Parcelize
    data class NeteaseSingerCnt(val artist: @RawValue SongBean.Network.Artist) : ScreenDestination()

    /**
     * 专辑内容
     */
    @Parcelize
    data class AlbumCnt(val id: String) : ScreenDestination()

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
     * 关注的歌手
     */
    @Parcelize
    object ArtistSub : ScreenDestination()

    /**
     * 我的歌单
     */
    @Parcelize
    object MePlaylist : ScreenDestination()

    /**
     * 电台
     */
    @Parcelize
    object FmScreen : ScreenDestination()

    /**
     * 排行榜
     */
    @Parcelize
    object ToplistScreen : ScreenDestination()

    /**
     * 我的页面
     */
    @Parcelize
    object MeScreen : ScreenDestination()

}