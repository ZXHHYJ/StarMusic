package com.zxhhyj.music.logic.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri

object ActivityHelper {
    fun openWeb(context: Context, url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        val activity = context as? Activity
        activity?.startActivity(intent)
    }
}