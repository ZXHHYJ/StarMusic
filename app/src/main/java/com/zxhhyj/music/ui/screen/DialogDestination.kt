package com.zxhhyj.music.ui.screen

import android.os.Parcelable
import com.zxhhyj.music.logic.bean.PlayListBean
import com.zxhhyj.music.logic.bean.SongBean
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

sealed class DialogDestination : Parcelable {

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
     * 扫描Android媒体库
     */
    @Parcelize
    data object ScanAndroidMediaLib : DialogDestination()

    /**
     * 扫描WebDav媒体库
     */
    @Parcelize
    data object SyncWebDavMediaLib : DialogDestination()

    @Parcelize
    data class EditPlayListTitle(val model: @RawValue PlayListBean) : DialogDestination()

    @Parcelize
    data class DeleteSong(val songBean: @RawValue SongBean) : DialogDestination()

    @Parcelize
    data object CustomTimer : DialogDestination()
}