package com.justplay1.shoppist.repository.datasource.remote.parse;

import com.justplay1.shoppist.di.scopes.PerSync;
import com.justplay1.shoppist.entity.ListDAO;
import com.justplay1.shoppist.net.ServerConstants;
import com.justplay1.shoppist.repository.datasource.remote.RemoteListDataStore;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 28.04.2016.
 */
@PerSync
public class RemoteListDataStoreImpl extends BaseRemoteDataStore<ListDAO> implements RemoteListDataStore {

    @Inject
    public RemoteListDataStoreImpl() {
    }

    @Override
    protected ParseObject fillObject(ParseObject objectToFill, ListDAO data) {
        objectToFill.put(ListDAO.LIST_ID, data.getId());
        objectToFill.put(ListDAO.IS_DELETED, data.isDelete());
        objectToFill.put(ListDAO.LIST_NAME, data.getName());
        objectToFill.put(ListDAO.COLOR, data.getColor());
        objectToFill.put(ListDAO.PRIORITY, data.getPriority());
        objectToFill.put(ListDAO.TIME_CREATED, data.getTimeCreated());
        return objectToFill;
    }

    public ListDAO transformToDAO(ParseObject object) {
        return new ListDAO(object.getString(ListDAO.LIST_ID),
                object.getObjectId(),
                object.getString(ListDAO.LIST_NAME),
                object.getLong(ServerConstants.TIMESTAMP),
                false,
                object.getBoolean(ListDAO.IS_DELETED),
                object.getInt(ListDAO.BOUGHT_COUNT),
                object.getLong(ListDAO.TIME_CREATED),
                object.getInt(ListDAO.PRIORITY),
                object.getInt(ListDAO.COLOR),
                object.getInt(ListDAO.SIZE),
                object.getInt(ListDAO.MANUAL_SORT_POSITION));
    }

    @Override
    public Collection<ListDAO> getItems() throws Exception {
        return getItems(0);
    }

    @Override
    public Collection<ListDAO> getItems(long timestamp) throws Exception {
        List<ParseObject> parseObjects = getAllItemsFromUser(ServerConstants.SHOPPING_LISTS_TABLE, timestamp);
        Collection<ListDAO> result = new ArrayList<>();
        for (ParseObject object : parseObjects) {
            ListDAO listDBO = transformToDAO(object);
            result.add(listDBO);
        }
        return result;
    }

    @Override
    public Collection<ListDAO> save(Collection<ListDAO> data) throws Exception {
        List<ParseObject> list = addItems(data, ServerConstants.SHOPPING_LISTS_TABLE, ServerConstants.SHOPPING_LIST);
        data.clear();
        for (ParseObject item : list) {
            ListDAO entity = transformToDAO(item);
            data.add(entity);
        }
        return data;
    }

    @Override
    public void delete(Collection<ListDAO> data) throws Exception {
        deleteLists(data, ServerConstants.SHOPPING_LISTS_TABLE);
    }

    @Override
    public void update(Collection<ListDAO> data) throws Exception {
        updateItems(data, ServerConstants.SHOPPING_LISTS_TABLE, ServerConstants.SHOPPING_LIST);
    }

    @Override
    public int clear() {
        return 0;
    }
}
