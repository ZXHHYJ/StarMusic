package studio.mandysa.music.logic.repository

import androidx.lifecycle.LiveData
import com.drake.serialize.serialize.serial
import com.drake.serialize.serialize.serialLiveData

object UserRepository {

    private var mUserId by serial<String?>(name = "userid", default = null)

    private val mCookieLiveData by serialLiveData<String?>(name = "cookie", default = null)

    var isLoginLiveData: LiveData<String?> = mCookieLiveData

    fun login(cookie: String, userId: String) {
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