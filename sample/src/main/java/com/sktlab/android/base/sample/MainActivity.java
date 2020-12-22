package com.sktlab.android.base.sample;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.sktlab.android.base.sample.databinding.ActivityMainBinding;
import com.sktlab.android.base.ui.BaseActivity;
import com.sktlab.android.base.util.AppUtil;
import com.sktlab.android.base.util.NotificationUtil;
import com.sktlab.android.base.util.RandomUtil;

import java.util.concurrent.TimeUnit;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlarmUtil.setAlarm(this, "test", System.currentTimeMillis() + 10000, "test");

                Intent intent = new Intent();
        String packageName = getPackageName();
        PowerManager pm = (PowerManager)
                getSystemService(Context.POWER_SERVICE);
//        if (pm.isIgnoringBatteryOptimizations(packageName))
//            intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
//        else {
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + packageName));
//        }
        startActivity(intent);

//        WorkRequest workRequest = new OneTimeWorkRequest.Builder(TestWorker.class)
//                .setInitialDelay(5, TimeUnit.SECONDS).build();
//        WorkManager.getInstance(this).enqueue(workRequest);

    }

    @Override
    protected ActivityMainBinding getBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setListeners() {

    }

    public static class TestWorker extends Worker{

        public TestWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
            super(context, workerParams);
        }

        @Override
        public void onStopped() {
            super.onStopped();
        }

        @NonNull
        @Override
        public Result doWork() {
            String id = RandomUtil.randomString(5);
            NotificationUtil.createNotificationChannel(getApplicationContext(), id, R.string.app_name, R.string.success, NotificationUtil.IMPORTANCE_HIGH);
            Notification.Builder builder = new Notification.Builder(getApplicationContext(), id)
                    .setContentTitle("text")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true);
            NotificationUtil.setNotification(getApplicationContext(), 1, builder.build());
            return Result.success();
        }
    }
}