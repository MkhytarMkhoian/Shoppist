package com.justplay1.shoppist.entity.mappers;

import com.justplay1.shoppist.entity.ListDAO;
import com.justplay1.shoppist.models.ListModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Mkhytar on 29.04.2016.
 */
@Singleton
public class ListDAODataMapper {

    @Inject
    public ListDAODataMapper() {
    }

    public ListModel transformFromDAO(ListDAO listEntity) {
        ListModel list = null;
        if (listEntity != null) {
            list = new ListModel();
            list.setId(listEntity.getId());
            list.setName(listEntity.getName());
            list.setServerId(listEntity.getServerId());
            list.setDelete(listEntity.isDelete());
            list.setTimestamp(listEntity.getTimestamp());
            list.setDirty(listEntity.isDirty());
            list.setTimeCreated(listEntity.getTimeCreated());
            list.setPriority(listEntity.getPriority());
            list.setBoughtCount(listEntity.getBoughtCount());
            list.setColor(listEntity.getColor());
            list.setSize(listEntity.getSize());
            list.setPosition(listEntity.getPosition());
        }
        return list;
    }

    public List<ListModel> transformFromDAO(Collection<ListDAO> listEntities) {
        List<ListModel> lists = new ArrayList<>();
        ListModel list;
        for (ListDAO listEntity : listEntities) {
            list = transformFromDAO(listEntity);
            if (list != null) {
                lists.add(list);
            }
        }
        return lists;
    }

    public ListDAO transformToDAO(ListModel list) {
        ListDAO entity = null;
        if (list != null) {
            entity = new ListDAO(list.getId(),
                    list.getServerId(),
                    list.getName(),
                    list.getTimestamp(),
                    list.isDirty(),
                    list.isDelete(),
                    list.getBoughtCount(),
                    list.getTimeCreated(),
                    list.getPriority(),
                    list.getColor(),
                    list.getSize(),
                    list.getPosition());
        }
        return entity;
    }

    public List<ListDAO> transformToDAO(Collection<ListModel> listEntities) {
        List<ListDAO> lists = new ArrayList<>();
        ListDAO list;
        for (ListModel listEntity : listEntities) {
            list = transformToDAO(listEntity);
            if (list != null) {
                lists.add(list);
            }
        }
        return lists;
    }
}
