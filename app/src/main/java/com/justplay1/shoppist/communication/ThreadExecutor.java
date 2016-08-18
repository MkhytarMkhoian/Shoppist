package com.justplay1.shoppist.communication;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Misha on 06.09.2014.
 */
public final class ThreadExecutor {

    public static final int DEFAULT_NETWORK_THREADS = 5;
    public static final int DEFAULT_SCHEDULED_THREADS = 3;

    private static ExecutorService mThreadPoolExecutor;
    private static ExecutorService mNetworkExecutor;
    private static ScheduledExecutorService mScheduledExecutorService;

    public static void initialize(int networkThreads, int scheduledThreads) {
        mThreadPoolExecutor = Executors.newCachedThreadPool();
        mNetworkExecutor = Executors.newFixedThreadPool(networkThreads);
        mScheduledExecutorService = Executors.newScheduledThreadPool(scheduledThreads);
    }

    public static void initialize() {
        initialize(DEFAULT_NETWORK_THREADS, DEFAULT_SCHEDULED_THREADS);
    }

    public static <R> void notifyStartListener(final ExecutorListener<R> listener) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.start();
            }
        });
    }

    public static <R> void notifyErrorListener(final ExecutorListener<R> listener, final Exception e) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.error(e);
            }
        });
    }

    public static <R> void notifyCompleteListener(final ExecutorListener<R> listener, final R data) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.complete(data);
            }
        });
    }

    public static ScheduledFuture<?> doTaskWithInterval(Runnable command,
                                                        long initialDelay,
                                                        long period,
                                                        TimeUnit unit) {
        if (command != null) {
            return mScheduledExecutorService.scheduleAtFixedRate(command, initialDelay, period, unit);
        }
        return null;
    }

    public static void shutdownTaskWithInterval() {
        if (!mScheduledExecutorService.isShutdown()) {
            mScheduledExecutorService.shutdown();
        }
    }

    public static <V> Future<V> doNetworkTaskAsync(final Callable<V> task) {
        return mNetworkExecutor.submit(task);
    }

    public static <V> Future<?> doNetworkTaskAsync(final Callable<V> task, final ExecutorListener<V> listener) {
        Runnable runnable = wrapToListener(task, listener);
        if (runnable != null) {
            return mNetworkExecutor.submit(runnable);
        }
        return null;
    }

    public static <V> Future<?> doBackgroundTaskAsync(final Callable<V> task, final ExecutorListener<V> listener) {
        Runnable runnable = wrapToListener(task, listener);
        if (runnable != null) {
            return mThreadPoolExecutor.submit(runnable);
        }
        return null;
    }

    private static <V> Runnable wrapToListener(final Callable<V> task, final ExecutorListener<V> listener) {
        if (task != null && listener != null) {
            return new Runnable() {
                @Override
                public void run() {
                    try {
                        notifyStartListener(listener);
                        V result = task.call();
                        notifyCompleteListener(listener, result);
                    } catch (Exception e) {
                        notifyErrorListener(listener, e);
                    }
                }
            };
        }
        return null;
    }
}
