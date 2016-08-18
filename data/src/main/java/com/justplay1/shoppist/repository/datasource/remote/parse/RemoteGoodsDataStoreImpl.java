package com.justplay1.shoppist.repository.datasource.remote.parse;

import com.justplay1.shoppist.di.scopes.PerSync;
import com.justplay1.shoppist.entity.CategoryDAO;
import com.justplay1.shoppist.entity.ProductDAO;
import com.justplay1.shoppist.entity.UnitDAO;
import com.justplay1.shoppist.net.ServerConstants;
import com.justplay1.shoppist.repository.datasource.local.LocalCategoryDataStore;
import com.justplay1.shoppist.repository.datasource.local.LocalUnitsDataStore;
import com.parse.ParseException;
import com.parse.ParseObject;
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
public class RemoteGoodsDataStoreImpl extends BaseRemoteDataStore<ProductDAO> implements com.justplay1.shoppist.repository.datasource.remote.RemoteGoodsDataStore {

    private final LocalCategoryDataStore mLocalCategoryDataStore;
    private final LocalUnitsDataStore mUnitsDataStore;

    @Inject
    public RemoteGoodsDataStoreImpl(LocalCategoryDataStore localCategoryDataStore,
                                    LocalUnitsDataStore unitsDataStore) {
        this.mLocalCategoryDataStore = localCategoryDataStore;
        this.mUnitsDataStore = unitsDataStore;
    }

    @Override
    protected ParseObject fillObject(ParseObject objectToFill, ProductDAO data) {
        objectToFill.put(ProductDAO.PRODUCT_ID, data.getId());
        objectToFill.put(ProductDAO.IS_DELETED, data.isDelete());
        objectToFill.put(ProductDAO.NAME, data.getName());
        objectToFill.put(ProductDAO.IS_CREATE_BY_USER, data.isCreateByUser());
        objectToFill.put(ProductDAO.TIME_CREATED, data.getTimeCreated());
        if (!data.isCategoryEmpty()) {
            objectToFill.put(ProductDAO.CATEGORY_ID, data.getCategory().getId());
        } else {
            objectToFill.put(ProductDAO.CATEGORY_ID, CategoryDAO.NO_CATEGORY_ID);
        }
        if (!data.isUnitEmpty()) {
            objectToFill.put(ProductDAO.UNIT_ID, data.getUnit().getId());
        } else {
            objectToFill.put(ProductDAO.UNIT_ID, UnitDAO.NO_UNIT_ID);
        }
        return objectToFill;
    }

    public ProductDAO transform(ParseObject object, CategoryDAO categoryEntity, UnitDAO unitEntity) {
        return new ProductDAO(object.getString(ProductDAO.PRODUCT_ID),
                object.getObjectId(),
                object.getString(ProductDAO.NAME),
                object.getLong(ServerConstants.TIMESTAMP),
                false,
                object.getBoolean(ProductDAO.IS_DELETED),
                categoryEntity,
                object.getBoolean(ProductDAO.IS_CREATE_BY_USER),
                0,
                unitEntity);
    }

    private List<ParseObject> addProducts(Collection<ProductDAO> products) throws ParseException {
        ParseUser user = ParseUser.getCurrentUser();
        List<ParseObject> toSave = new ArrayList<>(products.size());
        if (user == null) return toSave;

        final long refreshTime = System.currentTimeMillis();

        for (ProductDAO item : products) {
            ParseObject object = new ParseObject(ServerConstants.GOODS_TABLE);
            fillObject(object, item);
            object.put(ServerConstants.TIMESTAMP, refreshTime);
            object.put(ServerConstants.CREATED_BY, user);
            toSave.add(object);
        }
        if (toSave.size() > 0) {
            ParseObject.saveAll(toSave);
            ParseRelation<ParseObject> relation = user.getRelation(ServerConstants.GOODS_TABLE);
            for (ParseObject object : toSave) {
                relation.add(object);
            }
            setRefreshTime(user, ServerConstants.GOODS_TABLE, refreshTime);
            user.save();
        }
        return toSave;
    }

    @Override
    public Collection<ProductDAO> getItems() throws Exception {
        return getItems(0);
    }

    @Override
    public Collection<ProductDAO> getItems(long timestamp) throws Exception {
//        Map<String, CategoryDAO> categoryMap = mLocalCategoryDataStore.getItems();
//        Map<String, UnitDAO> unitMap = mUnitsDataStore.getItems();
        List<ParseObject> parseObjects = getAllItemsFromUser(ServerConstants.GOODS_TABLE, timestamp);

        Collection<ProductDAO> result = new ArrayList<>();
//        for (ParseObject object : parseObjects) {
//            CategoryDAO category = categoryMap.get(object.getString(ProductDAO.CATEGORY_ID));
//            UnitDAO unit = unitMap.get(object.getString(ProductDAO.UNIT_ID));
//            ProductDAO productEntity = transform(object, category, unit);
//            result.add(productEntity);
//        }
        return result;
    }

    @Override
    public Collection<ProductDAO> save(Collection<ProductDAO> data) throws Exception {
        List<ParseObject> list = addProducts(data);
        for (ParseObject item : list) {
            for (ProductDAO product : data) {
                if (product.getId().equals(item.getString(ProductDAO.PRODUCT_ID))) {
                    product.setServerId(item.getObjectId());
                    product.setTimestamp(item.getLong(ServerConstants.TIMESTAMP));
                    product.setDirty(false);
                }
            }
        }
        return data;
    }

    @Override
    public void delete(Collection<ProductDAO> data) throws Exception {
        deleteItems(data, ServerConstants.GOODS_TABLE);
    }

    @Override
    public void update(Collection<ProductDAO> data) throws Exception {
        updateItems(data, ServerConstants.GOODS_TABLE, ServerConstants.GOODS);
    }

    @Override
    public int clear() {
        return 0;
    }
}
