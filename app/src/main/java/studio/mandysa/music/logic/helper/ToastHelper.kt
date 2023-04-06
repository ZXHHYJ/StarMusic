package studio.mandysa.music.logic.helper

import android.widget.Toast
import studio.mandysa.music.logic.config.application

object ToastHelper {
    fun toast(content: String) {
        Toast.makeText(application, content, Toast.LENGTH_LONG).show()
    }
}