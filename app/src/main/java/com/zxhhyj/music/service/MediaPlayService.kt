package com.zxhhyj.music.service

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.AudioManager
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
import com.zxhhyj.music.logic.helper.AudioFocusHelper
import com.zxhhyj.music.service.playmanager.PlayManager
import kotlinx.coroutines.launch

class MediaPlayService : LifecycleService() {

    companion object {

        const val CHANNEL_ID = "notification_channel_id"

        const val ID = 1

        var isServiceAlive = false

        private val PlaybackStateBuilder = PlaybackStateCompat.Builder()
            .setActions(PlaybackStateCompat.ACTION_SEEK_TO or PlaybackStateCompat.ACTION_PLAY_PAUSE or PlaybackStateCompat.ACTION_SKIP_TO_NEXT or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or PlaybackStateCompat.ACTION_STOP)

        private val MediaMetadataBuilder = MediaMetadataCompat.Builder()
    }

    data class State(
        /*  var title: String,
          var artist: ArrayList<SongBean.Artist>,*/
        @PlaybackStateCompat.State var state: Int,
        var position: Long,
        var playbackSpeed: Float = 0.0f
    )

    private fun refreshMediaNotifications() {
        if (!isForeground) {
            startForeground(ID, mMediaNotification.build())
            isForeground = true
        } else {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            mNotificationManager.notify(
                ID,
                mMediaNotification.build()
            )
        }

        if (PlayManager.isPaused) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                stopForeground(STOP_FOREGROUND_DETACH)
            } else {
                @Suppress("DEPRECATION")
                stopForeground(false)
            }
            isForeground = false
        }
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
    private lateinit var mAudioFocusHelper: AudioFocusHelper

    /**
     * 判断是否是前台服务
     */
    private var isForeground = false

    private var state = State(PlaybackStateCompat.STATE_PAUSED, 0, 1.0f)

    override fun onCreate() {
        isServiceAlive = true
        super.onCreate()
        mAudioFocusHelper = AudioFocusHelper(this, AudioFocusChangeListener())
        PlayManager.changeMusicLiveData().observe(this@MediaPlayService) {
            if (it == null) return@observe
            mMediaNotification
                .setContentTitle(it.songName)
                .setContentText(it.artist.name)
                .build()
            refreshMediaNotifications()
            lifecycle.coroutineScope.launch {
                try {
                    val drawable = imageLoader.execute(
                        ImageRequest.Builder(this@MediaPlayService)
                            .data(it.album.coverUrl)
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

            mMediaSession.setMetadata(
                MediaMetadataBuilder
                    .putString(MediaMetadataCompat.METADATA_KEY_TITLE, it.songName)
                    .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, it.artist.name)
                    .build()
            )
        }
        PlayManager.progressLiveData().observe(this@MediaPlayService) {
            state =
                state.copy(position = it.toLong())
            //通知session播放状态、播放进度、播放速度
            mMediaSession.setPlaybackState(
                PlaybackStateBuilder.setState(
                    state.state,
                    state.position,
                    state.playbackSpeed
                ).build()
            )
        }
        PlayManager.pauseLiveData().observe(this@MediaPlayService) {
            refreshMediaNotifications()

            state =
                state.copy(state = if (it) PlaybackStateCompat.STATE_PAUSED else PlaybackStateCompat.STATE_PLAYING)
            //通知session播放状态、播放进度、播放速度
            mMediaSession.setPlaybackState(
                PlaybackStateBuilder.setState(
                    state.state,
                    state.position,
                    state.playbackSpeed
                ).build()
            )

            if (it == false) {
                mAudioFocusHelper.requestAudioFocus(
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN
                )
            } else {
                mAudioFocusHelper.abandonAudioFocus()
            }
        }
        PlayManager.durationLiveData().observe(this@MediaPlayService) {
            //通知session歌曲时长
            mMediaSession.setMetadata(
                MediaMetadataCompat.Builder()
                    .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, it.toLong())
                    .build()
            )
        }
        PlayManager.changePlayListLiveData().observe(this@MediaPlayService) {
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
        mAudioFocusHelper.abandonAudioFocus()
        mMediaSession.isActive = false
        mMediaSession.release()
        isServiceAlive = false
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
            mMediaSession.setCallback(MediaSessionCallback())
            mMediaNotification = MediaNotification(this, mMediaSession, CHANNEL_ID)
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
    }

    /**
     * 对音频焦点获取与丢失做出反应
     */
    inner class AudioFocusChangeListener : AudioFocusHelper.OnAudioFocusChangeListener {

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
                PlayManager.play()
            }
        }
    }
}