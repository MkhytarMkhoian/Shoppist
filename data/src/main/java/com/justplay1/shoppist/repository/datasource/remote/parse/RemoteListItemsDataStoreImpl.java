package com.justplay1.shoppist.repository.datasource.remote.parse;

import com.justplay1.shoppist.di.scopes.PerSync;
import com.justplay1.shoppist.entity.CategoryDAO;
import com.justplay1.shoppist.entity.CurrencyDAO;
import com.justplay1.shoppist.entity.ListItemDAO;
import com.justplay1.shoppist.entity.UnitDAO;
import com.justplay1.shoppist.entity.mappers.ListItemsDAODataMapper;
import com.justplay1.shoppist.net.ServerConstants;
import com.justplay1.shoppist.net.parse.ServerRequests;
import com.justplay1.shoppist.repository.datasource.local.LocalCategoryDataStore;
import com.justplay1.shoppist.repository.datasource.local.LocalCurrencyDataStore;
import com.justplay1.shoppist.repository.datasource.local.LocalUnitsDataStore;
import com.justplay1.shoppist.repository.datasource.remote.RemoteListItemsDataStore;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 28.04.2016.
 */
@PerSync
public class RemoteListItemsDataStoreImpl extends BaseRemoteDataStore<ListItemDAO> implements RemoteListItemsDataStore {

    private final LocalCategoryDataStore mLocalCategoryDataStore;
    private final LocalUnitsDataStore mLocalUnitsDataStore;
    private final LocalCurrencyDataStore mLocalCurrencyDataStore;

    @Inject
    public RemoteListItemsDataStoreImpl(LocalCategoryDataStore localCategoryDataStore,
                                        LocalUnitsDataStore localUnitsDataStore, LocalCurrencyDataStore localCurrencyDataStore) {
        this.mLocalCategoryDataStore = localCategoryDataStore;
        this.mLocalUnitsDataStore = localUnitsDataStore;
        this.mLocalCurrencyDataStore = localCurrencyDataStore;
    }

    @Override
    protected ParseObject fillObject(ParseObject objectToFill, ListItemDAO item) {
        objectToFill.put(ListItemDAO.IS_DELETED, item.isDelete());
        objectToFill.put(ListItemDAO.LIST_ITEM_NAME, item.getName());
        objectToFill.put(ListItemDAO.LIST_ITEM_ID, item.getId());
        objectToFill.put(ListItemDAO.PARENT_LIST_ID, item.getParentListId());
        objectToFill.put(ListItemDAO.PRIORITY, item.getPriority());
        objectToFill.put(ListItemDAO.TIME_CREATED, item.getTimeCreated());
        objectToFill.put(ListItemDAO.PRICE, item.getPrice());
        objectToFill.put(ListItemDAO.SHORT_DESCRIPTION, item.getNote());
        objectToFill.put(ListItemDAO.STATUS, item.getStatus());
        objectToFill.put(ListItemDAO.QUANTITY, item.getQuantity());

        if (!item.isCategoryEmpty()) {
            objectToFill.put(ListItemDAO.CATEGORY_ID, item.getCategory().getId());
        } else {
            objectToFill.put(ListItemDAO.CATEGORY_ID, CategoryDAO.NO_CATEGORY_ID);
        }
        if (!item.isUnitEmpty()) {
            objectToFill.put(ListItemDAO.UNIT_ID, item.getUnit().getId());
        } else {
            objectToFill.put(ListItemDAO.UNIT_ID, UnitDAO.NO_UNIT_ID);
        }
        if (!item.isCurrencyEmpty()) {
            objectToFill.put(ListItemDAO.CURRENCY_ID, item.getCurrency().getId());
        } else {
            objectToFill.put(ListItemDAO.CURRENCY_ID, CurrencyDAO.NO_CURRENCY_ID);
        }
        return objectToFill;
    }

    public ListItemDAO transformToDAO(ParseObject item, CategoryDAO categoryEntity,
                                      UnitDAO unitEntity, CurrencyDAO currencyEntity) {
        return new ListItemDAO(item.getString(ListItemDAO.LIST_ITEM_ID),
                item.getObjectId(),
                item.getString(ListItemDAO.LIST_ITEM_NAME),
                item.getLong(ServerRequests.TIMESTAMP),
                false,
                item.getBoolean(ListItemDAO.IS_DELETED),
                item.getString(ListItemDAO.PARENT_LIST_ID),
                item.getString(ListItemDAO.SHORT_DESCRIPTION),
                item.getInt(ListItemDAO.STATUS) == 1,
                categoryEntity,
                item.getInt(ListItemDAO.PRIORITY),
                item.getDouble(ListItemDAO.PRICE),
                item.getDouble(ListItemDAO.QUANTITY),
                unitEntity,
                item.getLong(ListItemDAO.TIME_CREATED),
                currencyEntity,
                -1);
    }

