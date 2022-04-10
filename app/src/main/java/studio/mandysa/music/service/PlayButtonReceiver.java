package studio.mandysa.music.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.media.session.PlaybackState;

import androidx.annotation.DrawableRes;

/**
 * @author Huang hao
 */
public final class PlayButtonReceiver {

    public final static String PLAY_BACK_STATE = "pbs";

    public static Notification.Action buildMediaButtonAction(Context context, @DrawableRes int icon, int state) {
        return new Notification.Action.Builder(
                Icon.createWithResource(context, icon),
                null
                ,
                PendingIntent.getBroadcast(context, state, new Intent(Intent.ACTION_MEDIA_BUTTON).putExtra(PLAY_BACK_STATE, state), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE)).build();
    }

    public static PendingIntent buildDeleteIntent(Context context) {
        return PendingIntent.getBroadcast(context, PlaybackState.STATE_STOPPED, new Intent(Intent.ACTION_MEDIA_BUTTON).putExtra(PLAY_BACK_STATE, PlaybackState.STATE_STOPPED), PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }
}
