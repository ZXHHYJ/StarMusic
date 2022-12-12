package studio.mandysa.music.ui.screen.cloud.login

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class CloudLoginDestination : Parcelable {
    /**
     * 手机号登录
     */
    @Parcelize
    object Phone : CloudLoginDestination()

    /**
     * 二维码登录
     */
    @Parcelize
    object QRCode : CloudLoginDestination()
}