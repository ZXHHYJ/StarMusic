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
     * 设置
     */
    @Parcelize
    object Setting : ScreenDestination()

    /**
     * 关于
     */
    @Parcelize
    object About : ScreenDestination()


}