package studio.mandysa.music.ui.screen.login

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import studio.mandysa.music.logic.network.api
import studio.mandysa.music.logic.user.UserManager
import studio.mandysa.music.ui.common.POPWindows

class LoginViewModel : ViewModel() {

    private val mDialogState = MutableLiveData(false)

    private val mSendCaptchaSecond = MutableLiveData(0)

    val dialogState: LiveData<Boolean> = mDialogState

    val sendCaptchaSecond: LiveData<Int> = mSendCaptchaSecond

    fun sendCaptcha(mobilePhone: String) {
        try {
            mDialogState.value = true
            viewModelScope.launch {
                val responseBody = api.sendCaptcha(mobilePhone)
                if (responseBody.code == 200) {
                    withContext(Dispatchers.Main) {
                        object : CountDownTimer(30000, 1000) {
                            override fun onTick(millisUntilFinished: Long) {
                                mSendCaptchaSecond.postValue((millisUntilFinished / 1000).toInt())
                            }

                            override fun onFinish() {
                                mSendCaptchaSecond.postValue(0)
                            }
                        }.start()
                    }
                } else {
                    POPWindows.postValue(responseBody.message)
                }
                mDialogState.value = false
            }
        } catch (e: Exception) {
            POPWindows.postValue(e.message.toString())
            mDialogState.value = false
        }
    }

    fun login(mobilePhone: String, captcha: String) {
        try {
            mDialogState.value = true
            viewModelScope.launch {
                val model = api.phoneLogin(mobilePhone, captcha)
                if (model.cookie.isNotEmpty()) {
                    UserManager.update(model.cookie, model.id)
                } else {
                    POPWindows.postValue("登录失败")
                }
            }
            mDialogState.value = false
        } catch (e: Exception) {
            POPWindows.postValue(e.message.toString())
        }
    }

}
