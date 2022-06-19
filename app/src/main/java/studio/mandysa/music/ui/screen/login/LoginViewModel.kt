package studio.mandysa.music.ui.screen.login

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch
import mandysax.anna2.exception.AnnaException
import studio.mandysa.music.logic.network.api
import studio.mandysa.music.logic.user.UserManager


class LoginViewModel : ViewModel() {

    private var mTask: Runnable? = null

    private val mHandler = Handler(Looper.myLooper()!!)

    private val mQRBitmapLiveData = MutableLiveData<Bitmap>()

    fun getQRBitmap(): LiveData<Bitmap> = mQRBitmapLiveData

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            val qrkey = api.getQRKey()
            val qrimg = api.getQRImg(qrkey)
            check {
                val check = api.check(qrkey)
                if (check.code == 803 && check.cookie.isNotEmpty()) {
                    val userInfo = api.getUserInfo(check.cookie)
                    UserManager.apply {
                        getCookieLiveData().postValue(check.cookie)
                        getUserIdLiveData().postValue(userInfo.userId)
                    }
                }
            }
            mQRBitmapLiveData.postValue(base64ToBitmap(qrimg))
        }
    }

    private inline fun check(crossinline task: () -> Unit) {
        if (mTask != null)
            mHandler.removeCallbacks(mTask!!)
        mTask = object : Runnable {
            override fun run() {
                try {
                    viewModelScope.launch(Dispatchers.IO) {
                        task.invoke()
                    }
                } catch (e: AnnaException) {
                }
                mHandler.postDelayed(this, 500)
            }
        }
        mHandler.post(mTask!!)
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
