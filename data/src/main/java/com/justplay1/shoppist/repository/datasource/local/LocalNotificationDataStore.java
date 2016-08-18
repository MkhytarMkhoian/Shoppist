package com.justplay1.shoppist.repository.datasource.local;

import com.justplay1.shoppist.entity.NotificationDAO;

import java.util.Collection;
import java.util.List;

import rx.Observable;

/**
 * Created by Mkhytar on 14.05.2016.
 */
public interface LocalNotificationDataStore extends LocalSetData<NotificationDAO> {

    Observable<Long> getNewNotificationCount(long time);

    Observable<List<NotificationDAO>> getItems();

    Observable<List<NotificationDAO>> getItems(long timestamp);

    Observable<List<NotificationDAO>> getItems(List<String> ids);

    Observable<NotificationDAO> getItem(final String id);
}
