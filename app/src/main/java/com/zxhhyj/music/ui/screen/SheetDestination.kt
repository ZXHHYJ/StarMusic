package com.zxhhyj.music.ui.screen

import android.os.Parcelable
import com.zxhhyj.music.logic.bean.PlayListModel
import com.zxhhyj.music.logic.bean.SongBean
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


sealed class SheetDestination : Parcelable {

    @Parcelize
    data class SongInfo(val song: @RawValue SongBean) : SheetDestination()

    @Parcelize
    data class SongMenu(val song: @RawValue SongBean) : SheetDestination()

    @Parcelize
    data class AddToPlayList(val song: @RawValue SongBean) : SheetDestination()

    @Parcelize
    data class PlaylistMenu(val model: @RawValue PlayListModel) : SheetDestination()

    @Parcelize
    data object SongSort : SheetDestination()

}