package studio.mandysa.music.ui.screen.cloud.login

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drake.net.exception.HttpFailureException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import studio.mandysa.music.R
import studio.mandysa.music.logic.config.api
import studio.mandysa.music.logic.config.application
import studio.mandysa.music.logic.repository.UserRepository
import studio.mandysa.music.ui.common.PopWindows

class CloudLoginViewModel : ViewModel() {

    private val mDialogState = MutableLiveData(false)

    private val mSendCaptchaSecond = MutableLiveData(0)

    val dialogState: LiveData<Boolean> = mDialogState

    val sendCaptchaSecond: LiveData<Int> = mSendCaptchaSecond

    /**
     *
     */
    fun sendCaptcha(mobilePhone: String) {
        viewModelScope.launch {
            try {
                mDialogState.value = true
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
                    PopWindows.postValue(responseBody.message)
                }
            } catch (e: HttpFailureException) {
                PopWindows.postValue(application.getString(R.string.network_error))
            } catch (e: Exception) {
                PopWindows.postValue(e.message.toString())
            } finally {
                mDialogState.value = false
            }
        }
    }

    /**
     *
     */
    fun mobileLogin(mobilePhone: String, captcha: String) {
        viewModelScope.launch {
            try {
                mDialogState.value = true
                val model = api.phoneLogin(mobilePhone, captcha)
                if (model.cookie.isNotEmpty()) {
                    UserRepository.login(model.cookie, model.id)
                } else {
                    PopWindows.postValue(model.message)
                }
            } catch (e: Exception) {
                PopWindows.postValue(e.message.toString())
            } finally {
                mDialogState.value = false
            }
        }
    }

    private var mTask: Runnable? = null

    private val mHandler = Handler(Looper.myLooper()!!)

    private val mQRBitmapLiveData = MutableLiveData<Bitmap>()

    val qrBitmapLiveData = mQRBitmapLiveData

    fun refreshQr() {
        viewModelScope.launch {
            val qrkey = api.getQRKey()
            val qrimg = api.getQRImg(qrkey)
            if (mTask != null)
                mHandler.removeCallbacks(mTask!!)
            mTask = object : Runnable {
                override fun run() {
                    try {
                        viewModelScope.launch {
                            val check = api.check(qrkey)
                            if (check.code == 803 && check.cookie.isNotEmpty()) {
                                val userInfo = api.getUserInfo(check.cookie)
                                UserRepository.login(check.cookie, userInfo.userId)
                            }
                        }
                    } catch (_: Exception) {
                    }
                    mHandler.postDelayed(this, 500)
                }
            }
            mHandler.post(mTask!!)
            mQRBitmapLiveData.postValue(base64ToBitmap(qrimg))
        }
    }

    private fun base64ToBitmap(base64String: String): Bitmap {
        val decode: ByteArray = Base64.decode(base64String.split(",")[1], Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decode, 0, decode.size)
    }

    override fun onCleared() {
        super.onCleared()
        mTask?.let {
            mHandler.removeCallbacks(it)
        }
    }

}
