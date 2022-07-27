package studio.mandysa.music.logic.repository

import androidx.lifecycle.LiveData
import studio.mandysa.fastkt.serial
import studio.mandysa.fastkt.serialLiveData
import studio.mandysa.music.logic.config.noBackupFastKt

object UserRepository {

    private var mUserId by serial<String?>(key = "userid", def = null, noBackupFastKt)

    private val mCookieLiveData by serialLiveData<String?>(key = "cookie", def = null, noBackupFastKt)

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