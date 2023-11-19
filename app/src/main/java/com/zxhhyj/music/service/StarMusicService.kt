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
import androidx.media.session.MediaButtonReceiver
import com.zxhhyj.music.MainActivity
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.logic.utils.AudioFocusUtils
import com.zxhhyj.music.service.playermanager.PlayerManager

class StarMusicService : LifecycleService() {

    companion object {

        private const val NOTIFICATION_ID = 12 + 13

        @Volatile
        var isServiceAlive = false
            private set

    }

    @SuppressLint("MissingPermission")
    @Synchronized
    private fun refreshMediaNotifications() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            startForeground(NOTIFICATION_ID, mStarMusicNotification.build())
            if (PlayerManager.pauseLiveData().value == true) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    stopForeground(STOP_FOREGROUND_DETACH)
                } else {
                    @Suppress("DEPRECATION")
                    stopForeground(false)
                }
            }
        } else {
            mNotificationManager.notify(NOTIFICATION_ID, mStarMusicNotification.build())
        }
    }

    /**
     * 媒体会话
     */
    private lateinit var mMediaSession: MediaSessionCompat

    /**
     * 通知管理器
     */
    private lateinit var mNotificationManager: NotificationManagerCompat

    private lateinit var mStarMusicNotification: StarMusicNotification

    /**
     * 管理音频焦点
     */
    private lateinit var mAudioFocusUtils: AudioFocusUtils

    override fun onCreate() {
        isServiceAlive = true
        super.onCreate()
        mAudioFocusUtils = AudioFocusUtils(this, AudioFocusChangeListener())
        PlayerManager.currentSongLiveData().observe(this@StarMusicService) {
            it?.apply {
                mStarMusicNotification.setSongBean(this)
                refreshMediaNotifications()
            }
        }
        PlayerManager.progressLiveData().observe(this@StarMusicService) {
            mStarMusicNotification.setPosition(it)
        }
        PlayerManager.pauseLiveData().observe(this@StarMusicService) {
            mStarMusicNotification.setPlaying(it)
            refreshMediaNotifications()

            if (it == false) {
                mAudioFocusUtils.requestAudioFocus(
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN
                )
            } else {
                mAudioFocusUtils.abandonAudioFocus()
            }
        }
        PlayerManager.playListLiveData().observe(this@StarMusicService) {
            //播放列表被清空
            //为了避免用户尝试恢复播放
            //直接把服务杀掉
            if (it == null) {
                stopSelf()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mAudioFocusUtils.abandonAudioFocus()
        mMediaSession.isActive = false
        mMediaSession.release()
        isServiceAlive = false
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!::mNotificationManager.isInitialized) {
            mNotificationManager = StarMusicNotification.createChannel(this)
        }
        if (!::mMediaSession.isInitialized) {
            mMediaSession =
                MediaSessionCompat.fromMediaSession(this, MediaSession(this, packageName))
            mMediaSession.isActive = true
            mMediaSession.setSessionActivity(
                PendingIntent.getActivity(
                    this,
                    0,
                    Intent(this, MainActivity::class.java),
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
            mMediaSession.setCallback(MediaSessionCallback())
            mStarMusicNotification = StarMusicNotification.Builder(this, mMediaSession)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            startForeground(NOTIFICATION_ID, mStarMusicNotification.build())
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
                                    if (PlayerManager.pauseLiveData().value == true) {
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
                            mNotificationManager.cancelAll()
                            stopSelf()
                        }
                    }
                }
            }
            return true
        }

    }

    /**
     * 对音频焦点获取与丢失做出反应
     */
    inner class AudioFocusChangeListener : AudioFocusUtils.OnAudioFocusChangeListener {

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
}