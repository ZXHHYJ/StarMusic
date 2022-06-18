package studio.mandysa.music.service

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import android.media.session.PlaybackState
import androidx.annotation.DrawableRes

/**
 * @author Huang hao
 */
object PlayButtonReceiver {
    const val PLAY_BACK_STATE = "pbs"
    fun buildMediaButtonAction(
        context: Context,
        @DrawableRes icon: Int,
        state: Int
    ): Notification.Action {
        return Notification.Action.Builder(
            Icon.createWithResource(context, icon),
            null,
            PendingIntent.getBroadcast(
                context, state, Intent(Intent.ACTION_MEDIA_BUTTON).putExtra(
                    PLAY_BACK_STATE, state
                ), PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        ).build()
    }

    fun buildDeleteIntent(context: Context): PendingIntent {
        return PendingIntent.getBroadcast(
            context, PlaybackState.STATE_STOPPED, Intent(Intent.ACTION_MEDIA_BUTTON).putExtra(
                PLAY_BACK_STATE, PlaybackState.STATE_STOPPED
            ), PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}