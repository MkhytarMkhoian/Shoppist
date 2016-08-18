package com.justplay1.shoppist.repository.datasource.remote.parse;

import com.justplay1.shoppist.entity.BaseDAO;
import com.justplay1.shoppist.entity.CategoryDAO;
import com.justplay1.shoppist.entity.CurrencyDAO;
import com.justplay1.shoppist.entity.ListDAO;
import com.justplay1.shoppist.entity.UnitDAO;
import com.justplay1.shoppist.net.ServerConstants;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Mkhytar on 28.04.2016.
 */
public abstract class BaseRemoteDataStore<T extends BaseDAO> {

    protected abstract ParseObject fillObject(ParseObject objectToFill, T data);

    protected List<ParseObject> addItems(Collection<T> items, String table, int flag) throws ParseException {
        ParseUser user = ParseUser.getCurrentUser();
        if (user == null) return null;

        final long refreshTime = System.currentTimeMillis();
        List<ParseObject> toSave = new ArrayList<>(items.size());

        for (T item : items) {
            ParseObject object = new ParseObject(table);
            fillObject(object, item, flag);
            object.put(ServerConstants.TIMESTAMP, refreshTime);
            toSave.add(object);
        }
        ParseObject.saveAll(toSave);
        ParseRelation<ParseObject> relation = user.getRelation(table);
        for (ParseObject item : toSave) {
            relation.add(item);
        }
        setRefreshTime(user, table, refreshTime);
        user.save();
        return toSave;
    }

    protected ParseObject getItem(String table, String parseId) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(table);
        query.whereContains(ListDAO.SERVER_ID, parseId);
        return query.get(parseId);
    }

    protected List<ParseObject> updateItems(Collection<T> list, String table, int flag) throws ParseException {
        ParseUser user = ParseUser.getCurrentUser();
        if (user == null) return null;

        final long refreshTime = System.currentTimeMillis();
        List<ParseObject> toSave = new ArrayList<>(list.size());

        for (T item : list) {
            if (item.getServerId() != null) {
                ParseObject object = ParseObject.createWithoutData(table, item.getServerId());
                fillObject(object, item, flag);
                object.put(ServerConstants.TIMESTAMP, refreshTime);
                toSave.add(object);
                item.setTimestamp(refreshTime);
            }
        }
        if (toSave.size() > 0) {
            ParseObject.saveAll(toSave);
            setRefreshTime(user, table, refreshTime);
            user.save();
        }
        return toSave;
    }

    protected Collection<T> deleteItems(Collection<T> lists, String table) throws ParseException {
        ParseUser user = ParseUser.getCurrentUser();
        if (user == null) return null;

        final long refreshTime = System.currentTimeMillis();
        ParseRelation<ParseObject> relation = user.getRelation(table);
        List<ParseObject> toDelete = new ArrayList<>(lists.size());
        for (T item : lists) {
            if (item.getServerId() != null) {
                ParseObject object = ParseObject.createWithoutData(table, item.getServerId());
                toDelete.add(object);
                relation.remove(object);
            }
        }
        if (toDelete.size() > 0) {
            ParseObject.deleteAll(toDelete);
            setRefreshTime(user, table, refreshTime);
            user.save();
        }
        return lists;
    }

    protected List<ParseObject> getAllItemsFromUser(String relationKey, long updateTime) throws ParseException {
        ParseUser user = ParseUser.getCurrentUser();
        if (user == null) return null;

        ParseRelation<ParseObject> relation = user.getRelation(relationKey);
        ParseQuery<ParseObject> query = relation.getQuery();
        query.whereGreaterThan(ServerConstants.TIMESTAMP, updateTime);
        switch (relationKey) {
            case ServerConstants.CATEGORIES:
                query.orderByAscending(CategoryDAO.CATEGORY_ID);
                break;
            case ServerConstants.CURRENCIES:
                query.orderByAscending(CurrencyDAO.CURRENCY_ID);
                break;
            case ServerConstants.UNITS:
                query.orderByAscending(UnitDAO.UNIT_ID);
                break;
        }
        return query.find();
    }

    protected ParseObject fillObject(ParseObject objectToFill, T item, int flag) {
        switch (flag) {
            case ServerConstants.CATEGORY:
                return fillObject(objectToFill, item);
            case ServerConstants.CURRENCY:
                return fillObject(objectToFill, item);
            case ServerConstants.SHOPPING_LIST:
                return fillObject(objectToFill, item);
            case ServerConstants.SHOPPING_LIST_ITEM:
                return fillObject(objectToFill, item);
            case ServerConstants.UNIT:
                return fillObject(objectToFill, item);
            case ServerConstants.GOODS:
                return fillObject(objectToFill, item);
        }
        return objectToFill;
    }

    protected Collection<T> deleteLists(Collection<T> lists, String table) throws ParseException {
        ParseUser user = ParseUser.getCurrentUser();
        if (user == null) return null;

        final long refreshTime = System.currentTimeMillis();
        ParseRelation<ParseObject> relation = user.getRelation(table);

        List<ParseObject> toDelete = new ArrayList<>(lists.size());
        for (T item : lists) {
            if (item.getServerId() != null) {
                ParseObject list = ParseObject.createWithoutData(table, item.getServerId());
                list.fetch();

                toDelete.add(list);
                relation.remove(list);
            }
        }
        if (toDelete.size() > 0) {
            ParseObject.deleteAll(toDelete);
            setRefreshTime(user, table, refreshTime);
            user.save();
        }
        return lists;
    }

    protected void setRefreshTime(ParseUser user, String table, final long time) {
        switch (table) {
            case ServerConstants.SHOPPING_LISTS_TABLE:
                user.put(ServerConstants.SHOPPING_LIST_TIMESTAMP, time);
                break;
            case ServerConstants.SHOPPING_LIST_ITEMS_TABLE:
                user.put(ServerConstants.SHOPPING_LIST_ITEM_TIMESTAMP, time);
                break;
            case ServerConstants.CATEGORIES:
                user.put(ServerConstants.CATEGORY_TIMESTAMP, time);
                break;
            case ServerConstants.CURRENCIES:
                user.put(ServerConstants.CURRENCY_TIMESTAMP, time);
                break;
            case ServerConstants.UNITS:
                user.put(ServerConstants.UNIT_TIMESTAMP, time);
                break;
            case ServerConstants.GOODS_TABLE:
                user.put(ServerConstants.GOODS_TIMESTAMP, time);
                break;
        }
    }
}
