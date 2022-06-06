package studio.mandysa.music.ui.screen.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mandysax.anna2.exception.AnnaException
import studio.mandysa.music.logic.network.api
import studio.mandysa.music.logic.toast.toast
import studio.mandysa.music.logic.user.UserManager


class LoginViewModel : ViewModel() {

    private val mLoginState = MutableLiveData(false)

    fun getLoginState(): LiveData<Boolean> = mLoginState

    fun login(mobilePhone: String, password: String) {
        mLoginState.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val model = api.login(mobilePhone, password)
                if (model.cookie.isNotEmpty()) {
                    UserManager.apply {
                        getCookieLiveData().postValue(model.cookie)
                        getUserIdLiveData().postValue(model.id)
                    }
                    return@launch
                }
                toast(model.msg)
            } catch (e: AnnaException) {
                toast(e.message!!)
            } finally {
                mLoginState.postValue(false)
            }
        }
    }

}
