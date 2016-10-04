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

import com.justplay1.shoppist.entity.ListItemDAO;
import com.justplay1.shoppist.models.ListItemModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Mkhytar Mkhoian.
 */
@Singleton
public class ListItemsDAODataMapper {

    private final CategoryDAODataMapper categoryDAODataMapper;
    private final CurrencyDAODataMapper currencyDAODataMapper;
    private final UnitsDAODataMapper unitsDAODataMapper;

    @Inject
    public ListItemsDAODataMapper(CategoryDAODataMapper categoryDAODataMapper, CurrencyDAODataMapper currencyDAODataMapper, UnitsDAODataMapper unitsDAODataMapper) {
        this.categoryDAODataMapper = categoryDAODataMapper;
        this.currencyDAODataMapper = currencyDAODataMapper;
        this.unitsDAODataMapper = unitsDAODataMapper;
    }

    public ListItemModel transformFromDAO(ListItemDAO itemDAO) {
        ListItemModel item = null;
        if (itemDAO != null) {
            item = new ListItemModel();
            item.setId(itemDAO.getId());
            item.setName(itemDAO.getName());
            item.setNote(itemDAO.getNote());
            item.setParentListId(itemDAO.getParentListId());
            item.setPrice(itemDAO.getPrice());
            item.setPriority(itemDAO.getPriority());
            item.setStatus(itemDAO.getStatus());
            item.setCategory(categoryDAODataMapper.transformFromDAO(itemDAO.getCategory()));
            item.setCurrency(currencyDAODataMapper.transformFromDAO(itemDAO.getCurrency()));
            item.setUnit(unitsDAODataMapper.transformFromDAO(itemDAO.getUnit()));
            item.setQuantity(itemDAO.getQuantity());
            item.setTimeCreated(itemDAO.getTimeCreated());
        }
        return item;
    }

    public List<ListItemModel> transformFromDAO(Collection<ListItemDAO> unitEntities) {
        List<ListItemModel> items = new ArrayList<>();
        ListItemModel item;
        for (ListItemDAO itemEntity : unitEntities) {
            item = transformFromDAO(itemEntity);
            if (item != null) {
                items.add(item);
            }
        }
        return items;
    }

    public ListItemDAO transformToDAO(ListItemModel listItem) {
        ListItemDAO item = null;
        if (listItem != null) {
            item = new ListItemDAO(listItem.getId(),
                    listItem.getName(),
                    listItem.getParentListId(),
                    listItem.getNote(),
                    listItem.getStatus(),
                    categoryDAODataMapper.transformToDAO(listItem.getCategory()),
                    listItem.getPriority(),
                    listItem.getPrice(),
                    listItem.getQuantity(),
                    unitsDAODataMapper.transformToDAO(listItem.getUnit()),
                    listItem.getTimeCreated(),
                    currencyDAODataMapper.transformToDAO(listItem.getCurrency()));
        }
        return item;
    }

    public List<ListItemDAO> transformToDAO(Collection<ListItemModel> unitEntities) {
        List<ListItemDAO> items = new ArrayList<>();
        ListItemDAO item;
        for (ListItemModel itemEntity : unitEntities) {
            item = transformToDAO(itemEntity);
            if (item != null) {
                items.add(item);
            }
        }
        return items;
    }
}
