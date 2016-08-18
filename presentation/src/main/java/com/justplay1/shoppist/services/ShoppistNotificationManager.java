package com.justplay1.shoppist.services;

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
import com.justplay1.shoppist.models.NotificationType;
import com.justplay1.shoppist.models.NotificationViewModel;
import com.justplay1.shoppist.preferences.ShoppistPreferences;
import com.justplay1.shoppist.view.activities.CategoriesActivity;
import com.justplay1.shoppist.view.activities.CurrencyActivity;
import com.justplay1.shoppist.view.activities.GoodsActivity;
import com.justplay1.shoppist.view.activities.MainActivity;
import com.justplay1.shoppist.view.activities.NotificationActivity;
import com.justplay1.shoppist.view.activities.UnitsActivity;

import java.util.List;

/**
 * Created by Mkhytar on 13.11.2015.
 */
public class ShoppistNotificationManager {

    private static final String NOTIFICATION_SHOPPIST_GROUP_ID = "Shoppist";

    private NotificationManager mNotificationManager;
    private Context mContext;
    private ShoppistPreferences mPreferences;

    public ShoppistNotificationManager(Context context, ShoppistPreferences preferences) {
        mContext = context;
        mPreferences = preferences;
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void sendSyncDataNotification(List<NotificationViewModel> notifications) {
        if (!mPreferences.isNotificationEnable()) return;
        if (notifications == null) return;

        for (NotificationViewModel notification : notifications) {
            PendingIntent contentIntent;
            Intent resultIntent = null;
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
            switch (notification.getType()) {
                case NotificationType.ALARM:
                    break;
                case NotificationType.SYNC_CATEGORIES_DATA:
                    resultIntent = new Intent(mContext, CategoriesActivity.class);
                    stackBuilder.addParentStack(CategoriesActivity.class);
                    break;
                case NotificationType.SYNC_CURRENCIES_DATA:
                    resultIntent = new Intent(mContext, CurrencyActivity.class);
                    stackBuilder.addParentStack(CurrencyActivity.class);
                    break;
                case NotificationType.SYNC_GOODS_DATA:
                    resultIntent = new Intent(mContext, GoodsActivity.class);
                    stackBuilder.addParentStack(GoodsActivity.class);
                    break;
                case NotificationType.SYNC_SHOPPING_LIST_DATA:
                    resultIntent = new Intent(mContext, MainActivity.class);
                    stackBuilder.addParentStack(MainActivity.class);
                    break;
                case NotificationType.SYNC_SHOPPING_LIST_ITEMS_DATA:
                    resultIntent = new Intent(mContext, MainActivity.class);
                    stackBuilder.addParentStack(MainActivity.class);
                    break;
                case NotificationType.SYNC_UNITS_DATA:
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

            if (mPreferences.isNotificationVibration()) {
                builder.setVibrate(new long[]{500, 500, 500});
            }
            if (mPreferences.isNotificationSound()) {
                builder.setDefaults(android.app.Notification.DEFAULT_SOUND);
            }
            builder.setContentIntent(contentIntent);
            mNotificationManager.notify(notification.getType() + notification.getStatus().ordinal(), builder.build());
        }
    }

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
}