    @Override
    public Collection<ListItemDAO> getItems() throws Exception {
        return getItems(0);
    }

    @Override
    public Collection<ListItemDAO> getItems(long timestamp) throws Exception {
        List<ParseObject> objects = getAllShoppingListItems(timestamp);
        List<ListItemDAO> result = new ArrayList<>();
//        Map<String, CategoryDAO> categoryMap = mLocalCategoryDataStore.getItems();
//        Map<String, UnitDAO> unitMap = mLocalUnitsDataStore.getItems();
//        Map<String, CurrencyDAO> currencyMap = mLocalCurrencyDataStore.getItems();
//
//        for (ParseObject object : objects) {
//            CategoryDAO category = categoryMap.get(object.getString(ListItemDAO.CATEGORY_ID));
//            UnitDAO unit = unitMap.get(object.getString(ListItemDAO.UNIT_ID));
//            CurrencyDAO currency = currencyMap.get(object.getString(ListItemDAO.CURRENCY_ID));
//            result.add(transformToDAO(object, category, unit, currency));
//        }
        return result;
    }

    @Override
    public Collection<ListItemDAO> save(Collection<ListItemDAO> data) throws Exception {
        List<ParseObject> object = addShoppingListItems(data);
        for (ParseObject item : object) {
            for (ListItemDAO list : data) {
                if (list.getId().equals(item.getString(ListItemDAO.LIST_ITEM_ID))) {
                    list.setServerId(item.getObjectId());
                    list.setTimestamp(item.getLong(ServerConstants.TIMESTAMP));
                    list.setDirty(false);
                }
            }
        }
        return data;
    }

    @Override
    public void delete(Collection<ListItemDAO> data) throws Exception {
        deleteShoppingListItems(data);
    }

    @Override
    public void update(Collection<ListItemDAO> data) throws Exception {
        updateItems(data, ServerConstants.SHOPPING_LIST_ITEMS_TABLE, ServerConstants.SHOPPING_LIST_ITEM);
    }

    @Override
    public int clear() {
        return 0;
    }

    private Collection<ListItemDAO> deleteShoppingListItems(Collection<ListItemDAO> items) throws ParseException {
        ParseUser user = ParseUser.getCurrentUser();
        if (user == null) return null;

        final long refreshTime = System.currentTimeMillis();
        ParseRelation<ParseObject> relation = user.getRelation(ServerConstants.SHOPPING_LIST_ITEMS_TABLE);

        List<ParseObject> toDelete = new ArrayList<>(items.size());
        for (ListItemDAO item : items) {
            ParseObject object = ParseObject.createWithoutData(ServerConstants.SHOPPING_LIST_ITEMS_TABLE, item.getServerId());
            toDelete.add(object);
            relation.remove(object);
        }
        ParseObject.deleteAll(toDelete);

        setRefreshTime(user, ServerConstants.SHOPPING_LISTS_TABLE, refreshTime);
        user.save();
        return items;
    }

    private List<ParseObject> addShoppingListItems(Collection<ListItemDAO> items) throws ParseException {
        List<ParseObject> toSave = new ArrayList<>(items.size());

        ParseUser user = ParseUser.getCurrentUser();
        if (user == null) return toSave;

        final long refreshTime = System.currentTimeMillis();

        for (ListItemDAO item : items) {
            if (item.getServerId() != null) continue;

            ParseObject object = new ParseObject(ServerConstants.SHOPPING_LIST_ITEMS_TABLE);
            fillObject(object, item);
            object.put(ServerConstants.TIMESTAMP, refreshTime);
            object.put(ServerConstants.CREATED_BY, user);
            toSave.add(object);
        }
        if (toSave.size() > 0) {
            ParseObject.saveAll(toSave);

            ParseRelation<ParseObject> relation = user.getRelation(ServerConstants.SHOPPING_LIST_ITEMS_TABLE);
            for (ParseObject object : toSave) {
                relation.add(object);
            }
            setRefreshTime(user, ServerConstants.SHOPPING_LIST_ITEMS_TABLE, refreshTime);
            user.save();
        }
        return toSave;
    }

    private List<ParseObject> getAllShoppingListItems(long updateTime) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ServerConstants.SHOPPING_LIST_ITEMS_TABLE);
        query.whereEqualTo(ServerConstants.CREATED_BY, ParseUser.getCurrentUser());
        query.whereGreaterThan(ServerConstants.TIMESTAMP, updateTime);
        return query.find();
    }
}
