package com.justplay1.shoppist.repository.datasource.local.database;

import android.content.ContentValues;
import android.content.Context;

import com.justplay1.shoppist.data.R;
import com.justplay1.shoppist.entity.CategoryDAO;
import com.justplay1.shoppist.entity.ProductDAO;
import com.justplay1.shoppist.entity.UnitDAO;
import com.justplay1.shoppist.repository.datasource.local.LocalGoodsDataStore;
import com.squareup.sqlbrite.BriteDatabase;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Created by Mkhytar on 28.04.2016.
 */
@Singleton
public class LocalGoodsDataStoreImpl extends BaseLocalDataStore<ProductDAO> implements LocalGoodsDataStore {

    private static final String WITHOUT_DELETED = ProductDAO.IS_DELETED + "<1";
    private static final String LAST_TIMESTAMP_QUERY =
            "SELECT MAX(" + ProductDAO.TIMESTAMP + ") FROM " + ProductDAO.TABLE;

    public static String PRODUCTS_QUERY(String selection) {
        return ProductDAO.TABLE +
                " LEFT OUTER JOIN " + CategoryDAO.TABLE
                + " ON " + ProductDAO.CATEGORY_ID + " = " + CategoryDAO.TABLE
                + "." + CategoryDAO.CATEGORY_ID +
                " LEFT OUTER JOIN " + UnitDAO.TABLE
                + " ON " + ProductDAO.UNIT_ID + " = " + UnitDAO.TABLE
                + "." + UnitDAO.UNIT_ID +
                " WHERE " + selection;
    }

    private Context mContext;

    @Inject
    public LocalGoodsDataStoreImpl(BriteDatabase db, Context context) {
        super(db);
        this.mContext = context;
    }

    @Override
    public Observable<Map<String, ProductDAO>> getDefaultData() {
        String[] products = mContext.getResources().getStringArray(R.array.products);
        Map<String, ProductDAO> newProducts = new HashMap<>(products.length);

        for (String item : products) {
            String[] productsName = item.split(" ! ");

            CategoryDAO category = new CategoryDAO(productsName[1]);
            UnitDAO unit = new UnitDAO(UnitDAO.NO_UNIT_ID);

            ProductDAO product = new ProductDAO(UUID.nameUUIDFromBytes((productsName[0]).getBytes()).toString(),
                    null, productsName[0], 0, false, false, category, false, System.currentTimeMillis(), unit);
            newProducts.put(product.getId(), product);
        }
        return Observable.just(newProducts);
    }

    @Override
    public Observable<List<ProductDAO>> getItems() {
        return getAllProducts(0, false);
    }

    @Override
    public Observable<List<ProductDAO>> getDirtyItems() {
        return getAllProducts(0, true);
    }

    @Override
    public Observable<List<ProductDAO>> getItems(long timestamp) {
        return getAllProducts(timestamp, false);
    }

    @Override
    public Observable<ProductDAO> getItem(String id) {
        return db.createQuery(ProductDAO.TABLE, PRODUCTS_QUERY(ProductDAO.WHERE_PRODUCT_ID), id)
                .mapToOne(ProductDAO.MAPPER::call);
    }

    @Override
    public void save(Collection<ProductDAO> data) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            for (ProductDAO product : data) {
                db.insert(UnitDAO.TABLE, getValue(product));
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    @Override
    public void save(ProductDAO data) {
        save(Collections.singletonList(data));
    }

    @Override
    public void delete(Collection<ProductDAO> data) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            for (ProductDAO product : data) {
                db.delete(UnitDAO.TABLE, ProductDAO.WHERE_PRODUCT_ID, product.getId());
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    @Override
    public void delete(ProductDAO data) {
        delete(Collections.singletonList(data));
    }

    @Override
    public void update(Collection<ProductDAO> data) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            for (ProductDAO product : data) {
                db.update(UnitDAO.TABLE, getValue(product), ProductDAO.WHERE_PRODUCT_ID, product.getId());
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    @Override
    public void update(ProductDAO data) {
        update(Collections.singletonList(data));
    }

    @Override
    protected ContentValues getValue(ProductDAO product) {
        ProductDAO.Builder builder = new ProductDAO.Builder();
        if (product.getServerId() != null) {
            builder.serverId(product.getServerId());
        }
        builder.id(product.getId());
        builder.name(product.getName());
        builder.categoryId(product.getCategory().getId());
        builder.isCreateByUser(product.isCreateByUser());
        builder.timeCreated(product.getTimeCreated());
        builder.isDelete(product.isDelete());
        builder.isDirty(product.isDirty());
        builder.timestamp(product.getTimestamp());
        builder.unitId(product.getUnit().getId());
        return builder.build();
    }

    @Override
    public Observable<Long> getLastTimestamp() {
        return getValue(ProductDAO.TABLE, LAST_TIMESTAMP_QUERY, null);
    }

    @Override
    public int clear() {
        return clear(ProductDAO.TABLE);
    }

    public Observable<List<ProductDAO>> getAllProducts(long timestamp, boolean getDirty) {
        String selection = WITHOUT_DELETED;
        String[] selectionArgs = null;
        if (timestamp > 0 && getDirty) {
            selection = ProductDAO.TIMESTAMP + " > ? AND " + ProductDAO.IS_DIRTY + " = ?";
            selectionArgs = new String[]{timestamp + "", 1 + ""};
        } else if (timestamp > 0) {
            selection = ProductDAO.TIMESTAMP + " > ?";
            selectionArgs = new String[]{timestamp + ""};
        } else if (getDirty) {
            selection = ProductDAO.IS_DIRTY + " = ?";
            selectionArgs = new String[]{1 + ""};
        }
        return db.createQuery(ProductDAO.TABLE, PRODUCTS_QUERY(selection), selectionArgs)
                .mapToList(ProductDAO.MAPPER::call);
    }
}
