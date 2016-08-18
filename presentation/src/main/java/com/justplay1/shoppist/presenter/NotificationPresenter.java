package com.justplay1.shoppist.presenter;

import android.os.Bundle;

import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.interactor.DefaultSubscriber;
import com.justplay1.shoppist.interactor.notification.ClearNotification;
import com.justplay1.shoppist.interactor.notification.DeleteNotifications;
import com.justplay1.shoppist.interactor.notification.GetNotifications;
import com.justplay1.shoppist.models.NotificationViewModel;
import com.justplay1.shoppist.models.mappers.NotificationModelDataMapper;
import com.justplay1.shoppist.preferences.ShoppistPreferences;
import com.justplay1.shoppist.presenter.base.BaseRxPresenter;
import com.justplay1.shoppist.view.NotificationsView;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseItemHolder;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar on 01.07.2016.
 */
@PerActivity
public class NotificationPresenter extends BaseRxPresenter<NotificationsView> {

    private final NotificationModelDataMapper mDataMapper;
    private final ShoppistPreferences mPreferences;
    private final GetNotifications mGetNotifications;
    private final ClearNotification mClearNotification;
    private final DeleteNotifications mDeleteNotifications;

    @Inject
    public NotificationPresenter(NotificationModelDataMapper dataMapper,
                                 ShoppistPreferences preferences,
                                 GetNotifications getNotifications,
                                 ClearNotification clearNotification,
                                 DeleteNotifications deleteNotifications) {
        this.mDataMapper = dataMapper;
        this.mPreferences = preferences;
        this.mGetNotifications = getNotifications;
        this.mClearNotification = clearNotification;
        this.mDeleteNotifications = deleteNotifications;
    }

    public void init() {
        loadData();
    }

    public void loadData() {
        mSubscriptions.add(mGetNotifications.get()
                .map(mDataMapper::transformToViewModel)
                .subscribe(new DefaultSubscriber<List<NotificationViewModel>>() {
                    @Override
                    public void onNext(List<NotificationViewModel> notifications) {
                        showData(notifications);
                    }
                }));
    }

    private void clearAll() {
        mSubscriptions.add(mClearNotification.get()
                .subscribe(new DefaultSubscriber<Integer>() {
                    @Override
                    public void onNext(Integer count) {

                    }
                }));
    }

    private void deleteItem(NotificationViewModel removeItem) {
        mSubscriptions.add(
                Observable.fromCallable(() -> {
                    return mDataMapper.transform(removeItem);
                }).flatMap(notificationModel -> {
                    mDeleteNotifications.setData(notificationModel);
                    return mDeleteNotifications.get();
                }).subscribe(new DefaultSubscriber<>()));
    }

    private void showData(List<NotificationViewModel> data) {
        if (isViewAttached()) {
            getView().showData(data);
        }
    }

    private void notifyDataSetChanged() {
        if (isViewAttached()) {
            getView().notifyDataSetChanged();
        }
    }

    public void onItemRemoved(NotificationViewModel removeItem, int position) {
        deleteItem(removeItem);
    }

    public void onClearAllClick() {

    }

    public void onMarkAsViewedClick() {
        mPreferences.setLastUserSeenNotificationsTime(System.currentTimeMillis());
        notifyDataSetChanged();
    }

    public void onItemClick(BaseItemHolder holder, int position, long id) {

    }
}
