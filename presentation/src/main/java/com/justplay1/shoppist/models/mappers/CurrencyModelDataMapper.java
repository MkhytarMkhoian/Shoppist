package com.justplay1.shoppist.models.mappers;

import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.models.CurrencyModel;
import com.justplay1.shoppist.models.CurrencyViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 28.04.2016.
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
            currencyModel.setServerId(currency.getServerId());
            currencyModel.setDelete(currency.isDelete());
            currencyModel.setTimestamp(currency.getTimestamp());
            currencyModel.setDirty(currency.isDirty());
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
            currency.setServerId(currencyModel.getServerId());
            currency.setDelete(currencyModel.isDelete());
            currency.setTimestamp(currencyModel.getTimestamp());
            currency.setDirty(currencyModel.isDirty());
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
