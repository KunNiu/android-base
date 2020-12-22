package com.sktlab.android.base.sample;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.sktlab.android.base.util.AppUtil;
import com.sktlab.android.base.util.SpUtil;

import java.io.Serializable;

public class AlarmUtil {
    public static void setAlarm(Context context, String alarmKey, long time, Serializable serializable) {
        Intent intent = new Intent();
        intent.setAction(AppUtil.getPackageName(context) + ".ALARM_ACTION");
        intent.setPackage(AppUtil.getPackageName(context));
        Bundle bundle = new Bundle();
        bundle.putSerializable("object", serializable);
        intent.putExtra("bundle", bundle);
        int alarmId = SpUtil.context(context).getInt("ALARM_ID", 0);
        SpUtil.context(context).save("ALARM_ID", ++alarmId);
        SpUtil.context(context).save(alarmKey, alarmId);

        PendingIntent sender = PendingIntent.getBroadcast(context, alarmId, intent, 0);
//        PendingIntent sender = PendingIntent.getService(context, alarmId, intent, 0);

        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, sender);
    }

    public static void cancelAlarm(Context context, String alarmKey) {
        int alarmId = SpUtil.context(context).getInt(alarmKey, -1);
        if (alarmId == -1) {
            return;
        }
        Intent intent = new Intent();
        intent.setAction(AppUtil.getPackageName(context) + ".ALARM_ACTION");
        PendingIntent sender = PendingIntent.getBroadcast(context, alarmId, intent, 0);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(sender);
    }
}
