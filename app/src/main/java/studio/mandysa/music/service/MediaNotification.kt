package studio.mandysa.music.service

import android.app.Notification
import android.app.Notification.MediaStyle
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.session.MediaSession
import android.media.session.PlaybackState
import studio.mandysa.music.MainActivity
import studio.mandysa.music.R

const val CHANNEL_ID = "notification_channel_id"

class MediaNotification(context: Context, session: MediaSession) :
    Notification.Builder(context, CHANNEL_ID) {
    private val mPlayAction = PlayButtonReceiver.buildMediaButtonAction(
        context,
        R.drawable.ic_play,
        PlaybackState.STATE_PLAYING
    )
    private val mPauseAction = PlayButtonReceiver.buildMediaButtonAction(
        context,
        R.drawable.ic_pause,
        PlaybackState.STATE_PAUSED
    )
    private val mNextAction = PlayButtonReceiver.buildMediaButtonAction(
        context,
        R.drawable.ic_skip_next,
        PlaybackState.STATE_SKIPPING_TO_NEXT
    )
    private val mPrevAction = PlayButtonReceiver.buildMediaButtonAction(
        context,
        R.drawable.ic_skip_previous,
        PlaybackState.STATE_SKIPPING_TO_PREVIOUS
    )
    private var mIsPlaying = false

    fun setAction(isPlaying: Boolean): MediaNotification {
        mIsPlaying = isPlaying
        setActions(mPrevAction, if (!isPlaying) mPlayAction else mPauseAction, mNextAction)
        return this
    }

    init {
        val manager = context.getSystemService(NotificationManager::class.java)
        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.channel_name),
            NotificationManager.IMPORTANCE_LOW
        )
        notificationChannel.description = context.getString(R.string.channel_description)
        notificationChannel.enableVibration(false)
        manager.createNotificationChannel(notificationChannel)
        //通知渠道设置
        setShowWhen(false)
        setAction(false)
        setContentIntent(
            PendingIntent.getActivity(
                context,
                0,
                Intent(context, MainActivity::class.java),
                PendingIntent.FLAG_IMMUTABLE
            )
        )
        setDeleteIntent(PlayButtonReceiver.buildDeleteIntent(context))
        style =
            MediaStyle().setShowActionsInCompactView(0, 1, 2)
                .setMediaSession(session.sessionToken)
        setCategory(Notification.CATEGORY_SERVICE)
        setSmallIcon(R.drawable.ic_launcher_foreground)
    }
}