package studio.mandysa.music.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mandysax.anna2.exception.AnnaException
import simon.tuke.Tuke
import studio.mandysa.music.logic.model.LoginModel
import studio.mandysa.music.logic.model.NeteaseCloudMusicApi
import studio.mandysa.music.logic.network.ServiceCreator


class EventViewModel : ViewModel() {

    private val mCookieKey = "cookie_key"

    private val mUserIdKey = "userid_key"

    private val mUserIdLiveData = MutableLiveData<String>(Tuke.tukeGet(mUserIdKey))

    private val mCookieLiveData = MutableLiveData<String>(Tuke.tukeGet(mCookieKey))

    fun login(mobilePhone: String, password: String): LiveData<LoginModel?> {
        return MutableLiveData<LoginModel?>().also {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val model = ServiceCreator.create(NeteaseCloudMusicApi::class.java)
                        .login(mobilePhone, password, System.currentTimeMillis()).execute()
                    it.postValue(model)
                    if (model.cookie.isNotEmpty()) {
                        Tuke.tukeWrite(mCookieKey, model.cookie)
                        Tuke.tukeWrite(mUserIdKey, model.id)
                        mCookieLiveData.postValue(model.cookie)
                        mUserIdLiveData.postValue(model.id)
                    }
                } catch (e: AnnaException) {
                    it.postValue(null)
                }
            }
        }
    }

    fun getCookieLiveData(): LiveData<String> = mCookieLiveData

    fun etUserIdLiveData(): LiveData<String> = mUserIdLiveData

}
