package studio.mandysa.music.ui.screen

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class ScreenDestination : Parcelable {

    @Parcelize
    object Main : ScreenDestination()

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
}