package studio.mandysa.music.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mandysax.anna2.exception.AnnaException
import simon.tuke.Tuke
import studio.mandysa.music.logic.network.api


class EventViewModel : ViewModel() {

    sealed class Status {
        data class Ok(val value: String) : Status()
        data class LoggingIn(val value: String) : Status()
        data class Fail(val value: String) : Status()
        data class Error(val e: AnnaException) : Status()
    }

    private val mCookieKey = "cookie_key"

    private val mUserIdKey = "userid_key"

    private val mUserIdLiveData = MutableLiveData<String>(Tuke.tukeGet(mUserIdKey))

    private val mCookieLiveData = MutableLiveData<String>(Tuke.tukeGet(mCookieKey))

    private val mLoginStatus = MutableLiveData<Status>()

    val loginStatus: LiveData<Status>
        get() = mLoginStatus

    fun login(mobilePhone: String, password: String) {
        mLoginStatus.value = Status.LoggingIn("")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val model = api
                    .login(mobilePhone, password, System.currentTimeMillis()).execute()
                if (model.cookie.isNotEmpty()) {
                    Tuke.tukeWrite(mCookieKey, model.cookie)
                    Tuke.tukeWrite(mUserIdKey, model.id)
                    mCookieLiveData.postValue(model.cookie)
                    mUserIdLiveData.postValue(model.id)
                    mLoginStatus.postValue(Status.Ok(model.cookie))
                    return@launch
                }
                mLoginStatus.postValue(Status.Fail(model.msg))
            } catch (e: AnnaException) {
                mLoginStatus.postValue(Status.Error(e))
            }
        }
    }

    fun getCookieLiveData(): LiveData<String> = mCookieLiveData

}
