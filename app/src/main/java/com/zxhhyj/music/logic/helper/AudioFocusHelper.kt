package com.zxhhyj.music.logic.helper

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * 用于帮助处理应用程序的音频焦点的获取与丢失。
 *
 *
 * **使用方法：**
 *
 *
 *
 *  1. 创建一个 AudioFocusHelper 对象。需提供以下两个参数：
 *
 *  1. 一个 Context 对象；
 *  1. 一个 [media.helper.AudioFocusHelper.OnAudioFocusChangeListener] 对象，该接口中的回调方法会在当前应用程序的音频焦点丢失与获取时被调用。
 *
 *  1. 调用 [.requestAudioFocus] 方法请求音频焦点。
 *  1. 调用 [.abandonAudioFocus] 方法放弃音频焦点。
 *
 *
 */
@Suppress("DEPRECATION")
class AudioFocusHelper(
    context: Context,
    listener: OnAudioFocusChangeListener
) {
    private val mAudioManager: AudioManager?
    private val mListener: OnAudioFocusChangeListener
    private var mAudioFocusRequest: AudioFocusRequest? = null
    private var mAudioFocusChangeListener: AudioManager.OnAudioFocusChangeListener? = null
    private var mLossTransient = false
    private var mLossTransientCanDuck = false

    /**
     * 创建一个 [media.helper.AudioFocusHelper] 对象。
     *
     * @param context  Context 对象，不能为 null
     * @param listener 事件监听器，不能为 null
     */
    init {
        mAudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        mListener = listener
        initAudioFocusChangeListener()
    }

    private fun initAudioFocusChangeListener() {
        mAudioFocusChangeListener = AudioManager.OnAudioFocusChangeListener { focusChange ->
            when (focusChange) {
                AudioManager.AUDIOFOCUS_LOSS -> {
                    mListener.onLoss()
                    mLossTransient = false
                    mLossTransientCanDuck = false
                }

                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                    mLossTransient = true
                    mListener.onLossTransient()
                }

                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                    mLossTransientCanDuck = true
                    mListener.onLossTransientCanDuck()
                }

                AudioManager.AUDIOFOCUS_GAIN -> {
                    mListener.onGain(mLossTransient, mLossTransientCanDuck)
                    mLossTransient = false
                    mLossTransientCanDuck = false
                }
            }
        }
    }

    /**
     * 获取音频焦点。
     *
     * @param streamType   受焦点请求影响的主要音频流类型。该参数通常是 `AudioManager.STREAM_MUSIC`。更多音频
     * 流类型，请查看 [AudioManager](https://developer.android.google.cn/reference/android/media/AudioManager)
     * 类中前缀为 `STREAM_` 的整形常量。
     * @param durationHint 可以是以下 4 个值之一：
     *
     *  1. `AudioManager.AUDIOFOCUS_GAIN`：表示获取未知时长的音频焦点；
     *  1. `AudioManager.AUDIOFOCUS_GAIN_TRANSIENT`：表示短暂的获取音频焦点；
     *  1. `AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK`：表示短暂的获取音频焦点，同时指示先前的焦点所有者可以通过降低音量（duck），并继续播放；
     *  1. `AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE`：（API level 19）：表示短暂获取音频焦点，在此期间，其他任何应用程序或系统组件均不应播放任何内容。
     *
     * 更多内容，请查看 [AudioManager](https://developer.android.google.cn/reference/android/media/AudioManager) 文档。
     * @return AudioManager.AUDIOFOCUS_REQUEST_GRANTED（申请成功）或者 AudioManager.AUDIOFOCUS_REQUEST_FAILED（申请失败）
     */
    fun requestAudioFocus(streamType: Int, durationHint: Int): Int {
        if (mAudioManager == null) {
            return AudioManager.AUDIOFOCUS_REQUEST_FAILED
        }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requestAudioFocusAPI26(streamType, durationHint)
        } else mAudioManager.requestAudioFocus(
            mAudioFocusChangeListener,
            streamType,
            durationHint
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun requestAudioFocusAPI26(streamType: Int, durationHint: Int): Int {
        if (mAudioManager == null) {
            return AudioManager.AUDIOFOCUS_REQUEST_FAILED
        }
        mAudioFocusRequest = AudioFocusRequest.Builder(durationHint)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setLegacyStreamType(streamType)
                    .build()
            )
            .setOnAudioFocusChangeListener(mAudioFocusChangeListener!!)
            .build()
        return mAudioManager.requestAudioFocus(mAudioFocusRequest!!)
    }

    /**
     * （主动）放弃音频焦点。
     */
    fun abandonAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            abandonAudioFocusAPI26()
            return
        }
        mAudioManager?.abandonAudioFocus(mAudioFocusChangeListener)
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun abandonAudioFocusAPI26() {
        mAudioManager?.abandonAudioFocusRequest(mAudioFocusRequest?:return)
    }

    /**
     * 可用于监听当前应用程序的音频焦点的获取与丢失。
     */
    interface OnAudioFocusChangeListener {
        /**
         * 音频焦点永久性丢失（此时应暂停播放）
         */
        fun onLoss()

        /**
         * 音频焦点暂时性丢失（此时应暂停播放）
         */
        fun onLossTransient()

        /**
         * 音频焦点暂时性丢失（此时只需降低音量，不需要暂停播放）
         */
        fun onLossTransientCanDuck()

        /**
         * 重新获取到音频焦点。
         *
         *
         * 如果应用因 [.onLossTransientCanDuck] 事件而降低了音量（lossTransientCanDuck 参数为 true），
         * 那么此时应恢复正常的音量。
         *
         * @param lossTransient        指示音频焦点是否是暂时性丢失，如果是，则此时可以恢复播放。
         * @param lossTransientCanDuck 指示音频焦点是否是可降低音量的暂时性丢失，如果是，则此时只需恢复音量即可。
         */
        fun onGain(lossTransient: Boolean, lossTransientCanDuck: Boolean)
    }
}