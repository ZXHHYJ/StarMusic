package studio.mandysa.music.logic.user

import com.drake.serialize.serialize.serialLiveData
import studio.mandysa.music.ui.screen.login.LoginScreen

object UserManager {

    private val mUserIdLiveData by serialLiveData<String>()

    private val mCookieLiveData by serialLiveData<String>()

    /**
     * 提供给[LoginScreen]使用
     */
    fun getCookieLiveData() = mCookieLiveData

    fun getUserIdLiveData() = mUserIdLiveData

    init {
        mUserIdLiveData.value
        //fix bug
    }

    fun cookie(): String {
        return mCookieLiveData.value!!
    }

    fun userId(): String {
        return mUserIdLiveData.value!!
    }

}