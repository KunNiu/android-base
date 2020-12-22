package com.sktlab.android.base.sample;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.sktlab.android.base.util.AppUtil;
import com.sktlab.android.base.util.NotificationUtil;
import com.sktlab.android.base.util.RandomUtil;

import java.io.Serializable;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("11111111111", "Receiver:" + intent.getAction());
        if (intent.getAction().equals(AppUtil.getPackageName(context) + ".ALARM_ACTION")) {
            Log.d("11111111111", "AlarmReceiver");
            Bundle bundle = intent.getBundleExtra("bundle");
            Serializable serializable = bundle.getSerializable("object");
            if (serializable != null) {
                String text = (String) serializable;
                String id = RandomUtil.randomString(5);
                NotificationUtil.createNotificationChannel(context, id, R.string.app_name, R.string.success, NotificationUtil.IMPORTANCE_HIGH);
                Notification.Builder builder = new Notification.Builder(context, id)
                        .setContentTitle(text)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setAutoCancel(true);
                NotificationUtil.setNotification(context, 1, builder.build());
            }
        }
    }
}
