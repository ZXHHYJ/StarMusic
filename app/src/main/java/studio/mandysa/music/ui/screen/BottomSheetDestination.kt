package studio.mandysa.music.ui.screen

import android.os.Parcelable
import androidx.compose.runtime.Composable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import studio.mandysa.music.service.playmanager.bean.SongBean

/**
 * @author 黄浩
 */

sealed class BottomSheetDestination : Parcelable {

    @Parcelize
    data class SongInfo(val song: @RawValue SongBean) : BottomSheetDestination()

    @Parcelize
    data class SongMenu(val song: @RawValue SongBean) : BottomSheetDestination()

    @Parcelize
    data class Message(val message: String) : BottomSheetDestination()

    /**
     * 限定在单个页面的BottomSheet
     */
    @Parcelize
    data class BottomSheet(val content: @RawValue @Composable () -> Unit) : BottomSheetDestination()

}