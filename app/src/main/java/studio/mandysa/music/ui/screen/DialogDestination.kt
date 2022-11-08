package studio.mandysa.music.ui.screen

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import studio.mandysa.music.service.playmanager.bean.MetaMusic

sealed class DialogDestination : Parcelable {
    @Parcelize
    data class SongMenu(val model: @RawValue MetaMusic<*, *>) : DialogDestination()

    @Parcelize
    data class PlaylistMenu(val id: String) : DialogDestination()

    @Parcelize
    data class Message(val message: String) : DialogDestination()

    @Parcelize
    object MeMenu : DialogDestination()
}