package studio.mandysa.music.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Icon
import android.media.MediaMetadata
import android.media.session.MediaSession
import android.media.session.PlaybackState
import android.os.Build
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.coroutineScope
import coil.imageLoader
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import studio.mandysa.music.R
import studio.mandysa.music.logic.ktx.playManager
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.service.playmanager.ktx.allArtist

class MediaPlayService : LifecycleService() {

    companion object {

        const val CHANNEL_ID = "notification_channel_id"

        const val ID = 1

        @Volatile
        @JvmStatic
        var instance: MediaPlayService? = null
    }

    init {
        instance = this
    }

    private inner class OnPlayStateReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            playManager {
                when (intent.extras?.getInt(PlayButtonReceiver.PLAY_BACK_STATE)) {
                    PlaybackState.STATE_PLAYING -> play()
                    PlaybackState.STATE_PAUSED -> pause()
                    PlaybackState.STATE_SKIPPING_TO_NEXT -> skipToNext()
                    PlaybackState.STATE_SKIPPING_TO_PREVIOUS -> skipToPrevious()
                    PlaybackState.STATE_STOPPED -> {
                        stop()
                        stopSelf()
                    }
                }
            }
        }
    }

    private fun refreshMediaNotifications() {
        startForeground()
        stopForeground()
    }

    private fun startForeground() {
        if (!isForeground) {
            startForeground(ID, mMediaNotification.setAction(!mPause).build())
            isForeground = true
        } else {
            mNotificationManager.notify(ID, mMediaNotification.setAction(!mPause).build())
        }
    }

    private fun stopForeground() {
        if (mPause) {
            if (!mAndroid12) {
                stopForeground(false)
                isForeground = false
            }
        }
    }

    private fun refreshMediaSession() {
        playManager {
            mSession.setPlaybackState(
                PlaybackState.Builder()
                    .setActions(PlaybackState.ACTION_SEEK_TO)
                    .setState(
                        if (pauseLiveData().value!!) PlaybackState.STATE_PAUSED else PlaybackState.STATE_PLAYING,
                        playingMusicProgressLiveData().value?.toLong() ?: 0,
                        1.0F
                    ).build()
            )
        }
    }

    //监听播放器的状态更新
    private fun initPlayManagerChanged() {
        playManager {
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
                        mMediaNotification
                            .setLargeIcon(
                                Icon.createWithBitmap(
                                    Bitmap.createScaledBitmap(
                                        drawable.bitmap.copy(
                                            Bitmap.Config.RGB_565,
                                            false
                                        ), 200, 200, true
                                    )
                                )
                            ).build()
                        refreshMediaNotifications()
                    } catch (e: Exception) {
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
                    mSession.setMetadata(
                        MediaMetadata.Builder()
                            .putLong(MediaMetadata.METADATA_KEY_DURATION, it.toLong())
                            .putString(MediaMetadata.METADATA_KEY_TITLE, title)
                            .putString(MediaMetadata.METADATA_KEY_ARTIST, artist.allArtist())
                            .build()
                    )
                }
            }
        }
    }

    private val mPause: Boolean
        get() = PlayManager.pauseLiveData().value!!

    private lateinit var mMediaNotification: MediaNotification

    private lateinit var mReceiver: OnPlayStateReceiver

    private lateinit var mSession: MediaSession

    private lateinit var mNotificationManager: NotificationManager

    private val mAndroid12 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    //判断是否是前台服务
    private var isForeground = false

    private fun registerBroadcast() {
        mReceiver = OnPlayStateReceiver()
        registerReceiver(mReceiver, IntentFilter(Intent.ACTION_MEDIA_BUTTON))
    }

    override fun onCreate() {
        super.onCreate()
        if (!::mNotificationManager.isInitialized) {
            mNotificationManager = getSystemService(NotificationManager::class.java)
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.channel_name),
                NotificationManager.IMPORTANCE_MIN
            )
            notificationChannel.description = getString(R.string.channel_description)
            notificationChannel.enableVibration(false)
            mNotificationManager.createNotificationChannel(notificationChannel)
            //通知渠道设置
        }
        mSession = MediaSession(this, packageName).apply {
            isActive = true
            setCallback(object : MediaSession.Callback() {
                override fun onSeekTo(pos: Long) {
                    super.onSeekTo(pos)
                    playManager {
                        seekTo(pos.toInt())
                    }
                }
            })
        }
        mMediaNotification = MediaNotification(this, mSession, CHANNEL_ID)
        registerBroadcast()
        initPlayManagerChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mReceiver)
        mSession.isActive = false
        instance = null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

}