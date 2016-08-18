package com.justplay1.shoppist.repository.datasource.remote.parse;

import com.justplay1.shoppist.di.scopes.PerSync;
import com.justplay1.shoppist.entity.CategoryDAO;
import com.justplay1.shoppist.net.ServerConstants;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 28.04.2016.
 */
@PerSync
public class RemoteCategoryDataStoreImpl extends BaseRemoteDataStore<CategoryDAO> implements com.justplay1.shoppist.repository.datasource.remote.RemoteCategoryDataStore {

    @Inject
    public RemoteCategoryDataStoreImpl() {

    }

    @Override
    protected ParseObject fillObject(ParseObject objectToFill, CategoryDAO data) {
        objectToFill.put(CategoryDAO.IS_DELETED, data.isDelete());
        objectToFill.put(CategoryDAO.CATEGORY_ID, data.getId());
        objectToFill.put(CategoryDAO.NAME, data.getName());
        objectToFill.put(CategoryDAO.COLOR, data.getColor());
        objectToFill.put(CategoryDAO.CREATE_BY_USER, data.isCreateByUser());
        return objectToFill;
    }

    public CategoryDAO transformToEntity(ParseObject object) {
        return new CategoryDAO(object.getString(CategoryDAO.CATEGORY_ID),
                object.getObjectId(),
                object.getString(CategoryDAO.NAME),
                object.getLong(ServerConstants.TIMESTAMP),
                false,
                object.getBoolean(CategoryDAO.IS_DELETED),
                object.getInt(CategoryDAO.COLOR),
                object.getBoolean(CategoryDAO.CREATE_BY_USER),
                object.getInt(CategoryDAO.MANUAL_SORT_POSITION));
    }

    @Override
    public Collection<CategoryDAO> getItems() throws Exception {
        return getItems(0);
    }

    @Override
    public Collection<CategoryDAO> getItems(long timestamp) throws Exception {
        List<ParseObject> parseObjects = getAllItemsFromUser(ServerConstants.CATEGORIES, timestamp);
        Collection<CategoryDAO> result = new ArrayList<>();
        for (ParseObject object : parseObjects) {
            CategoryDAO categoryEntity = transformToEntity(object);
            result.add(categoryEntity);
        }
        return result;
    }

    @Override
    public Collection<CategoryDAO> save(Collection<CategoryDAO> data) throws Exception {
        if (data.size() > 0) {
            List<ParseObject> object = addItems(data, ServerConstants.CATEGORIES, ServerConstants.CATEGORY);
            data.clear();
            for (ParseObject item : object) {
                CategoryDAO category = transformToEntity(item);
                data.add(category);
            }
        }
        return data;
    }

    @Override
    public void delete(Collection<CategoryDAO> data) throws Exception {
        deleteItems(data, ServerConstants.CATEGORIES);
    }

    @Override
    public void update(Collection<CategoryDAO> data) throws Exception {
        updateItems(data, ServerConstants.CATEGORIES, ServerConstants.CATEGORY);
    }

    @Override
    public int clear() throws Exception {
        deleteCategories();
        return 1;
    }

    public void deleteCategories() throws ParseException {
        ParseUser user = ParseUser.getCurrentUser();
        if (user == null) return;

        final long refreshTime = System.currentTimeMillis();
        ParseObject.deleteAll(user.getRelation(ServerConstants.CATEGORIES).getQuery().find());

        setRefreshTime(user, ServerConstants.CATEGORIES, refreshTime);
        user.save();
    }
}
