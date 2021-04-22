package com.sktlab.android.base.threadpool;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;

public class Executors {
    private static final String TAG = Executors.class.getSimpleName();
    // For Singleton instantiation
    private static final Object LOCK = new Object();
    // For disk
    private static Executor diskIO;
    // Working on main thread
    private static MainThreadExecutor mainThread;
    // For network request
    private static Executor networkIO;
    // For location request
    private static Executor locationIO;
    // For websocket only
    private static Executor websocketIO;
    // For scheduled task
    private static ScheduledExecutorService scheduledExecutorIO;

    private Executors() {
    }

    public static Executor disk() {
        if (diskIO == null) {
            synchronized (LOCK) {
                if (diskIO == null) {
                    diskIO = java.util.concurrent.Executors.newSingleThreadExecutor();
                }
            }
        }
        return diskIO;
    }

    public static MainThreadExecutor main() {
        if (mainThread == null) {
            synchronized (LOCK) {
                if (mainThread == null) {
                    mainThread = new MainThreadExecutor();
                }
            }
        }
        return mainThread;
    }

    public static Executor net() {
        if (networkIO == null) {
            synchronized (LOCK) {
                if (networkIO == null) {
                    networkIO = java.util.concurrent.Executors.newFixedThreadPool(5);
                }
            }
        }
        return networkIO;
    }

    // the nThreads will only effect at first time invoke.
    public static Executor net(int nThreads) {
        if (networkIO == null) {
            synchronized (LOCK) {
                if (networkIO == null) {
                    networkIO = java.util.concurrent.Executors.newFixedThreadPool(nThreads);
                }
            }
        }
        return networkIO;
    }

    public static Executor location() {
        if (locationIO == null) {
            synchronized (LOCK) {
                if (locationIO == null) {
                    locationIO = java.util.concurrent.Executors.newSingleThreadExecutor();
                }
            }
        }
        return locationIO;
    }

    public static Executor websocket() {
        if (websocketIO == null) {
            synchronized (LOCK) {
                if (websocketIO == null) {
                    websocketIO = java.util.concurrent.Executors.newSingleThreadExecutor();
                }
            }
        }
        return websocketIO;
    }

    public static ScheduledExecutorService scheduled() {
        if (scheduledExecutorIO == null) {
            synchronized (LOCK) {
                if (scheduledExecutorIO == null) {
                    scheduledExecutorIO = java.util.concurrent.Executors.newScheduledThreadPool(16);
                }
            }
        }
        return scheduledExecutorIO;
    }

    public static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }

        public void execute(@NonNull Runnable command, long delayMillis) {
            mainThreadHandler.postDelayed(command, delayMillis);
        }
    }

    public static void destroy() {
        diskIO = null;
        mainThread = null;
        websocketIO = null;
        if (scheduledExecutorIO != null) {
            scheduledExecutorIO.shutdownNow();
            scheduledExecutorIO = null;
        }
    }
}
