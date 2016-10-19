/*
 * Copyright (C) 2016 Mkhytar Mkhoian
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.justplay1.shoppist.entity;

import android.content.ContentValues;
import android.database.Cursor;

import com.justplay1.shoppist.repository.datasource.local.database.DbUtil;

import rx.functions.Func1;

/**
 * Created by Mkhytar Mkhoian.
 */
public class ProductDAO extends BaseDAO {

    public static final String TABLE = "products";

    public static final String COL_ID = "product_id";
    public static final String COL_NAME = "product_name";
    public static final String COL_CATEGORY_ID = "product_category_id";
    public static final String COL_IS_CREATE_BY_USER = "product_is_create_by_user";
    public static final String COL_TIME_CREATED = "product_time_created";
    public static final String COL_UNIT_ID = "product_unit_id";

    public static final String WHERE_PRODUCT_ID = COL_ID + " IN(?)";

    private final CategoryDAO category;
    private final boolean isCreateByUser;
    private final long timeCreated;
    private final UnitDAO unit;

    public ProductDAO(String id, String name, CategoryDAO category, boolean isCreateByUser, long timeCreated, UnitDAO unit) {
        super(id, name);
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
        String id = DbUtil.getString(cursor, COL_ID);
        String name = DbUtil.getString(cursor, COL_NAME);
        boolean isCreateByUser = DbUtil.getBoolean(cursor, COL_IS_CREATE_BY_USER);
        long timeCreated = DbUtil.getLong(cursor, COL_TIME_CREATED);
        CategoryDAO category = CategoryDAO.map(cursor, COL_CATEGORY_ID);
        UnitDAO unit = UnitDAO.MAPPER.call(cursor);
        return new ProductDAO(id, name, category, isCreateByUser, timeCreated, unit);
    };

    public static final class Builder {
        private final ContentValues values = new ContentValues();

        public Builder id(String id) {
            values.put(COL_ID, id);
            return this;
        }

        public Builder name(String name) {
            values.put(COL_NAME, name);
            return this;
        }

        public Builder unitId(String id) {
            values.put(COL_UNIT_ID, id);
            return this;
        }

        public Builder categoryId(String id) {
            values.put(COL_CATEGORY_ID, id);
            return this;
        }

        public Builder isCreateByUser(boolean isCreate) {
            values.put(COL_IS_CREATE_BY_USER, isCreate ? 1 : 0);
            return this;
        }

        public Builder timeCreated(long timeCreated) {
            values.put(COL_TIME_CREATED, timeCreated);
            return this;
        }

        public ContentValues build() {
            return values;
        }
    }
}
