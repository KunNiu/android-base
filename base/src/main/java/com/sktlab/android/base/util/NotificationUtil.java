package com.sktlab.android.base.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class NotificationUtil {
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            IMPORTANCE_UNSPECIFIED,
            IMPORTANCE_NONE,
            IMPORTANCE_MIN,
            IMPORTANCE_LOW,
            IMPORTANCE_DEFAULT,
            IMPORTANCE_HIGH
    })
    public @interface Importance {
    }

    /**
     * @see NotificationManager#IMPORTANCE_UNSPECIFIED
     */
    public static final int IMPORTANCE_UNSPECIFIED = NotificationManager.IMPORTANCE_UNSPECIFIED;
    /**
     * @see NotificationManager#IMPORTANCE_NONE
     */
    public static final int IMPORTANCE_NONE = NotificationManager.IMPORTANCE_NONE;
    /**
     * @see NotificationManager#IMPORTANCE_MIN
     */
    public static final int IMPORTANCE_MIN = NotificationManager.IMPORTANCE_MIN;
    /**
     * @see NotificationManager#IMPORTANCE_LOW
     */
    public static final int IMPORTANCE_LOW = NotificationManager.IMPORTANCE_LOW;
    /**
     * @see NotificationManager#IMPORTANCE_DEFAULT
     */
    public static final int IMPORTANCE_DEFAULT = NotificationManager.IMPORTANCE_DEFAULT;
    /**
     * @see NotificationManager#IMPORTANCE_HIGH
     */
    public static final int IMPORTANCE_HIGH = NotificationManager.IMPORTANCE_HIGH;

    /**
     * Creates a notification channel that notifications can be posted to. See {@link
     * NotificationChannel} and {@link
     * NotificationManager#createNotificationChannel(NotificationChannel)} for details.
     *
     * @param context               A {@link Context}.
     * @param id                    The id of the channel. Must be unique per package. The value may be truncated if it's
     *                              too long.
     * @param nameResourceId        A string resource identifier for the user visible name of the channel.
     *                              The recommended maximum length is 40 characters. The string may be truncated if it's too
     *                              long. You can rename the channel when the system locale changes by listening for the {@link
     *                              Intent#ACTION_LOCALE_CHANGED} broadcast.
     * @param descriptionResourceId A string resource identifier for the user visible description of
     *                              the channel, or 0 if no description is provided. The recommended maximum length is 300
     *                              characters. The value may be truncated if it is too long. You can change the description of
     *                              the channel when the system locale changes by listening for the {@link
     *                              Intent#ACTION_LOCALE_CHANGED} broadcast.
     * @param importance            The importance of the channel. This controls how interruptive notifications
     *                              posted to this channel are. One of {@link #IMPORTANCE_UNSPECIFIED}, {@link
     *                              #IMPORTANCE_NONE}, {@link #IMPORTANCE_MIN}, {@link #IMPORTANCE_LOW}, {@link
     *                              #IMPORTANCE_DEFAULT} and {@link #IMPORTANCE_HIGH}.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void createNotificationChannel(
            Context context,
            String id,
            @StringRes int nameResourceId,
            @StringRes int descriptionResourceId,
            @Importance int importance) {
        NotificationManager notificationManager =
                checkNotNull(
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));
        NotificationChannel channel =
                new NotificationChannel(id, context.getString(nameResourceId), importance);
        if (descriptionResourceId != 0) {
            channel.setDescription(context.getString(descriptionResourceId));
        }
        notificationManager.createNotificationChannel(channel);
    }

    /**
     * Post a notification to be shown in the status bar. If a notification with the same id has
     * already been posted by your application and has not yet been canceled, it will be replaced by
     * the updated information. If {@code notification} is {@code null} then any notification
     * previously shown with the specified id will be cancelled.
     *
     * @param context      A {@link Context}.
     * @param id           The notification id.
     * @param notification The {@link Notification} to post, or {@code null} to cancel a previously
     *                     shown notification.
     */
    public static void setNotification(Context context, int id, @Nullable Notification notification) {
        NotificationManager notificationManager =
                checkNotNull((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));
        if (notification != null) {
            notificationManager.notify(id, notification);
        } else {
            notificationManager.cancel(id);
        }
    }

    private NotificationUtil() {
    }
}
