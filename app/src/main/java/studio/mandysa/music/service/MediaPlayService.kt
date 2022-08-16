package studio.mandysa.music.service

import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.session.MediaSession
import android.os.Build
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.compose.runtime.snapshotFlow
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import androidx.media.session.MediaButtonReceiver
import coil.imageLoader
import coil.request.ImageRequest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import studio.mandysa.music.MainActivity
import studio.mandysa.music.R
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

    private fun refreshMediaNotifications() {
        startForeground()
        stopForeground()
    }

    private fun startForeground() {
        if (!isForeground) {
            startForeground(ID, mMediaNotification.setAction(!(PlayManager.pause ?: true)).build())
            isForeground = true
        } else {
            mNotificationManager.notify(
                ID,
                mMediaNotification.setAction(!(PlayManager.pause ?: true)).build()
            )
        }
    }

    private fun stopForeground() {
        if (PlayManager.pause != false) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                stopForeground(false)
                isForeground = false
            }
        }
    }

    private fun refreshMediaSession() {
        snapshotFlow {
            PlayManager.progress
        }.onEach { time ->
            if (::mMediaSession.isInitialized) {
                mMediaSession.setPlaybackState(
                    mPlaybackStateBuilder.setState(
                        if (PlayManager.pause != false) PlaybackStateCompat.STATE_PAUSED else PlaybackStateCompat.STATE_PLAYING,
                        time!!.toLong(),
                        1.0F
                    ).build()
                )
            }
        }.launchIn(lifecycleScope)
    }

    //监听播放器的状态更新
    private fun initPlayManagerChanged() {
        if (!mInitPlayManagerChanged) {
            mInitPlayManagerChanged = true
        }
        snapshotFlow {
            PlayManager.changeMusic
        }.onEach {
            if (it == null) return@onEach
            mMediaNotification
                .setContentTitle(it.title)
                .setContentText(it.artist.allArtist())
                .build()
            refreshMediaNotifications()
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
            } catch (e: Exception) {
            }
        }.launchIn(lifecycleScope)

        snapshotFlow {
            PlayManager.progress
        }.onEach {
            if (it == null) return@onEach
            refreshMediaSession()
        }.launchIn(lifecycleScope)

        snapshotFlow {
            PlayManager.pause
        }.onEach {
            if (it == null) return@onEach
            refreshMediaNotifications()
            refreshMediaSession()
        }.launchIn(lifecycleScope)

        snapshotFlow {
            PlayManager.duration
        }.onEach {
            if (it == null) return@onEach
            PlayManager.changeMusic?.let { model ->
                mMediaSession.setMetadata(
                    MediaMetadataCompat.Builder()
                        .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, it.toLong())
                        .putString(MediaMetadataCompat.METADATA_KEY_TITLE, model.title)
                        .putString(
                            MediaMetadataCompat.METADATA_KEY_ARTIST,
                            model.artist.allArtist()
                        )
                        .build()
                )
            }
        }.launchIn(lifecycleScope)
    }

    private lateinit var mMediaNotification: MediaNotification

    private lateinit var mMediaSession: MediaSessionCompat

    private lateinit var mNotificationManager: NotificationManagerCompat

    private var mInitPlayManagerChanged = false

    private val mPlaybackStateBuilder = PlaybackStateCompat.Builder()
        .setActions(PlaybackStateCompat.ACTION_SEEK_TO or PlaybackStateCompat.ACTION_PLAY_PAUSE or PlaybackStateCompat.ACTION_SKIP_TO_NEXT or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or PlaybackStateCompat.ACTION_STOP)

    //判断是否是前台服务
    private var isForeground = false

    override fun onDestroy() {
        super.onDestroy()
        mMediaSession.isActive = false
        mMediaSession.release()
        instance = null
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
        initPlayManagerChanged()
        return super.onStartCommand(intent, flags, startId)
    }

}