package com.justplay1.shoppist.entity.mappers;

import com.justplay1.shoppist.entity.CurrencyDAO;
import com.justplay1.shoppist.models.CurrencyModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Mkhytar on 28.04.2016.
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
            list.setServerId(listEntity.getServerId());
            list.setDelete(listEntity.isDelete());
            list.setTimestamp(listEntity.getTimestamp());
            list.setDirty(listEntity.isDirty());
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
                    currency.getServerId(),
                    currency.getName(),
                    currency.getTimestamp(),
                    currency.isDirty(),
                    currency.isDelete());
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
