package com.justplay1.shoppist.repository;

import com.justplay1.shoppist.models.NotificationModel;

import java.util.Collection;
import java.util.List;

import rx.Observable;

/**
 * Created by Mkhytar on 28.04.2016.
 */
public interface NotificationRepository {

    Observable<List<NotificationModel>> getItems();

    Observable<NotificationModel> getItem(final String id);

    Observable<Integer> getNewNotificationCount(long timestamp);

    Observable<List<NotificationModel>> getNewNotifications(long timestamp);

    void save(Collection<NotificationModel> data) throws Exception;

    void save(NotificationModel data) throws Exception;

    void delete(Collection<NotificationModel> data) throws Exception;

    void delete(NotificationModel data) throws Exception;

    void update(Collection<NotificationModel> data) throws Exception;

    void update(NotificationModel data) throws Exception;

    int clear();
}
