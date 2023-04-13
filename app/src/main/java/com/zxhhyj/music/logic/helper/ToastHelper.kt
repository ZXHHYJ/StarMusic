package com.zxhhyj.music.logic.helper

import android.widget.Toast
import androidx.annotation.StringRes
import com.zxhhyj.music.logic.config.application

object ToastHelper {
    fun toast(content: String) {
        Toast.makeText(application, content, Toast.LENGTH_LONG).show()
    }

    fun toast(@StringRes resId: Int) {
        toast(application.getString(resId))
    }
}