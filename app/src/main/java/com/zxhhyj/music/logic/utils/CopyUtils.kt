package com.zxhhyj.music.logic.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.zxhhyj.music.MainApplication

object CopyUtils {
    fun copyText(content: String) {
        val cm =
            MainApplication.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val mClipData = ClipData.newPlainText("Label", content)
        cm?.setPrimaryClip(mClipData)
    }
}