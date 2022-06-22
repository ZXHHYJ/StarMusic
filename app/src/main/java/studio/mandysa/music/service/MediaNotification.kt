package studio.mandysa.music.service

import android.app.Notification
import android.app.Notification.MediaStyle
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.session.MediaSession
import android.media.session.PlaybackState
import android.os.Build
import studio.mandysa.music.MainActivity
import studio.mandysa.music.R

class MediaNotification(context: Context, session: MediaSession, channelId: String) :
    Notification.Builder(context, channelId) {

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
    private val mStopAction = PlayButtonReceiver.buildMediaButtonAction(
        context,
        R.drawable.ic_icons8_close,
        PlaybackState.STATE_STOPPED
    )

    fun setAction(playing: Boolean): MediaNotification {
        if (mAndroid12) {
            setActions(
                mPrevAction,
                if (!playing) mPlayAction else mPauseAction,
                mNextAction,
                mStopAction
            )
        } else setActions(mPrevAction, if (!playing) mPlayAction else mPauseAction, mNextAction)
        return this
    }

    private val mAndroid12 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    init {
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
        setCategory(Notification.CATEGORY_SERVICE)
        setSmallIcon(R.drawable.ic_launcher_foreground)
        style = if (mAndroid12) {
            MediaStyle().setShowActionsInCompactView(0, 1, 2, 3)
                .setMediaSession(session.sessionToken)
        } else {
            MediaStyle().setShowActionsInCompactView(0, 1, 2)
                .setMediaSession(session.sessionToken)
        }
    }
}