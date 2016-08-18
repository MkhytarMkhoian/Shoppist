package com.justplay1.shoppist.communication.alarm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.Notification;
import com.justplay1.shoppist.models.NotificationTypeConstants;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.ui.activities.CategoriesActivity;
import com.justplay1.shoppist.ui.activities.CurrencyActivity;
import com.justplay1.shoppist.ui.activities.GoodsActivity;
import com.justplay1.shoppist.ui.activities.NotificationActivity;
import com.justplay1.shoppist.ui.activities.ShoppingListActivity;
import com.justplay1.shoppist.ui.activities.ShoppingListItemActivity;
import com.justplay1.shoppist.ui.activities.UnitsActivity;

import java.util.List;

/**
 * Created by Mkhytar on 13.11.2015.
 */
public class ShoppistNotificationManager {

    public static final String NOTIFICATION_SHOPPIST_GROUP_ID = "Shoppist";
    public static final String NOTIFICATION_SNOOZE_BUTTON_ACTION = "com.justplay1.shoppist.notification_snooze_button_action";
    public static final String NOTIFICATION_DONE_BUTTON_ACTION = "com.justplay1.shoppist.notification_done_button_action";
    public static final String NOTIFICATION_CLOSE_BUTTON_ACTION = "com.justplay1.shoppist.notification_close_button_action";

    protected NotificationManager mNotificationManager;
    protected Context mContext;

    public ShoppistNotificationManager(Context context) {
        this.mContext = context;
        mNotificationManager = (NotificationManager)
                mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void sendSyncDataNotification(List<Notification> notifications) {
        ShoppistPreferences.initPreferences(mContext);
        if (!ShoppistPreferences.isNotificationEnable()) return;
        if (notifications == null) return;

        for (Notification notification : notifications) {
            PendingIntent contentIntent;
            Intent resultIntent = null;
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
            switch (notification.getType()) {
                case NotificationTypeConstants.NOTIFICATION_ALARM_ID:
                    break;
                case NotificationTypeConstants.NOTIFICATION_SYNC_CATEGORIES_DATA_ID:
                    resultIntent = new Intent(mContext, CategoriesActivity.class);
                    stackBuilder.addParentStack(CategoriesActivity.class);
                    break;
                case NotificationTypeConstants.NOTIFICATION_SYNC_CURRENCIES_DATA_ID:
                    resultIntent = new Intent(mContext, CurrencyActivity.class);
                    stackBuilder.addParentStack(CurrencyActivity.class);
                    break;
                case NotificationTypeConstants.NOTIFICATION_SYNC_GOODS_DATA_ID:
                    resultIntent = new Intent(mContext, GoodsActivity.class);
                    stackBuilder.addParentStack(GoodsActivity.class);
                    break;
                case NotificationTypeConstants.NOTIFICATION_SYNC_SHOPPING_LIST_DATA_ID:
                    resultIntent = new Intent(mContext, ShoppingListActivity.class);
                    stackBuilder.addParentStack(ShoppingListActivity.class);
                    break;
                case NotificationTypeConstants.NOTIFICATION_SYNC_SHOPPING_LIST_ITEMS_DATA_ID:
                    resultIntent = new Intent(mContext, ShoppingListActivity.class);
                    stackBuilder.addParentStack(ShoppingListActivity.class);
                    break;
                case NotificationTypeConstants.NOTIFICATION_SYNC_UNITS_DATA_ID:
                    resultIntent = new Intent(mContext, UnitsActivity.class);
                    stackBuilder.addParentStack(UnitsActivity.class);
                    break;
                default:
                    resultIntent = new Intent(mContext, NotificationActivity.class);
                    stackBuilder.addParentStack(NotificationActivity.class);
                    break;
            }
            stackBuilder.addNextIntent(resultIntent);
            contentIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            Resources res = mContext.getResources();

            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(mContext)
                            .setSmallIcon(getSmallIcon())
                            .setLargeIcon(getLargeIcon(notification.getStatus().mColorRes, notification.getStatus().mImageRes))
                            .setContentTitle(notification.getTitle())
                            .setAutoCancel(true)
                            .setGroup(NOTIFICATION_SHOPPIST_GROUP_ID)
                            .setTicker(notification.getTitle())
                            .setColor(res.getColor(R.color.red_color));

            if (notification.getItemNames().size() > 1) {
                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                for (String item : notification.getItemNames()) {
                    inboxStyle.addLine(item);
                }
                builder.setStyle(inboxStyle);
            } else {
                builder.setContentText(notification.getItemNames().get(0));
            }

            if (ShoppistPreferences.isNotificationVibration()) {
                builder.setVibrate(new long[]{500, 500, 500});
            }
            if (ShoppistPreferences.isNotificationSound()) {
                builder.setDefaults(android.app.Notification.DEFAULT_SOUND);
            }
            builder.setContentIntent(contentIntent);
            mNotificationManager.notify(notification.getType() + notification.getStatus().ordinal(), builder.build());
        }
    }

//    public void sendAlarmForTaskNotification(TodoListItem task) {
//        ShoppistPreferences.initPreferences(mContext);
//        if (!ShoppistPreferences.isNotificationEnable()) return;
//
//        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0,
//                new Intent(mContext, BaseShoppingListActivity.class), 0);
//
//        Intent btnPostponeIntent = new Intent(mContext, HandleNotificationButtonClick.class);
//        btnPostponeIntent.setAction(NOTIFICATION_SNOOZE_BUTTON_ACTION);
//        btnPostponeIntent.putExtra(ShoppistAlarmReceiver.TASK, task);
//        PendingIntent btnPostponeClickPIntent = PendingIntent.getBroadcast(mContext, 0, btnPostponeIntent, 0);
//
//        Intent btnDoneIntent = new Intent(mContext, HandleNotificationButtonClick.class);
//        btnDoneIntent.setAction(NOTIFICATION_DONE_BUTTON_ACTION);
//        btnPostponeIntent.putExtra(ShoppistAlarmReceiver.TASK, task);
//        PendingIntent btnDoneClickPIntent = PendingIntent.getBroadcast(mContext, 0, btnDoneIntent, 0);
//
//        Intent btnCloseIntent = new Intent(mContext, HandleNotificationButtonClick.class);
//        btnCloseIntent.setAction(NOTIFICATION_CLOSE_BUTTON_ACTION);
//        btnPostponeIntent.putExtra(ShoppistAlarmReceiver.TASK, task);
//        PendingIntent btnCloseClickPIntent = PendingIntent.getBroadcast(mContext, 0, btnCloseIntent, 0);
//
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(mContext)
//                        .setSmallIcon(getSmallIcon())
//                        .setLargeIcon(getLargeIcon(R.color.red_color, R.drawable.ic_status_bar_todo_list))
//                        .setColor(mContext.getResources().getColor(R.color.red_color))
//                        .setContentTitle(task.getName())
//                        .setAutoCancel(true)
//                        .setGroup(NOTIFICATION_SHOPPIST_GROUP_ID)
//                        .setTicker(task.getName())
//                        .setCategory(NotificationCompat.CATEGORY_EVENT)
//                        .setPriority(NotificationCompat.PRIORITY_HIGH)
//                        .addAction(R.drawable.ic_done_black, mContext.getString(R.string.status_done), btnDoneClickPIntent)
//                        .addAction(R.drawable.ic_close, mContext.getString(R.string.close), btnCloseClickPIntent)
//                        .addAction(R.drawable.ic_action_schedule, mContext.getString(R.string.snooze), btnPostponeClickPIntent)
//                        .setStyle(new NotificationCompat.BigTextStyle()
//                                .bigText(task.getNote()));
//
//        if (ShoppistPreferences.isNotificationVibration()) {
//            mBuilder.setVibrate(new long[]{500, 500, 500});
//        }
//        if (ShoppistPreferences.isNotificationSound()) {
//            mBuilder.setDefaults(Notification.DEFAULT_SOUND);
//        }
//        mBuilder.setContentIntent(contentIntent);
//        mNotificationManager.notify(NotificationTypeConstants.NOTIFICATION_ALARM_ID, mBuilder.build());
//    }

    protected int getSmallIcon() {
        int smallIconRes = R.drawable.ic_stat_launcher;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            smallIconRes = R.drawable.ic_status_bar_launcher;
        }
        return smallIconRes;
    }

