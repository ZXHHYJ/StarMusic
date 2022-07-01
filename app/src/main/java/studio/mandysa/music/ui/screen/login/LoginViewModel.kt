package studio.mandysa.music.ui.screen.login

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import studio.mandysa.music.logic.network.api
import studio.mandysa.music.logic.user.UserManager


class LoginViewModel : ViewModel() {

    private val mDialogState = MutableLiveData(false)

    private val mMessage = MutableLiveData<String>()

    private val mSendCaptchaSecond = MutableLiveData(0)

    val dialogState: LiveData<Boolean> = mDialogState

    val message: LiveData<String> = mMessage

    val sendCaptchaSecond: LiveData<Int> = mSendCaptchaSecond

    fun sendCaptcha(mobilePhone: String) {
        try {
            mDialogState.value = true
            viewModelScope.launch(Dispatchers.IO) {
                val responseBody = api.sendCaptcha(mobilePhone)
                if (responseBody.code == 200) {
                    object : CountDownTimer(30000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            mSendCaptchaSecond.postValue((millisUntilFinished / 1000).toInt())
                        }

                        override fun onFinish() {
                            mSendCaptchaSecond.postValue(0)
                        }
                    }
                } else {
                    // TODO: 失败
                }
            }
        } catch (e: Exception) {
            // TODO: 失败
        } finally {
            mDialogState.value = false
        }
    }

    fun login(mobilePhone: String, captcha: String) {
        try {
            mDialogState.value = true
            viewModelScope.launch(Dispatchers.IO) {
                val model = api.phoneLogin(mobilePhone, captcha)
                if (model.cookie.isNotEmpty()) {
                    UserManager.update(model.cookie, model.id)
                }
            }
        } catch (e: Exception) {

        } finally {
            mDialogState.postValue(false)
        }
    }

}
