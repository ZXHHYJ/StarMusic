package com.zxhhyj.music.service

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.session.MediaSession
import android.os.Build
import android.support.v4.media.session.MediaSessionCompat
import android.view.KeyEvent
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LifecycleService
import androidx.media.session.MediaButtonReceiver
import com.zxhhyj.music.MainActivity
import com.zxhhyj.music.logic.repository.SettingRepository
import com.zxhhyj.music.logic.utils.AudioFocusUtils
import com.zxhhyj.music.service.playmanager.PlayManager

class StarMusicService : LifecycleService() {

    companion object {

        private const val NOTIFICATION_ID = 12 + 13

        @Volatile
        var isServiceAlive = false
            private set

    }

    @Synchronized
    private fun refreshMediaNotifications() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            startForeground(NOTIFICATION_ID, mStarMusicNotification.build())
            if (PlayManager.pauseLiveData().value == true) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    stopForeground(STOP_FOREGROUND_DETACH)
                } else {
                    @Suppress("DEPRECATION")
                    stopForeground(false)
                }
            }
        } else {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
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
        PlayManager.currentSongLiveData().observe(this@StarMusicService) {
            it?.apply {
                mStarMusicNotification.setSongBean(this)
                refreshMediaNotifications()
            }
        }
        PlayManager.progressLiveData().observe(this@StarMusicService) {
            mStarMusicNotification.setPosition(it)
        }
        PlayManager.pauseLiveData().observe(this@StarMusicService) {
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
        PlayManager.playListLiveData().observe(this@StarMusicService) {
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
            PlayManager.seekTo(pos.toInt())
        }

        override fun onPlay() {
            super.onPlay()
            PlayManager.start()
        }

        override fun onPause() {
            super.onPause()
            PlayManager.pause()
        }

        override fun onSkipToPrevious() {
            super.onSkipToPrevious()
            PlayManager.skipToPrevious()
        }

        override fun onSkipToNext() {
            super.onSkipToNext()
            PlayManager.skipToNext()
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
                                    if (PlayManager.pauseLiveData().value == true) {
                                        PlayManager.start()
                                    } else {
                                        PlayManager.pause()
                                    }
                                }

                                1 -> {
                                    PlayManager.skipToNext()
                                }

                                2 -> {
                                    PlayManager.skipToPrevious()
                                }
                            }
                        }

                        KeyEvent.KEYCODE_MEDIA_PLAY -> {
                            PlayManager.start()
                        }

                        KeyEvent.KEYCODE_MEDIA_PAUSE -> {
                            PlayManager.pause()
                        }

                        KeyEvent.KEYCODE_MEDIA_NEXT -> {
                            PlayManager.skipToNext()
                        }

                        KeyEvent.KEYCODE_MEDIA_PREVIOUS -> {
                            PlayManager.skipToPrevious()
                        }

                        KeyEvent.KEYCODE_MEDIA_STOP -> {
                            PlayManager.pause()
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
                PlayManager.pause()
            }
        }

        override fun onLossTransient() {
            if (!SettingRepository.EnableIsPlayingWithOtherApps) {
                PlayManager.pause()
            }
        }

        override fun onLossTransientCanDuck() {

        }

        override fun onGain(lossTransient: Boolean, lossTransientCanDuck: Boolean) {
            if (lossTransient) {
                PlayManager.start()
            }
        }
    }
}