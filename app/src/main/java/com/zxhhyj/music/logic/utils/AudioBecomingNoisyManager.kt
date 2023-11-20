package com.zxhhyj.music.logic.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager

class AudioBecomingNoisyManager(
    private val context: Context,
    private val onAudioBecomingNoisy: () -> Unit
) {
    private val receiver = AudioBecomingNoisyReceiver()
    private var receiverRegistered = false

    interface EventListener {
        fun onAudioBecomingNoisy()
    }

    /**
     * Enables the [AudioBecomingNoisyManager] which calls [ ][EventListener.onAudioBecomingNoisy] upon receiving an intent of [ ][AudioManager.ACTION_AUDIO_BECOMING_NOISY].
     *
     * @param enabled True if the listener should be notified when audio is becoming noisy.
     */
    fun setEnabled(enabled: Boolean) {
        if (enabled && !receiverRegistered) {
            context.registerReceiver(
                receiver,
                IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
            )
            receiverRegistered = true
        } else if (!enabled && receiverRegistered) {
            context.unregisterReceiver(receiver)
            receiverRegistered = false
        }
    }

    private inner class AudioBecomingNoisyReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY == intent.action) {
                onAudioBecomingNoisy()
            }
        }
    }
}