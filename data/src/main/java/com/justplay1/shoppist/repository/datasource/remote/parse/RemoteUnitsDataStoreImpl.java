package com.justplay1.shoppist.repository.datasource.remote.parse;

import com.justplay1.shoppist.di.scopes.PerSync;
import com.justplay1.shoppist.entity.UnitDAO;
import com.justplay1.shoppist.net.ServerConstants;
import com.justplay1.shoppist.repository.datasource.remote.RemoteUnitsDataStore;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 28.04.2016.
 */
@PerSync
public class RemoteUnitsDataStoreImpl extends BaseRemoteDataStore<UnitDAO> implements RemoteUnitsDataStore {

    @Inject
    public RemoteUnitsDataStoreImpl() {

    }

    @Override
    protected ParseObject fillObject(ParseObject objectToFill, UnitDAO data) {
        objectToFill.put(UnitDAO.UNIT_ID, data.getId());
        objectToFill.put(UnitDAO.SHORT_NAME, data.getShortName());
        objectToFill.put(UnitDAO.FULL_NAME, data.getName());
        objectToFill.put(UnitDAO.IS_DELETED, data.isDelete());
        return objectToFill;
    }

    public UnitDAO transformToDAO(ParseObject object) {
        return new UnitDAO(object.getString(UnitDAO.UNIT_ID),
                object.getObjectId(),
                object.getString(UnitDAO.FULL_NAME),
                object.getLong(ServerConstants.TIMESTAMP),
                false,
                object.getBoolean(UnitDAO.IS_DELETED),
                object.getString(UnitDAO.SHORT_NAME));
    }

    @Override
    public Collection<UnitDAO> getItems() throws Exception {
        return getItems(0);
    }

    @Override
    public Collection<UnitDAO> getItems(long timestamp) throws Exception {
        List<ParseObject> parseObjects = getAllItemsFromUser(ServerConstants.UNITS, timestamp);
        Collection<UnitDAO> result = new ArrayList<>();
        for (ParseObject object : parseObjects) {
            UnitDAO listDBO = transformToDAO(object);
            result.add(listDBO);
        }
        return result;
    }

    @Override
    public Collection<UnitDAO> save(Collection<UnitDAO> data) throws Exception {
        List<ParseObject> parseObjects = addItems(data, ServerConstants.UNITS, ServerConstants.UNIT);
        data.clear();
        for (ParseObject item : parseObjects) {
            UnitDAO unit = transformToDAO(item);
            data.add(unit);
        }
        return data;
    }

    @Override
    public void delete(Collection<UnitDAO> data) throws Exception {
        deleteItems(data, ServerConstants.UNITS);
    }

    @Override
    public void update(Collection<UnitDAO> data) throws Exception {
        updateItems(data, ServerConstants.UNITS, ServerConstants.UNIT);
    }

    @Override
    public int clear() {
        return 0;
    }
}
