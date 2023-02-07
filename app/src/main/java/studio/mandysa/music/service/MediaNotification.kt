package studio.mandysa.music.service

import android.app.Notification
import android.content.Context
import android.os.Build
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.media.session.MediaButtonReceiver
import studio.mandysa.music.R

class MediaNotification(private val context: Context, mediaSessionCompat: MediaSessionCompat, channelId: String) :
    NotificationCompat.Builder(context, channelId) {

    fun setAction(playing: Boolean): MediaNotification {
        clearActions()
        addAction(
            NotificationCompat.Action(
                R.drawable.ic_skip_previous,
                "test",
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    context,
                    PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                )
            )
        )
        addAction(
            NotificationCompat.Action(
                if (playing) R.drawable.ic_pause else R.drawable.ic_play,
                "test",
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    context,
                    PlaybackStateCompat.ACTION_PLAY_PAUSE
                )
            )
        )
        addAction(
            NotificationCompat.Action(
                R.drawable.ic_skip_next,
                "test",
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    context,
                    PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                )
            )
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            addAction(
                NotificationCompat.Action(
                    R.drawable.ic_round_clear_24,
                    "test",
                    MediaButtonReceiver.buildMediaButtonPendingIntent(
                        context,
                        PlaybackStateCompat.ACTION_STOP
                    )
                )
            )
        }
        return this
    }

    init {
        setShowWhen(false)
        setAction(false)
        setCategory(Notification.CATEGORY_SERVICE)
        setSmallIcon(R.drawable.ic_launcher_foreground)
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