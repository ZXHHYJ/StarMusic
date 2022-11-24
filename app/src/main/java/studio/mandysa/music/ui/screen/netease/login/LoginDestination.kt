package studio.mandysa.music.ui.screen.netease.login

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class LoginDestination : Parcelable {
    /**
     * 手机号登录
     */
    @Parcelize
    object Phone : LoginDestination()

    /**
     * 二维码登录
     */
    @Parcelize
    object QRCode : LoginDestination()
}