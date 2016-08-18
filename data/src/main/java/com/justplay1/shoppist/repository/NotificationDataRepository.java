package com.justplay1.shoppist.repository;

import com.justplay1.shoppist.entity.mappers.NotificationDAODataMapper;
import com.justplay1.shoppist.models.NotificationModel;
import com.justplay1.shoppist.repository.datasource.local.LocalNotificationDataStore;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Created by Mkhytar on 28.04.2016.
 */
@Singleton
public class NotificationDataRepository implements NotificationRepository {

    private final LocalNotificationDataStore mDataStore;
    private final NotificationDAODataMapper mDataMapper;

    @Inject
    public NotificationDataRepository(LocalNotificationDataStore dataStore, NotificationDAODataMapper dataMapper) {
        this.mDataStore = dataStore;
        this.mDataMapper = dataMapper;
    }

    @Override
    public Observable<Integer> getNewNotificationCount(long timestamp) {
        return mDataStore.getNewNotificationCount(timestamp)
                .map(aLong -> Integer.valueOf(aLong + ""));
    }

    @Override
    public Observable<List<NotificationModel>> getNewNotifications(long timestamp) {
        return mDataStore.getItems(timestamp)
                .map(mDataMapper::transformFromDAO);
    }

    @Override
    public Observable<List<NotificationModel>> getItems() {
        return mDataStore.getItems()
                .map(mDataMapper::transformFromDAO);
    }

    @Override
    public Observable<NotificationModel> getItem(String id) {
        return mDataStore.getItem(id)
                .map(mDataMapper::transformFromDAO);
    }

    @Override
    public void save(Collection<NotificationModel> data) throws Exception {
        mDataStore.save(mDataMapper.transformToDAO(data));
    }

    @Override
    public void save(NotificationModel data) throws Exception {
        mDataStore.save(mDataMapper.transformToDAO(data));
    }

    @Override
    public void delete(Collection<NotificationModel> data) throws Exception {
        mDataStore.delete(mDataMapper.transformToDAO(data));
    }

    @Override
    public void delete(NotificationModel data) throws Exception {
        mDataStore.delete(mDataMapper.transformToDAO(data));
    }

    @Override
    public void update(Collection<NotificationModel> data) throws Exception {
        mDataStore.update(mDataMapper.transformToDAO(data));
    }

    @Override
    public void update(NotificationModel data) throws Exception {
        mDataStore.update(mDataMapper.transformToDAO(data));
    }

    @Override
    public int clear() {
        return mDataStore.clear();
    }
}
