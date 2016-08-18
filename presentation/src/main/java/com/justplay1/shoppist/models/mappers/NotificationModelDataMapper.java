package com.justplay1.shoppist.models.mappers;

import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.models.NotificationModel;
import com.justplay1.shoppist.models.NotificationViewModel;
import com.justplay1.shoppist.models.NotificationStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 28.04.2016.
 */
@PerActivity
public class NotificationModelDataMapper {

    @Inject
    public NotificationModelDataMapper() {
    }

    @SuppressWarnings("ResourceType")
    public NotificationViewModel transformToViewModel(NotificationModel notification) {
        NotificationViewModel notificationViewModel = null;
        if (notification != null) {
            notificationViewModel = new NotificationViewModel();
            notificationViewModel.setId(notification.getId());
            notificationViewModel.setItemNames(notification.getItemNames());
            notificationViewModel.setStatus(NotificationStatus.values()[notification.getStatus()]);
            notificationViewModel.setTime(notification.getTime());
            notificationViewModel.setType(notification.getType());
        }
        return notificationViewModel;
    }

    public List<NotificationViewModel> transformToViewModel(Collection<NotificationModel> notifications) {
        List<NotificationViewModel> notificationViewModels = new ArrayList<>();
        NotificationViewModel notificationViewModel;
        for (NotificationModel notification : notifications) {
            notificationViewModel = transformToViewModel(notification);
            if (notificationViewModel != null) {
                notificationViewModels.add(notificationViewModel);
            }
        }
        return notificationViewModels;
    }

    public NotificationModel transform(NotificationViewModel notificationViewModel) {
        NotificationModel notification = null;
        if (notificationViewModel != null) {
            notification = new NotificationModel();
            notification.setId(notificationViewModel.getId());
            notification.setItemNames(notificationViewModel.getItemNames());
            notification.setStatus(notificationViewModel.getStatus().ordinal());
            notification.setTime(notificationViewModel.getTime());
            notification.setType(notificationViewModel.getType());
        }
        return notification;
    }

    public List<NotificationModel> transform(Collection<NotificationViewModel> notificationViewModels) {
        List<NotificationModel> items = new ArrayList<>();
        NotificationModel item;
        for (NotificationViewModel notificationViewModel : notificationViewModels) {
            item = transform(notificationViewModel);
            if (item != null) {
                items.add(item);
            }
        }
        return items;
    }
}
