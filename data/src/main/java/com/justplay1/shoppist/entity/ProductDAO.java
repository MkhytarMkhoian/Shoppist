package com.justplay1.shoppist.entity;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.justplay1.shoppist.repository.datasource.local.database.DbUtil;

import rx.functions.Func1;

/**
 * Created by Mkhitar on 18.05.2015.
 */
public class ProductDAO extends BaseDAO {

    public static final String TABLE = "products";

    public static final String PRODUCT_ID = "product_id";
    public static final String NAME = "product_name";
    public static final String CATEGORY_ID = "product_category_id";
    public static final String IS_CREATE_BY_USER = "product_is_create_by_user";
    public static final String SERVER_ID = "product_parse_id";
    public static final String TIME_CREATED = "product_time_created";
    public static final String UNIT_ID = "product_unit_id";
    public static final String IS_DELETED = "product_is_deleted";
    public static final String TIMESTAMP = "product_timestamp";
    public static final String IS_DIRTY = "product_is_dirty";

    public static final String WHERE_PRODUCT_ID = PRODUCT_ID + "=?";

    private CategoryDAO category;
    private boolean isCreateByUser;
    private long timeCreated;
    private UnitDAO unit;

    public ProductDAO(String id, String serverId, String name, long timestamp, boolean isDirty,
                      boolean isDelete, CategoryDAO category, boolean isCreateByUser, long timeCreated, UnitDAO unit) {
        super(id, serverId, name, timestamp, isDirty, isDelete);
        this.category = category;
        this.isCreateByUser = isCreateByUser;
        this.timeCreated = timeCreated;
        this.unit = unit;
    }

    public UnitDAO getUnit() {
        return unit;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public boolean isCreateByUser() {
        return isCreateByUser;
    }

    public CategoryDAO getCategory() {
        return category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof ProductDAO)) return false;
        ProductDAO item = (ProductDAO) o;

        return item.getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public boolean isCategoryEmpty() {
        return category == null || category.getId() == null || category.getName() == null;
    }

    public boolean isUnitEmpty() {
        return unit == null || unit.getId() == null || unit.getName() == null;
    }

    public static final Func1<Cursor, ProductDAO> MAPPER = (Func1<Cursor, ProductDAO>) cursor -> {
        String id = DbUtil.getString(cursor, PRODUCT_ID);
        String name = DbUtil.getString(cursor, NAME);
        boolean isCreateByUser = DbUtil.getBoolean(cursor, IS_CREATE_BY_USER);
        String serverId = DbUtil.getString(cursor, SERVER_ID);
        boolean isDelete = DbUtil.getBoolean(cursor, IS_DELETED);
        boolean isDirty = DbUtil.getBoolean(cursor, IS_DIRTY);
        long timestamp = DbUtil.getLong(cursor, TIMESTAMP);
        long timeCreated = DbUtil.getLong(cursor, TIME_CREATED);
        CategoryDAO category = CategoryDAO.map(cursor, CATEGORY_ID);
        UnitDAO unit = UnitDAO.MAPPER.call(cursor);
        return new ProductDAO(id, serverId, name, timestamp, isDirty, isDelete, category, isCreateByUser, timeCreated, unit);
    };

    public static final class Builder {
        private final ContentValues values = new ContentValues();

        public Builder id(String id) {
            values.put(PRODUCT_ID, id);
            return this;
        }

        public Builder name(String name) {
            values.put(NAME, name);
            return this;
        }

        public Builder unitId(String id) {
            values.put(UNIT_ID, id);
            return this;
        }

        public Builder categoryId(String id) {
            values.put(CATEGORY_ID, id);
            return this;
        }

        public Builder serverId(String serverId) {
            values.put(SERVER_ID, serverId);
            return this;
        }

        public Builder isCreateByUser(boolean isCreate) {
            values.put(IS_CREATE_BY_USER, isCreate ? 1 : 0);
            return this;
        }

        public Builder isDelete(boolean delete) {
            values.put(IS_DELETED, delete ? 1 : 0);
            return this;
        }

        public Builder isDirty(boolean dirty) {
            values.put(IS_DIRTY, dirty ? 1 : 0);
            return this;
        }

        public Builder timestamp(long timestamp) {
            values.put(TIMESTAMP, timestamp);
            return this;
        }

        public Builder timeCreated(long timeCreated) {
            values.put(TIME_CREATED, timeCreated);
            return this;
        }

        public ContentValues build() {
            return values;
        }
    }
}