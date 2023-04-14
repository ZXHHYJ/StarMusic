package com.zxhhyj.music.service

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.session.MediaSession
import android.os.Build
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
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
import com.zxhhyj.music.service.playmanager.PlayManager
import com.zxhhyj.music.service.playmanager.PlayManager.isPaused
import com.zxhhyj.music.service.playmanager.PlayManager.playingMusicProgressLiveData
import com.zxhhyj.music.service.playmanager.ktx.allArtist
import com.zxhhyj.music.service.playmanager.ktx.artist
import com.zxhhyj.music.service.playmanager.ktx.coverUrl
import com.zxhhyj.music.service.playmanager.ktx.title
import kotlinx.coroutines.launch


class MediaPlayService : LifecycleService() {

    companion object {

        const val CHANNEL_ID = "notification_channel_id"

        const val ID = 1

        var isServiceAlive = false
    }

    private fun refreshMediaNotifications() {
        startForeground()
        stopForeground()
    }

    private fun startForeground() {
        if (!isForeground) {
            startForeground(ID, mMediaNotification.setAction(!isPaused()).build())
            isForeground = true
        } else {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            mNotificationManager.notify(ID, mMediaNotification.setAction(!isPaused()).build())
        }
    }

    private fun stopForeground() {
        if (isPaused()) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                stopForeground(false)
                isForeground = false
            }
        }
    }

    private fun refreshMediaSession() {
        mMediaSession.setPlaybackState(
            mPlaybackStateBuilder.setState(
                if (isPaused()) PlaybackStateCompat.STATE_PAUSED else PlaybackStateCompat.STATE_PLAYING,
                playingMusicProgressLiveData().value!!.toLong(),
                1.0F
            ).build()
        )
    }

    //监听播放器的状态更新
    private fun initPlayManagerChanged() {
        PlayManager.apply {
            changeMusicLiveData().observe(this@MediaPlayService) {
                mMediaNotification
                    .setContentTitle(it.title)
                    .setContentText(it.artist.allArtist())
                    .build()
                refreshMediaNotifications()
                lifecycle.coroutineScope.launch {
                    try {
                        val drawable = imageLoader.execute(
                            ImageRequest.Builder(this@MediaPlayService)
                                .data(it.coverUrl)
                                .build()
                        ).drawable as BitmapDrawable
                        mMediaNotification.setLargeIcon(
                            drawable.bitmap.copy(
                                Bitmap.Config.RGB_565,
                                false
                            )
                        ).build()
                        refreshMediaNotifications()
                    } catch (_: Exception) {
                    }
                }
            }
            playingMusicProgressLiveData().observe(this@MediaPlayService) {
                refreshMediaSession()
            }
            pauseLiveData().observe(this@MediaPlayService) {
                refreshMediaNotifications()
                refreshMediaSession()
            }
            playingMusicDurationLiveData().observe(this@MediaPlayService) {
                changeMusicLiveData().value?.apply {
                    mMediaSession.setMetadata(
                        MediaMetadataCompat.Builder()
                            .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, it.toLong())
                            .putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
                            .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist.allArtist())
                            .build()
                    )
                }
            }
        }
    }

    private lateinit var mMediaNotification: MediaNotification

    private lateinit var mMediaSession: MediaSessionCompat

    private lateinit var mNotificationManager: NotificationManagerCompat

    private val mPlaybackStateBuilder = PlaybackStateCompat.Builder()
        .setActions(PlaybackStateCompat.ACTION_SEEK_TO or PlaybackStateCompat.ACTION_PLAY_PAUSE or PlaybackStateCompat.ACTION_SKIP_TO_NEXT or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or PlaybackStateCompat.ACTION_STOP)

    //判断是否是前台服务
    private var isForeground = false

    override fun onCreate() {
        super.onCreate()
        isServiceAlive = true;
        initPlayManagerChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMediaSession.isActive = false
        mMediaSession.release()
        isServiceAlive = false;
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //配置通知
        if (!::mNotificationManager.isInitialized) {
            mNotificationManager = NotificationManagerCompat.from(this)
            val channel = NotificationChannelCompat.Builder(
                CHANNEL_ID,
                NotificationManagerCompat.IMPORTANCE_LOW
            )
            channel.setName(getString(R.string.channel_name))
            channel.setDescription(getString(R.string.channel_description))
            channel.setVibrationEnabled(false)
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
            mMediaSession.setCallback(object : MediaSessionCompat.Callback() {
                override fun onSeekTo(pos: Long) {
                    super.onSeekTo(pos)
                    PlayManager.seekTo(pos.toInt())
                }

                override fun onMediaButtonEvent(mediaButtonEvent: Intent?): Boolean {
                    println(mediaButtonEvent)
                    return super.onMediaButtonEvent(mediaButtonEvent)
                }

                override fun onStop() {
                    super.onStop()
                    PlayManager.stop()
                    stopSelf()
                }

                override fun onSkipToNext() {
                    super.onSkipToNext()
                    PlayManager.skipToNext()
                }

                override fun onSkipToPrevious() {
                    super.onSkipToPrevious()
                    PlayManager.skipToPrevious()
                }

                override fun onPlay() {
                    super.onPlay()
                    PlayManager.play()
                }

                override fun onPause() {
                    super.onPause()
                    PlayManager.pause()
                }
            })
            mMediaNotification = MediaNotification(this, mMediaSession, CHANNEL_ID)
        }
        MediaButtonReceiver.handleIntent(mMediaSession, intent)
        return super.onStartCommand(intent, flags, startId)
    }

}