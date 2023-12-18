package com.zxhhyj.music.logic.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager

/**
 * 音频变为嘈杂时的管理器类，用于接收音频变为嘈杂的广播，并触发相应的回调函数。
 *
 * @param context 上下文
 * @param onAudioBecomingNoisy 音频变为嘈杂时的回调函数
 */
class AudioBecomingNoisyManager(
    private val context: Context,
    private val onAudioBecomingNoisy: () -> Unit
) {
    private val receiver = AudioBecomingNoisyReceiver()
    private var receiverRegistered = false

    /**
     * 设置是否启用 [AudioBecomingNoisyManager]，当接收到 [AudioManager.ACTION_AUDIO_BECOMING_NOISY] 的意图时，调用 [onAudioBecomingNoisy] 函数进行通知。
     *
     * @param enabled 如果为 true，则在音频变为嘈杂时通知监听器。
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

    /**
     * 音频变为嘈杂时的广播接收器。
     */
    private inner class AudioBecomingNoisyReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY == intent.action) {
                onAudioBecomingNoisy()
            }
        }
    }
}