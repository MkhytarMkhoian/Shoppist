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

import com.justplay1.shoppist.entity.CurrencyDAO;
import com.justplay1.shoppist.models.CurrencyModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Mkhytar Mkhoian.
 */
@Singleton
public class CurrencyDAODataMapper {

    @Inject
    public CurrencyDAODataMapper() {
    }

    public CurrencyModel transformFromDAO(CurrencyDAO listEntity) {
        CurrencyModel list = null;
        if (listEntity != null) {
            list = new CurrencyModel();
            list.setId(listEntity.getId());
            list.setName(listEntity.getName());
        }
        return list;
    }

    public List<CurrencyModel> transformFromDAO(Collection<CurrencyDAO> entities) {
        List<CurrencyModel> currencies = new ArrayList<>();
        CurrencyModel currency;
        for (CurrencyDAO entity : entities) {
            currency = transformFromDAO(entity);
            if (currency != null) {
                currencies.add(currency);
            }
        }
        return currencies;
    }

    public CurrencyDAO transformToDAO(CurrencyModel currency) {
        CurrencyDAO list = null;
        if (currency != null) {
            list = new CurrencyDAO(currency.getId(),
                    currency.getName());
        }
        return list;
    }

    public List<CurrencyDAO> transformToDAO(Collection<CurrencyModel> currencies) {
        List<CurrencyDAO> entities = new ArrayList<>();
        CurrencyDAO entity;
        for (CurrencyModel currency : currencies) {
            entity = transformToDAO(currency);
            if (entity != null) {
                entities.add(entity);
            }
        }
        return entities;
    }
}
