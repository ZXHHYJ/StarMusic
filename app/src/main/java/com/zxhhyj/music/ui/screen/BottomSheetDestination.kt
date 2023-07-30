package com.zxhhyj.music.ui.screen

import android.os.Parcelable
import com.zxhhyj.music.logic.bean.PlayListModel
import com.zxhhyj.music.logic.bean.SongBean
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


sealed class BottomSheetDestination : Parcelable {

    @Parcelize
    data class SongInfo(val song: @RawValue SongBean) : BottomSheetDestination()

    @Parcelize
    data class SongMenu(val song: @RawValue SongBean) : BottomSheetDestination()

    @Parcelize
    data class AddToPlayList(val song: @RawValue SongBean) : BottomSheetDestination()

    @Parcelize
    data class PlaylistMenu(val model: @RawValue PlayListModel) : BottomSheetDestination()

}