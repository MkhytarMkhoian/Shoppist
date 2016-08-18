package com.justplay1.shoppist.repository.datasource.remote.parse;

import com.justplay1.shoppist.di.scopes.PerSync;
import com.justplay1.shoppist.entity.CurrencyDAO;
import com.justplay1.shoppist.net.ServerConstants;
import com.justplay1.shoppist.repository.datasource.remote.RemoteCurrencyDataStore;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 28.04.2016.
 */
@PerSync
public class RemoteCurrencyDataStoreImpl extends BaseRemoteDataStore<CurrencyDAO> implements RemoteCurrencyDataStore {

    @Inject
    public RemoteCurrencyDataStoreImpl() {

    }

    @Override
    protected ParseObject fillObject(ParseObject objectToFill, CurrencyDAO data) {
        objectToFill.put(CurrencyDAO.NAME, data.getName());
        objectToFill.put(CurrencyDAO.CURRENCY_ID, data.getId());
        objectToFill.put(CurrencyDAO.IS_DELETED, data.isDelete());
        return objectToFill;
    }

    public CurrencyDAO transformToDAO(ParseObject object) {
        return new CurrencyDAO(object.getString(CurrencyDAO.CURRENCY_ID),
                object.getObjectId(),
                object.getString(CurrencyDAO.NAME),
                object.getLong(ServerConstants.TIMESTAMP),
                false,
                object.getBoolean(CurrencyDAO.IS_DELETED));
    }

    @Override
    public Collection<CurrencyDAO> getItems() throws Exception {
        return getItems(0);
    }

    @Override
    public Collection<CurrencyDAO> getItems(long timestamp) throws Exception {
        List<ParseObject> parseObjects = getAllItemsFromUser(ServerConstants.CURRENCIES, timestamp);
        Collection<CurrencyDAO> result = new ArrayList<>();
        for (ParseObject object : parseObjects) {
            CurrencyDAO categoryDBO = transformToDAO(object);
            result.add(categoryDBO);
        }
        return result;
    }

    @Override
    public Collection<CurrencyDAO> save(Collection<CurrencyDAO> data) throws Exception {
        List<ParseObject> list = addItems(data, ServerConstants.CURRENCIES, ServerConstants.CURRENCY);
        data.clear();
        for (ParseObject item : list) {
            CurrencyDAO currency = transformToDAO(item);
            data.add(currency);
        }
        return data;
    }

    @Override
    public void delete(Collection<CurrencyDAO> data) throws Exception {
        deleteItems(data, ServerConstants.CURRENCIES);
    }

    @Override
    public void update(Collection<CurrencyDAO> data) throws Exception {
        updateItems(data, ServerConstants.CURRENCIES, ServerConstants.CURRENCY);
    }

    @Override
    public int clear() {
        return 0;
    }
}
