package com.zxhhyj.music.ui.screen

import android.os.Parcelable
import androidx.compose.runtime.Composable
import com.zxhhyj.music.service.playmanager.bean.SongBean
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


sealed class BottomSheetDestination : Parcelable {

    @Parcelize
    data class SongInfo(val song: @RawValue SongBean) : BottomSheetDestination()

    @Parcelize
    data class SongMenu(val song: @RawValue SongBean) : BottomSheetDestination()

    @Parcelize
    data class Message(val message: String) : BottomSheetDestination()

    @Parcelize
    data class AddToPlayList(val song: @RawValue SongBean) : BottomSheetDestination()

    /**
     * 限定在单个页面的BottomSheet
     */
    @Parcelize
    data class BottomSheet(val content: @RawValue @Composable () -> Unit) : BottomSheetDestination()

}