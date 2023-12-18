package com.zxhhyj.music.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.media.AudioManager
import android.media.session.MediaSession
import android.os.Build
import android.support.v4.media.session.MediaSessionCompat
import android.view.KeyEvent
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import androidx.media.session.MediaButtonReceiver
import com.zxhhyj.music.MainActivity
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.logic.utils.AudioFocusUtils
import com.zxhhyj.music.service.playermanager.PlayerManager
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class StarMusicService : LifecycleService(), AudioFocusUtils.OnAudioFocusChangeListener {

    companion object {

        /**
         * 静态常量NOTIFICATION_ID，用于标识通知的唯一ID
         */
        private const val NOTIFICATION_ID = 12 + 13

        /**
         * 使用Volatile关键字修饰isServiceAlive变量，确保多线程环境下的可见性
         */
        @Volatile
        var isServiceAlive = false
            private set

    }

    @SuppressLint("MissingPermission")
    @Synchronized
    private fun refreshMediaNotifications() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            startForeground(NOTIFICATION_ID, mStarMusicNotification!!.build())
            if (PlayerManager.pauseFlow.value) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    stopForeground(STOP_FOREGROUND_DETACH)
                } else {
                    @Suppress("DEPRECATION")
                    stopForeground(false)
                }
            }
        } else {
            mNotificationManager!!.notify(NOTIFICATION_ID, mStarMusicNotification!!.build())
        }
    }

    /**
     * 媒体会话
     */
    private var mMediaSession: MediaSessionCompat? = null

    /**
     * 通知管理器
     */
    private var mNotificationManager: NotificationManagerCompat? = null

    /**
     * 媒体通知
     */
    private var mStarMusicNotification: StarMusicNotification? = null

    /**
     * 管理音频焦点
     */
    private val mAudioFocusUtils by lazy { AudioFocusUtils(this, this) }

    override fun onCreate() {
        isServiceAlive = true
        // 将isServiceAlive设置为true，表示服务已启动
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        mAudioFocusUtils.abandonAudioFocus() // 放弃音频焦点
        mMediaSession?.apply {
            isActive = false // 设置媒体会话为非活动状态
            release() // 释放媒体会话资源
        }
        isServiceAlive = false // 将isServiceAlive设置为false，表示服务已停止
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (mNotificationManager == null) {
            mNotificationManager = StarMusicNotification.createChannel(this)
        }
        if (mMediaSession == null) {
            mMediaSession =
                MediaSessionCompat.fromMediaSession(this, MediaSession(this, packageName)).apply {
                    isActive = true
                    setSessionActivity(
                        PendingIntent.getActivity(
                            this@StarMusicService,
                            0,
                            Intent(this@StarMusicService, MainActivity::class.java),
                            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                        )
                    )
                    setCallback(MediaSessionCallback())
                }

            mStarMusicNotification = StarMusicNotification.Builder(this, mMediaSession!!)

            PlayerManager.currentSongFlow.onEach {
                if (it != null) {
                    mStarMusicNotification?.setSongBean(it)
                    refreshMediaNotifications()
                }
            }.launchIn(lifecycleScope)
            PlayerManager.progressFlow.onEach {
                mStarMusicNotification?.setPosition(it)
            }.launchIn(lifecycleScope)
            PlayerManager.durationFlow.onEach {
                mStarMusicNotification?.setDuration(it.toLong())
            }.launchIn(lifecycleScope)
            PlayerManager.pauseFlow.onEach {
                mStarMusicNotification?.setPlaying(it)
                refreshMediaNotifications()

                if (!it) {
                    mAudioFocusUtils.requestAudioFocus(
                        AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN
                    )
                } else {
                    mAudioFocusUtils.abandonAudioFocus()
                }
            }.launchIn(lifecycleScope)
            PlayerManager.playListFlow.onEach {
                //播放列表被清空
                //为了避免用户尝试恢复播放
                //直接把服务杀掉
                if (it == null) {
                    stopSelf()
                }
            }.launchIn(lifecycleScope)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            startForeground(NOTIFICATION_ID, mStarMusicNotification!!.build())
        }
        MediaButtonReceiver.handleIntent(mMediaSession, intent)
        return super.onStartCommand(intent, flags, startId)
    }

    /**
     * 监听MediaSessionCallback事件
     */
    inner class MediaSessionCallback : MediaSessionCompat.Callback() {
        override fun onSeekTo(pos: Long) {
            super.onSeekTo(pos)
            PlayerManager.seekTo(pos.toInt())
        }

        override fun onPlay() {
            super.onPlay()
            PlayerManager.start()
        }

        override fun onPause() {
            super.onPause()
            PlayerManager.pause()
        }

        override fun onSkipToPrevious() {
            super.onSkipToPrevious()
            PlayerManager.skipToPrevious()
        }

        override fun onSkipToNext() {
            super.onSkipToNext()
            PlayerManager.skipToNext()
        }

        override fun onMediaButtonEvent(mediaButtonEvent: Intent): Boolean {
            val action = mediaButtonEvent.action
            if (Intent.ACTION_MEDIA_BUTTON == action) {
                val keyEvent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    mediaButtonEvent.getParcelableExtra(
                        Intent.EXTRA_KEY_EVENT,
                        KeyEvent::class.java
                    )
                } else {
                    @Suppress("DEPRECATION")
                    mediaButtonEvent.getParcelableExtra(Intent.EXTRA_KEY_EVENT)
                }
                if (keyEvent != null && keyEvent.action == KeyEvent.ACTION_DOWN) {
                    // 处理媒体按钮按下事件
                    when (keyEvent.keyCode) {

                        KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, KeyEvent.KEYCODE_HEADSETHOOK -> {
                            when (keyEvent.repeatCount) {
                                0 -> {
                                    if (PlayerManager.pauseFlow.value) {
                                        PlayerManager.start()
                                    } else {
                                        PlayerManager.pause()
                                    }
                                }

                                1 -> {
                                    PlayerManager.skipToNext()
                                }

                                2 -> {
                                    PlayerManager.skipToPrevious()
                                }
                            }
                        }

                        KeyEvent.KEYCODE_MEDIA_PLAY -> {
                            PlayerManager.start()
                        }

                        KeyEvent.KEYCODE_MEDIA_PAUSE -> {
                            PlayerManager.pause()
                        }

                        KeyEvent.KEYCODE_MEDIA_NEXT -> {
                            PlayerManager.skipToNext()
                        }

                        KeyEvent.KEYCODE_MEDIA_PREVIOUS -> {
                            PlayerManager.skipToPrevious()
                        }

                        KeyEvent.KEYCODE_MEDIA_STOP -> {
                            PlayerManager.pause()
                            mNotificationManager!!.cancelAll()
                            stopSelf()
                        }
                    }
                }
            }
            return true
        }

    }

    override fun onLoss() {
        if (!SettingRepository.EnableIsPlayingWithOtherApps) {
            PlayerManager.pause()
        }
    }

    override fun onLossTransient() {
        if (!SettingRepository.EnableIsPlayingWithOtherApps) {
            PlayerManager.pause()
        }
    }

    override fun onLossTransientCanDuck() {

    }

    override fun onGain(lossTransient: Boolean, lossTransientCanDuck: Boolean) {
        if (lossTransient) {
            PlayerManager.start()
        }
    }
}