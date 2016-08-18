package com.justplay1.shoppist.communication.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.util.Calendar;
import java.util.List;

public class ShoppistAlarmReceiver extends WakefulBroadcastReceiver {

    public static final String TASK = "task";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, ShoppistSchedulingService.class);
        service.putExtra(TASK, intent.getParcelableExtra(TASK));

        // Start the service, keeping the device awake while it is launching.
//        startWakefulService(context, service);
    }

//    public static void setAlarm(Context context, TodoListItem item) {
//        if (item == null) return;
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//
//        if (!item.getDateTerm().isDateEmpty()) {
//            calendar.set(Calendar.YEAR, item.getDateTerm().getYear());
//            calendar.set(Calendar.MONTH, item.getDateTerm().getMonth());
//            calendar.set(Calendar.DAY_OF_MONTH, item.getDateTerm().getDayOfMonth());
//
//            calendar.set(Calendar.HOUR_OF_DAY, item.getDateTerm().getHourOfDay());
//            calendar.set(Calendar.MINUTE, item.getDateTerm().getMinute());
//
//            if (calendar.getTimeInMillis() <= System.currentTimeMillis()) return;
//
//            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, buildIntent(context, item), 0);
//
//            switch (item.getReminder()) {
//                case 1:
//                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//                    break;
//                case 2:
//                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
//                            calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
//                    break;
//                case 3:
//                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
//                            calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
//                    break;
//                case 4:
//                    if (!item.getDateTerm().isDateEmpty()) break;
//                    Calendar calendar2 = Calendar.getInstance();
//                    calendar2.setTimeInMillis(System.currentTimeMillis());
//                    calendar2.set(Calendar.HOUR_OF_DAY, item.getDateTerm().getHourOfDay());
//                    calendar2.set(Calendar.MINUTE, item.getDateTerm().getMinute());
//
//                    for (int i = 2; i <= 6; i++) {
//                        calendar.set(Calendar.DAY_OF_WEEK, i);
//                    }
//                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
//                            calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
//                    break;
//                case 5:
//                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
//                            calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 30, pendingIntent);
//                    break;
//                case 6:
//                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
//                            calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 365, pendingIntent);
//                    break;
//            }
//        }
//    }
//
//    public static void setEnableBootReceiver(Context context) {
//        ComponentName receiver = new ComponentName(context, ShoppistBootReceiver.class);
//        PackageManager pm = context.getPackageManager();
//
//        pm.setComponentEnabledSetting(receiver,
//                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                PackageManager.DONT_KILL_APP);
//    }
//
//    public static void setDisabledBootReceiver(Context context) {
//        ComponentName receiver = new ComponentName(context, ShoppistBootReceiver.class);
//        PackageManager pm = context.getPackageManager();
//
//        pm.setComponentEnabledSetting(receiver,
//                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//                PackageManager.DONT_KILL_APP);
//    }
//
//    private static Intent buildIntent(Context context, TodoListItem item) {
//        if (item == null) return null;
//
//        Intent intent = new Intent(context, ShoppistAlarmReceiver.class);
//        intent.setAction(item.getName() + "_" + item.getParentListId());
//        intent.putExtra(TASK, item);
//        return intent;
//    }
//
//    public static void cancelAlarm(Context context, List<TodoListItem> items) {
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//
//        for (TodoListItem item :items){
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, buildIntent(context, item), 0);
//            alarmManager.cancel(pendingIntent);
//        }
//    }
}
