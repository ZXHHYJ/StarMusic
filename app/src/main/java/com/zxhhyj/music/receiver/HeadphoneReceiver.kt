package com.zxhhyj.music.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.zxhhyj.music.service.media.playmanager.PlayManager


class HeadphoneReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_HEADSET_PLUG) {
            val state = intent.getIntExtra("state", -1)
            if (state == 0) {
                PlayManager.pause()
            }
        }
    }
}