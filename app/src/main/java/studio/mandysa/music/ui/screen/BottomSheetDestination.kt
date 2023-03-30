package studio.mandysa.music.ui.screen

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import studio.mandysa.music.service.playmanager.bean.SongBean

sealed class BottomSheetDestination : Parcelable {
    @Parcelize
    data class SongMenu(val song: @RawValue SongBean) : BottomSheetDestination()

    @Parcelize
    data class PlaylistMenu(val id: String) : BottomSheetDestination()

    @Parcelize
    data class Message(val message: String) : BottomSheetDestination()

    @Parcelize
    object MeMenu : BottomSheetDestination()
}