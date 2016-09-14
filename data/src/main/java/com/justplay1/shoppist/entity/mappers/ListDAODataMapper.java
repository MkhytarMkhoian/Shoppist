/*
 * Copyright (C) 2016 Mkhytar Mkhoian
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.justplay1.shoppist.entity.mappers;

import com.justplay1.shoppist.entity.ListDAO;
import com.justplay1.shoppist.models.ListModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Mkhytar Mkhoian.
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
                    list.getName(),
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
