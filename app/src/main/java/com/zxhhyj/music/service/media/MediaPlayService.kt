package com.zxhhyj.music.service.media

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.media.AudioManager
import android.media.session.MediaSession
import android.os.Build
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.KeyEvent
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.coroutineScope
import androidx.media.session.MediaButtonReceiver
import coil.imageLoader
import coil.request.ImageRequest
import com.zxhhyj.music.MainActivity
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.utils.AudioFocusUtils
import com.zxhhyj.music.logic.utils.BitmapUtils
import com.zxhhyj.music.service.media.playmanager.PlayManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MediaPlayService : LifecycleService() {

    companion object {

        private const val NOTIFICATION_CHANNEL_ID = "media_notification_channel"

        private const val NOTIFICATION_ID = 1

        @Volatile
        var isServiceAlive = false
            private set

        private val PlaybackStateBuilder = PlaybackStateCompat.Builder()
            .setActions(
                PlaybackStateCompat.ACTION_SEEK_TO or
                        PlaybackStateCompat.ACTION_PLAY_PAUSE or
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or
                        PlaybackStateCompat.ACTION_STOP or
                        PlaybackStateCompat.ACTION_PLAY or
                        PlaybackStateCompat.ACTION_PAUSE
            )

        private val MediaMetadataBuilder = MediaMetadataCompat.Builder()
    }

    @Synchronized
    private fun refreshMediaNotifications() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            startForeground(NOTIFICATION_ID, mMediaNotification.build())
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
            mNotificationManager.notify(NOTIFICATION_ID, mMediaNotification.build())
        }
    }

    /**
     *  通知session播放状态、播放进度、播放速度
     */
    private fun refreshMediaSession() {
        mMediaSession.setPlaybackState(
            PlaybackStateBuilder.setState(
                if (PlayManager.pauseLiveData().value == true) PlaybackStateCompat.STATE_PAUSED else PlaybackStateCompat.STATE_PLAYING,
                PlayManager.progressLiveData().value?.toLong() ?: 0,
                0F
            ).build()
        )
    }

    private fun refreshMetadata(
        songName: String? = null,
        artistName: String? = null,
        duration: Int? = null
    ) {
        if (songName != null) {
            MediaMetadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_TITLE, songName)
        }
        if (artistName != null) {
            MediaMetadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artistName)
        }
        if (duration != null && duration > 0) {
            MediaMetadataBuilder.putLong(
                MediaMetadataCompat.METADATA_KEY_DURATION,
                duration.toLong()
            )
        }
        mMediaSession.setMetadata(MediaMetadataBuilder.build())
    }

    /**
     * 媒体通知
     */
    private lateinit var mMediaNotification: MediaNotification

    /**
     * 媒体会话
     */
    private lateinit var mMediaSession: MediaSessionCompat

    /**
     * 通知管理器
     */
    private lateinit var mNotificationManager: NotificationManagerCompat

    /**
     * 管理音频焦点
     */
    private lateinit var mAudioFocusUtils: AudioFocusUtils

    override fun onCreate() {
        isServiceAlive = true
        super.onCreate()
        mAudioFocusUtils = AudioFocusUtils(this, AudioFocusChangeListener())
        PlayManager.currentSongLiveData().observe(this@MediaPlayService) {
            if (it == null) return@observe
            mMediaNotification
                .setContentTitle(it.songName)
                .setContentText(it.artist.name)
            refreshMediaNotifications()
            lifecycle.coroutineScope.launch(Dispatchers.IO) {
                try {
                    val drawable = imageLoader.execute(
                        ImageRequest.Builder(this@MediaPlayService)
                            .data(it.coverUrl)
                            .build()
                    ).drawable as BitmapDrawable
                    withContext(Dispatchers.Main) {
                        mMediaNotification.setLargeIcon(BitmapUtils.compressBitmap(drawable.bitmap))
                        refreshMediaNotifications()
                    }
                } catch (_: Exception) {
                }
            }
            refreshMetadata(songName = it.songName, artistName = it.artist.name)
        }
        PlayManager.progressLiveData().observe(this@MediaPlayService) {
            refreshMediaSession()
        }
        PlayManager.pauseLiveData().observe(this@MediaPlayService) {
            refreshMediaNotifications()
            refreshMediaSession()

            if (it == false) {
                mAudioFocusUtils.requestAudioFocus(
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN
                )
            } else {
                mAudioFocusUtils.abandonAudioFocus()
            }
        }
        PlayManager.durationLiveData().observe(this@MediaPlayService) {
            refreshMetadata(duration = it)
        }
        PlayManager.playListLiveData().observe(this@MediaPlayService) {
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
        //配置通知
        if (!::mNotificationManager.isInitialized) {
            mNotificationManager = NotificationManagerCompat.from(this)
            val channel = NotificationChannelCompat.Builder(
                NOTIFICATION_CHANNEL_ID,
                NotificationManagerCompat.IMPORTANCE_LOW
            ).apply {
                setName(getString(R.string.channel_name))
                setDescription(getString(R.string.channel_name))
                setVibrationEnabled(false)
            }
            mNotificationManager.createNotificationChannel(channel.build())
        }
        //配置媒体会话
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
            mMediaNotification = MediaNotification(this, mMediaSession, NOTIFICATION_CHANNEL_ID)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            startForeground(NOTIFICATION_ID, mMediaNotification.build())
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
            PlayManager.pause()
        }

        override fun onLossTransient() {
            PlayManager.pause()
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