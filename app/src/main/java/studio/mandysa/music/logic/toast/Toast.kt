package studio.mandysa.music.logic.toast

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast

@SuppressLint("StaticFieldLeak")
lateinit var ToastContext: Context

private val mHandler by lazy {
    Handler(Looper.getMainLooper())
}

fun toast(msg: String) {
    mHandler.post {
        Toast.makeText(ToastContext, msg, Toast.LENGTH_LONG).show()
    }
}