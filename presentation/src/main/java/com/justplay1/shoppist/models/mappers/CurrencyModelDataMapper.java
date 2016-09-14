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

package com.justplay1.shoppist.models.mappers;

import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.models.CurrencyModel;
import com.justplay1.shoppist.models.CurrencyViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar Mkhoian.
 */
@PerActivity
public class CurrencyModelDataMapper {

    @Inject
    public CurrencyModelDataMapper() {
    }

    public CurrencyViewModel transformToViewModel(CurrencyModel currency) {
        CurrencyViewModel currencyModel = null;
        if (currency != null) {
            currencyModel = new CurrencyViewModel();
            currencyModel.setId(currency.getId());
            currencyModel.setName(currency.getName());
        }
        return currencyModel;
    }

    public List<CurrencyViewModel> transformToViewModel(Collection<CurrencyModel> currencies) {
        List<CurrencyViewModel> currencyModels = new ArrayList<>();
        CurrencyViewModel currency;
        for (CurrencyModel entity : currencies) {
            currency = transformToViewModel(entity);
            if (currency != null) {
                currencyModels.add(currency);
            }
        }
        return currencyModels;
    }

    public CurrencyModel transform(CurrencyViewModel currencyModel) {
        CurrencyModel currency = null;
        if (currencyModel != null) {
            currency = new CurrencyModel();
            currency.setId(currencyModel.getId());
            currency.setName(currencyModel.getName());
        }
        return currency;
    }

    public List<CurrencyModel> transform(Collection<CurrencyViewModel> currencyModels) {
        List<CurrencyModel> currencies = new ArrayList<>();
        CurrencyModel entity;
        for (CurrencyViewModel currency : currencyModels) {
            entity = transform(currency);
            if (entity != null) {
                currencies.add(entity);
            }
        }
        return currencies;
    }
}
