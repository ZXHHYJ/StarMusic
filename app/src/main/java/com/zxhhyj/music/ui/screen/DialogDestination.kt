package com.zxhhyj.music.ui.screen

import android.os.Parcelable
import com.zxhhyj.music.logic.bean.PlayListModel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

sealed class DialogDestination : Parcelable {
    /**
     * 隐私政策
     */
    @Parcelize
    data object Splash : DialogDestination()

    /**
     * 媒体库为空时的提示
     */
    @Parcelize
    data object MediaLibsEmpty : DialogDestination()

    /**
     * 创建播放列表
     */
    @Parcelize
    data object CreatePlayList : DialogDestination()

    /**
     * 扫描音乐
     */
    @Parcelize
    data object ScanMusic : DialogDestination()

    @Parcelize
    data class EditPlayListTitle(val model: @RawValue PlayListModel) : DialogDestination()
}