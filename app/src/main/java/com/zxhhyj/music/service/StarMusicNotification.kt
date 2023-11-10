package com.zxhhyj.music.service

import android.app.Notification
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import androidx.media.session.MediaButtonReceiver
import com.zxhhyj.music.R
import com.zxhhyj.music.logic.bean.SongBean
import com.zxhhyj.music.logic.utils.BitmapUtils


open class StarMusicNotification private constructor(
    private val context: Context,
    private val sessionCompat: MediaSessionCompat
) {

    class Builder(context: Context, sessionCompat: MediaSessionCompat) :
        StarMusicNotification(context, sessionCompat)

    private val notificationCompat = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)

    private var playing = false

    private var position = 0

    private val skipPrevious =
        NotificationCompat.Action(
            R.drawable.ic_skip_previous,
            "SKIP_TO_PREVIOUS",
            MediaButtonReceiver.buildMediaButtonPendingIntent(
                context,
                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
            )
        )
    private val pause =
        NotificationCompat.Action(
            R.drawable.ic_pause,
            "PAUSE",
            MediaButtonReceiver.buildMediaButtonPendingIntent(
                context,
                PlaybackStateCompat.ACTION_PAUSE
            )
        )
    private val play =
        NotificationCompat.Action(
            R.drawable.ic_play,
            "PLAY",
            MediaButtonReceiver.buildMediaButtonPendingIntent(
                context,
                PlaybackStateCompat.ACTION_PLAY
            )
        )
    private val skipNext =
        NotificationCompat.Action(
            R.drawable.ic_skip_next,
            "NEXT",
            MediaButtonReceiver.buildMediaButtonPendingIntent(
                context,
                PlaybackStateCompat.ACTION_SKIP_TO_NEXT
            )
        )

    private val stop =
        NotificationCompat.Action(
            R.drawable.ic_round_clear_24,
            "STOP",
            MediaButtonReceiver.buildMediaButtonPendingIntent(
                context, PlaybackStateCompat.ACTION_STOP
            )
        )

    init {
        notificationCompat.apply {
            // 隐藏通知的时间戳
            setShowWhen(false)
            // 将通知归类为服务类别
            setCategory(Notification.CATEGORY_SERVICE)
            // 设置通知的小图标
            setSmallIcon(R.drawable.ic_notification_icon)
                .setStyle(
                    androidx.media.app.NotificationCompat.MediaStyle()
                        // 显示取消按钮
                        .setShowCancelButton(true)
                        // 设置取消按钮的操作为停止
                        .setCancelButtonIntent(
                            MediaButtonReceiver.buildMediaButtonPendingIntent(
                                context,
                                PlaybackStateCompat.ACTION_STOP
                            )
                        )
                        // 在紧凑视图中显示的操作按钮
                        .setShowActionsInCompactView(0, 1, 2, 3)
                        // 设置媒体会话
                        .setMediaSession(sessionCompat.sessionToken)
                )
            // 点击通知时启动媒体会话的活动
            setContentIntent(sessionCompat.controller.sessionActivity)
            // 在通知被滑动删除时停止服务
            setDeleteIntent(
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    context,
                    PlaybackStateCompat.ACTION_STOP
                )
            )
            // 在锁屏界面上显示媒体控制按钮
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        }
    }

    fun build(): Notification {
        notificationCompat.clearActions()
        // 根据当前的播放状态添加不同的按钮
        listOf(
            skipPrevious,
            if (playing) play else pause,
            skipNext
        ).forEach {
            notificationCompat.addAction(it)
        }
        // Android 12 及以上版本支持停止操作
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            notificationCompat.addAction(stop)
        }
        return notificationCompat.build()
    }

    fun setSongBean(songBean: SongBean) {
        val metadata = MediaMetadataCompat.Builder()
            .putString(MediaMetadataCompat.METADATA_KEY_TITLE, songBean.songName)
            .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, songBean.artist.name)
            .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, songBean.album.name)
            .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, songBean.duration)

        notificationCompat.apply {
            setContentTitle(songBean.songName)
            setContentText(songBean.artist.name)
            try {
                val contentResolver = context.contentResolver
                contentResolver?.openInputStream(songBean.coverUrl.toUri())?.use { inputStream ->
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    val minBitmap = BitmapUtils.compressBitmap(bitmap)
                    setLargeIcon(minBitmap)
                    metadata.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, minBitmap)
                }
            } catch (_: Exception) {
            }
        }

        sessionCompat.setMetadata(metadata.build())
    }

    fun setPlaying(playing: Boolean) {
        this.playing = playing
        sessionCompat.setPlaybackState(
            PlaybackStateBuilder.setState(
                if (playing) PlaybackStateCompat.STATE_PAUSED else PlaybackStateCompat.STATE_PLAYING,
                position.toLong(),
                0F
            ).build()
        )
    }

    fun setPosition(position: Int) {
        this.position = position
        sessionCompat.setPlaybackState(
            PlaybackStateBuilder.setState(
                if (playing) PlaybackStateCompat.STATE_PAUSED else PlaybackStateCompat.STATE_PLAYING,
                position.toLong(),
                0F
            ).build()
        )
    }

    companion object {

        private const val NOTIFICATION_CHANNEL_ID = "media_notification_channel"

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

        fun createChannel(context: Context): NotificationManagerCompat {
            val notificationManager = NotificationManagerCompat.from(context)
            val channel = NotificationChannelCompat.Builder(
                NOTIFICATION_CHANNEL_ID,
                NotificationManagerCompat.IMPORTANCE_LOW
            ).apply {
                setName(context.getString(R.string.channel_name))
                setDescription(context.getString(R.string.channel_name))
                setVibrationEnabled(false)
                setShowBadge(false)
                setSound(null, null)
            }
            notificationManager.createNotificationChannel(channel.build())
            return notificationManager
        }
    }
}