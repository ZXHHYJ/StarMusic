package com.zxhhyj.music.ui.screen

import android.os.Parcelable
import com.zxhhyj.music.logic.bean.PlayListBean
import com.zxhhyj.music.logic.bean.SongBean
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


sealed class SheetDestination : Parcelable {

    @Parcelize
    data class SongParameters(val songBean: @RawValue SongBean) : SheetDestination()

    @Parcelize
    data class SongMenu(val songBean: @RawValue SongBean) : SheetDestination()

    @Parcelize
    data class SongPanel(val songBean: @RawValue SongBean) : SheetDestination()

    @Parcelize
    data class AddToPlayList(val songBean: @RawValue SongBean) : SheetDestination()

    @Parcelize
    data class PlaylistMenu(val model: @RawValue PlayListBean) : SheetDestination()

    @Parcelize
    data object SongSort : SheetDestination()

    @Parcelize
    data object Timer : SheetDestination()

    @Parcelize
    data object Start : SheetDestination()

}