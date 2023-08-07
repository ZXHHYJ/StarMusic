package com.zxhhyj.music.service

import android.app.Notification
import android.content.Context
import android.os.Build
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import androidx.media.session.MediaButtonReceiver
import com.zxhhyj.music.R
import com.zxhhyj.music.service.playmanager.PlayManager


class MediaNotification(
    private val context: Context,
    mediaSessionCompat: MediaSessionCompat,
    channelId: String
) : NotificationCompat.Builder(context, channelId) {

    companion object {
        private const val EMPTY_STRING = ""
    }

    // 创建一个带有图标和 MediaButtonReceiver 的按钮
    private fun button(@DrawableRes icon: Int, action: Long) = NotificationCompat.Action(
        icon,
        EMPTY_STRING,
        MediaButtonReceiver.buildMediaButtonPendingIntent(
            context,
            action
        )
    )

    // 创建跳转到上一曲、播放/暂停、下一曲和停止的按钮
    private val skipPrevious =
        button(R.drawable.ic_skip_previous, PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
    private val pause = button(R.drawable.ic_pause, PlaybackStateCompat.ACTION_PLAY_PAUSE)
    private val play = button(R.drawable.ic_play, PlaybackStateCompat.ACTION_PLAY_PAUSE)
    private val skipNext = button(R.drawable.ic_skip_next, PlaybackStateCompat.ACTION_SKIP_TO_NEXT)
    private val stop = button(R.drawable.ic_round_clear_24, PlaybackStateCompat.ACTION_STOP)

    override fun build(): Notification {
        clearActions()
        // 根据当前的播放状态添加不同的按钮
        listOf(
            skipPrevious,
            if (PlayManager.pauseLiveData().value == true) play else pause,
            skipNext
        ).forEach {
            addAction(it)
        }
        // Android 12 及以上版本支持停止操作
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            addAction(stop)
        }
        return super.build()
    }

    init {
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
                    .setMediaSession(mediaSessionCompat.sessionToken)
            )
        // 点击通知时启动媒体会话的活动
        setContentIntent(mediaSessionCompat.controller.sessionActivity)
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