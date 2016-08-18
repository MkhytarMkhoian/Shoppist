package com.justplay1.shoppist.communication.alarm;

import android.app.IntentService;
import android.content.Intent;

/**
 * This {@code IntentService} does the app's actual work.
 * {@code ShoppistAlarmReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class ShoppistSchedulingService extends IntentService {

    public static final String TAG = ShoppistSchedulingService.class.getName();

    public ShoppistSchedulingService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ShoppistNotificationManager manager = new ShoppistNotificationManager(getApplicationContext());
   //     manager.sendAlarmForTaskNotification((TodoListItem) intent.getParcelableExtra(ShoppistAlarmReceiver.TASK));

        // Release the wake lock provided by the BroadcastReceiver.
        ShoppistAlarmReceiver.completeWakefulIntent(intent);
    }
}
