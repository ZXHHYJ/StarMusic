package studio.mandysa.music.service

import android.app.Notification
import android.app.Notification.MediaStyle
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.session.PlaybackState
import studio.mandysa.music.MainActivity
import studio.mandysa.music.R

class MediaNotification(mService: MediaPlayService) : Notification.Builder(
    mService, mService.getString(R.string.channel_id)
) {

    init {
        val manager =
            mService.getSystemService(NotificationManager::class.java)
        val notificationChannel = NotificationChannel(
            mService.getString(R.string.channel_id),
            mService.getString(R.string.channel_name),
            NotificationManager.IMPORTANCE_LOW
        )
        notificationChannel.description = mService.getString(R.string.channel_description)
        notificationChannel.enableVibration(false)
        manager.createNotificationChannel(notificationChannel)
    }

    private val mPlayAction = PlayButtonReceiver.buildMediaButtonAction(
        mService,
        R.drawable.ic_play,
        PlaybackState.STATE_PLAYING
    )
    private val mPauseAction = PlayButtonReceiver.buildMediaButtonAction(
        mService,
        R.drawable.ic_pause,
        PlaybackState.STATE_PAUSED
    )
    private val mNextAction = PlayButtonReceiver.buildMediaButtonAction(
        mService,
        R.drawable.ic_skip_next,
        PlaybackState.STATE_SKIPPING_TO_NEXT
    )
    private val mPrevAction = PlayButtonReceiver.buildMediaButtonAction(
        mService,
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
        setShowWhen(false)
        setAction(false)
        setContentIntent(
            PendingIntent.getActivity(
                mService,
                0,
                Intent(mService, MainActivity::class.java),
                PendingIntent.FLAG_IMMUTABLE
            )
        )
        setDeleteIntent(PlayButtonReceiver.buildDeleteIntent(mService))
        style =
            MediaStyle().setShowActionsInCompactView(0, 1, 2)
                .setMediaSession(mService.getSessionToken())
        setCategory(Notification.CATEGORY_SERVICE)
        setSmallIcon(R.drawable.ic_launcher_foreground)
    }
}