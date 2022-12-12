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
     * 歌手内容
     */
    @Parcelize
    data class SingerCnt(val artist: SongBean.Local.Artist) : ScreenDestination()

    @Parcelize
    data class AlbumCnt(val album: SongBean.Local.Album) : ScreenDestination()

    /**
     * 网易云的歌手内容
     */
    @Parcelize
    data class CloudSingerCnt(val artist: @RawValue SongBean.Network.Artist) : ScreenDestination()

    /**
     * 网易云的歌单内容
     */
    @Parcelize
    data class CloudPlaylistCnt(val id: String) : ScreenDestination()

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
    object CloudArtistSub : ScreenDestination()

    /**
     * 我的歌单
     */
    @Parcelize
    object CloudMePlaylist : ScreenDestination()

    /**
     * 电台
     */
    @Parcelize
    object CloudFmScreen : ScreenDestination()

    /**
     * 排行榜
     */
    @Parcelize
    object CloudToplistScreen : ScreenDestination()

    /**
     * 我的页面
     */
    @Parcelize
    object CloudMeScreen : ScreenDestination()

}