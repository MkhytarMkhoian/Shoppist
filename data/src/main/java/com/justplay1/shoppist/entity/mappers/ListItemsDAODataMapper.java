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

    private final CategoryDAODataMapper mCategoryDAODataMapper;
    private final CurrencyDAODataMapper mCurrencyDAODataMapper;
    private final UnitsDAODataMapper mUnitsDAODataMapper;

    @Inject
    public ListItemsDAODataMapper(CategoryDAODataMapper categoryDAODataMapper, CurrencyDAODataMapper currencyDAODataMapper, UnitsDAODataMapper unitsDAODataMapper) {
        this.mCategoryDAODataMapper = categoryDAODataMapper;
        this.mCurrencyDAODataMapper = currencyDAODataMapper;
        this.mUnitsDAODataMapper = unitsDAODataMapper;
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
            item.setCategory(mCategoryDAODataMapper.transformFromDAO(itemDAO.getCategory()));
            item.setCurrency(mCurrencyDAODataMapper.transformFromDAO(itemDAO.getCurrency()));
            item.setUnit(mUnitsDAODataMapper.transformFromDAO(itemDAO.getUnit()));
            item.setQuantity(itemDAO.getQuantity());
            item.setTimeCreated(itemDAO.getTimeCreated());
            item.setPosition(itemDAO.getPosition());
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
                    mCategoryDAODataMapper.transformToDAO(listItem.getCategory()),
                    listItem.getPriority(),
                    listItem.getPrice(),
                    listItem.getQuantity(),
                    mUnitsDAODataMapper.transformToDAO(listItem.getUnit()),
                    listItem.getTimeCreated(),
                    mCurrencyDAODataMapper.transformToDAO(listItem.getCurrency()),
                    listItem.getPosition());
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
