package com.zxhhyj.music.ui.screen

import android.os.Parcelable
import com.zxhhyj.music.logic.bean.PlayListBean
import com.zxhhyj.music.logic.bean.SongBean
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 * 定义所有Sheet级别的导航
 */
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
    data class PlaylistMenu(val playListBean: @RawValue PlayListBean) : SheetDestination()

    @Parcelize
    data object MediaLibMenu : SheetDestination()

    @Parcelize
    data object Timer : SheetDestination()

    @Parcelize
    data object Start : SheetDestination()

}