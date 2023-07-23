package com.zxhhyj.music.ui.screen

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class DialogDestination : Parcelable {
    /**
     * 隐私政策
     */
    @Parcelize
    object Splash : DialogDestination()

    /**
     * 媒体库为空时的提示
     */
    @Parcelize
    object MediaLibsEmpty : DialogDestination()

    /**
     * 添加播放列表
     */
    @Parcelize
    object AddPlayList : DialogDestination()

    /**
     * 扫描音乐
     */
    @Parcelize
    object ScanMusic : DialogDestination()
}