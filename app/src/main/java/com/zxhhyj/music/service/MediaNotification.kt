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
) :
    NotificationCompat.Builder(context, channelId) {

    private fun button(@DrawableRes icon: Int, action: Long) = NotificationCompat.Action(
        icon,
        "media button",
        MediaButtonReceiver.buildMediaButtonPendingIntent(
            context,
            action
        )
    )

    private val skipPrevious =
        button(R.drawable.ic_skip_previous, PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)

    private val pause = button(R.drawable.ic_pause, PlaybackStateCompat.ACTION_PLAY_PAUSE)

    private val play = button(R.drawable.ic_play, PlaybackStateCompat.ACTION_PLAY_PAUSE)

    private val skipNext = button(R.drawable.ic_skip_next, PlaybackStateCompat.ACTION_SKIP_TO_NEXT)

    private val stop = button(R.drawable.ic_round_clear_24, PlaybackStateCompat.ACTION_STOP)

    override fun build(): Notification {
        clearActions()
        listOf(skipPrevious, if (PlayManager.isPaused) play else pause, skipNext).forEach {
            addAction(it)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            addAction(stop)
        }
        return super.build()
    }

    init {
        setShowWhen(false)
        setCategory(Notification.CATEGORY_SERVICE)
        setSmallIcon(R.drawable.ic_notification_icon)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowCancelButton(true)
                    .setCancelButtonIntent(
                        MediaButtonReceiver.buildMediaButtonPendingIntent(
                            context,
                            PlaybackStateCompat.ACTION_STOP
                        )
                    )
                    .setShowActionsInCompactView(0, 1, 2, 3)
                    .setMediaSession(mediaSessionCompat.sessionToken)
            )
        // Enable launching the player by clicking the notification
        setContentIntent(mediaSessionCompat.controller.sessionActivity)
        // Stop the service when the notification is swiped away
        setDeleteIntent(
            MediaButtonReceiver.buildMediaButtonPendingIntent(
                context,
                PlaybackStateCompat.ACTION_STOP
            )
        )
        // Make the transport controls visible on the lockscreen
        setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
    }
}