    protected Bitmap getLargeIcon(@ColorRes int backgroundColorRes, @DrawableRes int imageRes) {
        Resources res = mContext.getResources();
        int width = res.getDimensionPixelSize(R.dimen.notification_icon_width);
        int height = res.getDimensionPixelSize(R.dimen.notification_icon_height);

        Drawable dr = res.getDrawable(imageRes);
        Bitmap bitmap = Bitmap.createScaledBitmap(((BitmapDrawable) dr).getBitmap(), width / 2, height / 2, true);

        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(b);
        Paint paint = new Paint();
        paint.setColor(res.getColor(backgroundColorRes));
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawColor(res.getColor(android.R.color.transparent));
            canvas.drawCircle(width / 2, height / 2, width / 2, paint);
        } else {
            canvas.drawColor(res.getColor(backgroundColorRes));
        }

        canvas.drawBitmap(bitmap, (width - bitmap.getWidth()) / 2, (height - bitmap.getHeight()) / 2, null);
        return b;
    }

//    public static class HandleNotificationButtonClick extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            TodoListItem task = intent.getParcelableExtra(ShoppistAlarmReceiver.TASK);
//            BaseTodoListItemCache table = new TodoListItemCache(context);
//
//            try {
//                if (intent.getAction().equals(NOTIFICATION_DONE_BUTTON_ACTION)) {
//                    ShoppistAlarmReceiver.cancelAlarm(context, Collections.singletonList(task));
//                    task.setStatus(Status.DONE);
//                    table.updateToDoListItem(task, task);
//
//                } else if (intent.getAction().equals(NOTIFICATION_SNOOZE_BUTTON_ACTION)) {
//                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//
//                    Intent snoozeIntent = new Intent(context, ShoppistAlarmReceiver.class);
//                    intent.setAction(task.getName() + "_" + task.getParentListId() + "_snooze");
//                    intent.putExtra(ShoppistAlarmReceiver.TASK, task);
//
//                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, snoozeIntent, 0);
//                    Calendar calendar = Calendar.getInstance();
//                    calendar.setTimeInMillis(System.currentTimeMillis() + 1 * 60 * 1000);
//                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//
//                } else if (intent.getAction().equals(NOTIFICATION_CLOSE_BUTTON_ACTION)) {
//                    ShoppistAlarmReceiver.cancelAlarm(context, Collections.singletonList(task));
//                    task.setDateTerm(new DateTerm());
//                    table.updateToDoListItem(task, task);
//                }
//            } catch (RemoteException | OperationApplicationException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
