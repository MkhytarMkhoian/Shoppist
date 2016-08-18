package com.justplay1.shoppist.repository.datasource.local.database;

import android.content.ContentValues;
import android.content.Context;

import com.justplay1.shoppist.data.R;
import com.justplay1.shoppist.entity.CategoryDAO;
import com.justplay1.shoppist.repository.datasource.local.LocalCategoryDataStore;
import com.squareup.sqlbrite.BriteDatabase;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Created by Mkhytar on 28.04.2016.
 */
@Singleton
public class LocalCategoryDataStoreImpl extends BaseLocalDataStore<CategoryDAO> implements LocalCategoryDataStore {

    public static final String WITHOUT_DELETED = CategoryDAO.IS_DELETED + "<1";

    private static final String LAST_TIMESTAMP_QUERY =
            "SELECT MAX(" + CategoryDAO.TIMESTAMP + ") FROM " + CategoryDAO.TABLE;

    private static String CATEGORY_QUERY(String selection) {
        return "SELECT * FROM " + CategoryDAO.TABLE +
                " WHERE " + selection;
    }

    private final Context mContext;

    @Inject
    public LocalCategoryDataStoreImpl(BriteDatabase db, Context context) {
        super(db);
        this.mContext = context;
    }

    @Override
    public Observable<Map<String, CategoryDAO>> getDefaultData() {
        String[] categories = mContext.getResources().getStringArray(R.array.categories);
        int[] categoriesColors = mContext.getResources().getIntArray(R.array.categories_colors);
        String[] categoriesName;

        Map<String, CategoryDAO> categoryList = new HashMap<>(categories.length);
        for (int i = 0; i < categories.length; i++) {
            categoriesName = categories[i].split(" ! ");
            String id = categoriesName[1];
            String serverId = null;
            String name = categoriesName[0];
            long timestamp = 0;
            boolean isDirty = false;
            boolean isDelete = false;
            int color = categoriesColors[i];
            boolean isCreateByUser = false;
            int position = -1;
            CategoryDAO category = new CategoryDAO(id, serverId, name, timestamp, isDirty, isDelete,
                    color, isCreateByUser, position);
            categoryList.put(category.getId(), category);
        }
        return Observable.just(categoryList);
    }

    @Override
    public Observable<List<CategoryDAO>> getItems() {
        return getAllCategories(0, false);
    }

    @Override
    public Observable<List<CategoryDAO>> getDirtyItems() {
        return getAllCategories(0, true);
    }

    @Override
    public Observable<List<CategoryDAO>> getItems(long timestamp) {
        return getAllCategories(timestamp, false);
    }

    @Override
    public Observable<CategoryDAO> getItem(String id) {
        return db.createQuery(CategoryDAO.TABLE, CATEGORY_QUERY(CategoryDAO.WHERE_CATEGORY_ID), id)
                .mapToOne(cursor -> CategoryDAO.map(cursor, CategoryDAO.CATEGORY_ID));
    }

    @Override
    public void save(Collection<CategoryDAO> data) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            for (CategoryDAO category : data) {
                db.insert(CategoryDAO.TABLE, getValue(category));
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    @Override
    public void save(CategoryDAO data) {
        save(Collections.singletonList(data));
    }

    @Override
    public void delete(Collection<CategoryDAO> data) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            for (CategoryDAO category : data) {
                db.delete(CategoryDAO.TABLE, CategoryDAO.WHERE_CATEGORY_ID, category.getId());
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    @Override
    public void delete(CategoryDAO data) {
        delete(Collections.singletonList(data));
    }

    @Override
    public void update(Collection<CategoryDAO> data) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        try {
            for (CategoryDAO category : data) {
                db.update(CategoryDAO.TABLE, getValue(category),
                        CategoryDAO.WHERE_CATEGORY_ID, category.getId());
            }
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    @Override
    public void update(CategoryDAO data) {
        update(Collections.singletonList(data));
    }

    @Override
    public int clear() {
        return clear(CategoryDAO.TABLE);
    }

    @Override
    public Observable<Long> getLastTimestamp() {
        return getValue(CategoryDAO.TABLE, LAST_TIMESTAMP_QUERY);
    }

    @Override
    protected ContentValues getValue(CategoryDAO data) {
        CategoryDAO.Builder builder = new CategoryDAO.Builder();
        builder.id(data.getId());
        if (data.getServerId() != null) {
            builder.serverId(data.getServerId());
        }
        builder.name(data.getName());
        builder.color(data.getColor());
        builder.createByUser(data.isCreateByUser());
        if (data.getPosition() != -1) {
            builder.position(data.getPosition());
        }
        builder.isDelete(data.isDelete());
        builder.isDirty(data.isDirty());
        builder.timestamp(data.getTimestamp());
        return builder.build();
    }

    private Observable<List<CategoryDAO>> getAllCategories(long timestamp, boolean getDirty) {
        String selection = WITHOUT_DELETED;
        String[] selectionArgs = new String[]{};
        if (timestamp > 0 && getDirty) {
            selection = CategoryDAO.TIMESTAMP + " > ? AND " + CategoryDAO.IS_DIRTY + " = ?";
            selectionArgs = new String[]{timestamp + "", "1"};
        } else if (timestamp > 0) {
            selection = CategoryDAO.TIMESTAMP + " > ?";
            selectionArgs = new String[]{timestamp + ""};
        } else if (getDirty) {
            selection = CategoryDAO.IS_DIRTY + " = ?";
            selectionArgs = new String[]{"1"};
        }

        return db.createQuery(CategoryDAO.TABLE, CATEGORY_QUERY(selection), selectionArgs)
                .mapToList(cursor -> CategoryDAO.map(cursor, CategoryDAO.CATEGORY_ID));
    }
}
