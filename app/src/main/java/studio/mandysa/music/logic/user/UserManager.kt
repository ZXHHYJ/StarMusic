package studio.mandysa.music.logic.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import simon.tuke.livedata

object UserManager {

    private val mUserIdLiveData by livedata("")

    private val mCookieLiveData by livedata("")

    var isLoginLiveData: LiveData<Boolean> = mCookieLiveData.map {
        it.isNotEmpty()
    }

    suspend fun update(cookie: String, userId: String) {
        withContext(Dispatchers.Main) {
            mUserIdLiveData.value = userId
            mCookieLiveData.value = cookie
        }
    }

    fun cookie(): String {
        return mCookieLiveData.value!!
    }

    fun userId(): String {
        return mUserIdLiveData.value!!
    }

    fun signOut() {
        mUserIdLiveData.value = ""
        mCookieLiveData.value = ""
    }

}