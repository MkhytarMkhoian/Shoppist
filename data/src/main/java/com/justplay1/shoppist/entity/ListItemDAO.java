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
public class ListItemDAO extends BaseDAO {

    public static final String TABLE = "shopping_list_items";

    public static final String COL_ID = "shopping_list_item_id";
    public static final String COL_PARENT_LIST_ID = "shopping_list_item_parent_list_id";
    public static final String COL_NAME = "shopping_list_item_name";
    public static final String COL_NOTE = "shopping_list_item_short_description";
    public static final String COL_STATUS = "shopping_list_item_status";
    public static final String COL_PRIORITY = "shopping_list_item_priority";
    public static final String COL_PRICE = "shopping_list_item_price";
    public static final String COL_QUANTITY = "shopping_list_item_quantity";
    public static final String COL_UNIT_ID = "shopping_list_item_unit_id";
    public static final String COL_CURRENCY_ID = "shopping_list_item_currency_id";
    public static final String COL_TIME_CREATED = "shopping_list_item_time_created";
    public static final String COL_CATEGORY_ID = "shopping_list_item_category_id";

    public static final String WHERE_STRING = COL_PARENT_LIST_ID + "=? and " + COL_ID + " IN(?)";

    private final String parentListId;
    private final String note;
    private final boolean status;
    private final CategoryDAO category;
    private final int priority;
    private final double price;
    private final double quantity;
    private final UnitDAO unit;
    private final long timeCreated;
    private final CurrencyDAO currency;

    public ListItemDAO(String id, String name, String parentListId, String note, boolean status, CategoryDAO category,
                       int priority, double price, double quantity, UnitDAO unit, long timeCreated,
                       CurrencyDAO currency) {
        super(id, name);
        this.parentListId = parentListId;
        this.note = note;
        this.status = status;
        this.category = category;
        this.priority = priority;
        this.price = price;
        this.quantity = quantity;
        this.unit = unit;
        this.timeCreated = timeCreated;
        this.currency = currency;
    }

    public String getParentListId() {
        return parentListId;
    }

    public String getNote() {
        return note;
    }

    public boolean getStatus() {
        return status;
    }

    public CategoryDAO getCategory() {
        return category;
    }

    @PriorityDAO
    public int getPriority() {
        return priority;
    }

    public double getPrice() {
        return price;
    }

    public double getQuantity() {
        return quantity;
    }

    public UnitDAO getUnit() {
        return unit;
    }

    public CurrencyDAO getCurrency() {
        return currency;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public boolean isCategoryEmpty() {
        return category == null || category.getId() == null || category.getName() == null;
    }

    public boolean isCurrencyEmpty() {
        return currency == null || currency.getId() == null || currency.getName() == null;
    }

    public boolean isUnitEmpty() {
        return unit == null || unit.getId() == null || unit.getName() == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof ListItemDAO)) return false;

        ListItemDAO item = (ListItemDAO) o;
        return item.getId().equals(this.getId())
                && (item.getParentListId().equals(this.getParentListId()));
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        return result + parentListId.hashCode();
    }

    public static final Func1<Cursor, ListItemDAO> MAPPER = (Func1<Cursor, ListItemDAO>) cursor -> {
        String id = DbUtil.getString(cursor, COL_ID);
        String parentId = DbUtil.getString(cursor, COL_PARENT_LIST_ID);
        String name = DbUtil.getString(cursor, COL_NAME);
        boolean status = DbUtil.getBoolean(cursor, COL_STATUS);
        long timeCreated = DbUtil.getLong(cursor, COL_TIME_CREATED);
        double price = DbUtil.getDouble(cursor, COL_PRICE);
        double quantity = DbUtil.getDouble(cursor, COL_QUANTITY);
        int priority = DbUtil.getInt(cursor, COL_PRIORITY);
        String note = DbUtil.getString(cursor, COL_NOTE);
        CategoryDAO category = CategoryDAO.map(cursor, COL_CATEGORY_ID);
        UnitDAO unit = UnitDAO.MAPPER.call(cursor);
        CurrencyDAO currency = CurrencyDAO.MAPPER.call(cursor);
        return new ListItemDAO(id, name, parentId, note, status, category, priority,
                price, quantity, unit, timeCreated, currency);
    };

    public static final class Builder {
        private final ContentValues values = new ContentValues();

        public Builder id(String id) {
            values.put(COL_ID, id);
            return this;
        }

        public Builder parentId(String parentId) {
            values.put(COL_PARENT_LIST_ID, parentId);
            return this;
        }

        public Builder name(String name) {
            values.put(COL_NAME, name);
            return this;
        }

        public Builder status(boolean status) {
            values.put(COL_STATUS, status ? 1 : 0);
            return this;
        }

        public Builder priority(int priority) {
            values.put(COL_PRIORITY, priority);
            return this;
        }

        public Builder timeCreated(long timeCreated) {
            values.put(COL_TIME_CREATED, timeCreated);
            return this;
        }

        public Builder price(double price) {
            values.put(COL_PRICE, price);
            return this;
        }

        public Builder quantity(double quantity) {
            values.put(COL_QUANTITY, quantity);
            return this;
        }

        public Builder note(String note) {
            values.put(COL_NOTE, note);
            return this;
        }

        public Builder categoryId(String id) {
            values.put(COL_CATEGORY_ID, id);
            return this;
        }

        public Builder unitId(String id) {
            values.put(COL_UNIT_ID, id);
            return this;
        }

        public Builder currencyId(String id) {
            values.put(COL_CURRENCY_ID, id);
            return this;
        }

        public ContentValues build() {
            return values;
        }
    }
}
