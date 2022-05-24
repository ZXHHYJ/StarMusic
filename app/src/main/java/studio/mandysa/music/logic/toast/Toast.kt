package studio.mandysa.music.logic.toast

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast

@SuppressLint("StaticFieldLeak")
lateinit var ToastContext: Context

fun toast(msg: String) {
    Toast.makeText(ToastContext, msg, Toast.LENGTH_LONG).show()
}