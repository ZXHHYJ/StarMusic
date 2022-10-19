package studio.mandysa.music.ui.screen

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class ScreenDestination : Parcelable {

    @Parcelize
    object Main : ScreenDestination()

    /**
     * 网易云的screen从这里开始
     */
    @Parcelize
    object Search : ScreenDestination()

    @Parcelize
    data class Playlist(val id: String) : ScreenDestination()

    @Parcelize
    data class Singer(val id: String) : ScreenDestination()

    @Parcelize
    data class Album(val id: String) : ScreenDestination()

    @Parcelize
    object Setting : ScreenDestination()

    @Parcelize
    object About : ScreenDestination()

    @Parcelize
    object ArtistSub : ScreenDestination()

    @Parcelize
    object MePlaylist : ScreenDestination()

    @Parcelize
    object FmScreen : ScreenDestination()

    @Parcelize
    object ToplistScreen : ScreenDestination()

    @Parcelize
    object MeScreen : ScreenDestination()

    /**
     * 本地音乐的Screen从这里开始
     */
}