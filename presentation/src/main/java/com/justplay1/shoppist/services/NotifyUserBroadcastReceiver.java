package com.justplay1.shoppist.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.justplay1.shoppist.UIThread;
import com.justplay1.shoppist.interactor.notification.GetNewNotifications;
import com.justplay1.shoppist.models.NotificationViewModel;

import java.util.Collection;

import rx.functions.Action1;

/**
 * Created by Mkhytar on 14.05.2016.
 */
public class NotifyUserBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("notify_user")) {

//            GetNewNotifications notifications = new GetNewNotifications(System.currentTimeMillis(), null, null, new UIThread());
//            notifications.get().subscribe(new Action1<Collection<NotificationViewModel>>() {
//                @Override
//                public void call(Collection<NotificationViewModel> notifications) {
//
//                }
//            });
        }
    }
}
