package com.justplay1.shoppist.entity.mappers;

import com.justplay1.shoppist.entity.NotificationDAO;
import com.justplay1.shoppist.models.NotificationModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Mkhytar on 28.04.2016.
 */
@Singleton
public class NotificationDAODataMapper {

    @Inject
    public NotificationDAODataMapper() {
    }

    public NotificationModel transformFromDAO(NotificationDAO itemEntity) {
        NotificationModel item = null;
        if (itemEntity != null) {
            item = new NotificationModel();
            item.setId(itemEntity.getId());
            item.setItemNames(itemEntity.getItemNames());
            item.setStatus(itemEntity.getStatus());
            item.setTime(itemEntity.getTime());
            item.setType(itemEntity.getType());
        }
        return item;
    }

    public List<NotificationModel> transformFromDAO(Collection<NotificationDAO> entities) {
        List<NotificationModel> items = new ArrayList<>();
        NotificationModel item;
        for (NotificationDAO entity : entities) {
            item = transformFromDAO(entity);
            if (item != null) {
                items.add(item);
            }
        }
        return items;
    }

    public NotificationDAO transformToDAO(NotificationModel notification) {
        NotificationDAO item = null;
        if (notification != null) {
            item = new NotificationDAO(notification.getId(),
                    notification.getTitle(),
                    notification.getItemNames(),
                    notification.getTime(),
                    notification.getStatus(),
                    notification.getType());
        }
        return item;
    }

    public List<NotificationDAO> transformToDAO(Collection<NotificationModel> notifications) {
        List<NotificationDAO> items = new ArrayList<>();
        NotificationDAO item;
        for (NotificationModel notification : notifications) {
            item = transformToDAO(notification);
            if (item != null) {
                items.add(item);
            }
        }
        return items;
    }
}
