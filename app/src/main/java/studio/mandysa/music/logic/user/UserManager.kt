package studio.mandysa.music.logic.user

import androidx.lifecycle.LiveData
import simon.tuke.livedata
import simon.tuke.value

object UserManager {

    private var mUserId by value<String?>(key = "userid")

    private val mCookieLiveData by livedata<String?>(key = "cookie")

    var isLoginLiveData: LiveData<String?> = mCookieLiveData

    fun update(cookie: String, userId: String) {
        mUserId = userId
        mCookieLiveData.postValue(cookie)
    }

    fun cookie(): String {
        return mCookieLiveData.value!!
    }

    fun userId(): String {
        return mUserId!!
    }

    fun signOut() {
        mUserId = null
        mCookieLiveData.value = null
    }

}