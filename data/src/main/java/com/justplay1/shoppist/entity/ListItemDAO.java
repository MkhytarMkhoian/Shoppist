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

    public static final String LIST_ITEM_ID = "shopping_list_item_id";
    public static final String PARENT_LIST_ID = "shopping_list_item_parent_list_id";
    public static final String LIST_ITEM_NAME = "shopping_list_item_name";
    public static final String SHORT_DESCRIPTION = "shopping_list_item_short_description";
    public static final String STATUS = "shopping_list_item_status";
    public static final String PRIORITY = "shopping_list_item_priority";
    public static final String PRICE = "shopping_list_item_price";
    public static final String QUANTITY = "shopping_list_item_quantity";
    public static final String UNIT_ID = "shopping_list_item_unit_id";
    public static final String CURRENCY_ID = "shopping_list_item_currency_id";
    public static final String TIME_CREATED = "shopping_list_item_time_created";
    public static final String CATEGORY_ID = "shopping_list_item_category_id";

    public static final String WHERE_STRING = PARENT_LIST_ID + "=? and " + LIST_ITEM_ID + " IN(?)";

    private String parentListId;
    private String note;
    private boolean status = false;
    private CategoryDAO category;
    private int priority = PriorityDAO.NO_PRIORITY;
    private double price;
    private double quantity = 1;
    private UnitDAO unit;
    private long timeCreated;
    private CurrencyDAO currency;

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
        String id = DbUtil.getString(cursor, LIST_ITEM_ID);
        String parentId = DbUtil.getString(cursor, PARENT_LIST_ID);
        String name = DbUtil.getString(cursor, LIST_ITEM_NAME);
        boolean status = DbUtil.getBoolean(cursor, STATUS);
        long timeCreated = DbUtil.getLong(cursor, TIME_CREATED);
        double price = DbUtil.getDouble(cursor, PRICE);
        double quantity = DbUtil.getDouble(cursor, QUANTITY);
        int priority = DbUtil.getInt(cursor, PRIORITY);
        String note = DbUtil.getString(cursor, SHORT_DESCRIPTION);
        CategoryDAO category = CategoryDAO.map(cursor, CATEGORY_ID);
        UnitDAO unit = UnitDAO.MAPPER.call(cursor);
        CurrencyDAO currency = CurrencyDAO.MAPPER.call(cursor);
        return new ListItemDAO(id, name, parentId, note, status, category, priority,
                price, quantity, unit, timeCreated, currency);
    };

    public static final class Builder {
        private final ContentValues values = new ContentValues();

        public Builder id(String id) {
            values.put(LIST_ITEM_ID, id);
            return this;
        }

        public Builder parentId(String parentId) {
            values.put(PARENT_LIST_ID, parentId);
            return this;
        }

        public Builder name(String name) {
            values.put(LIST_ITEM_NAME, name);
            return this;
        }

        public Builder status(boolean status) {
            values.put(STATUS, status ? 1 : 0);
            return this;
        }

        public Builder priority(int priority) {
            values.put(PRIORITY, priority);
            return this;
        }

        public Builder timeCreated(long timeCreated) {
            values.put(TIME_CREATED, timeCreated);
            return this;
        }

        public Builder price(double price) {
            values.put(PRICE, price);
            return this;
        }

        public Builder quantity(double quantity) {
            values.put(QUANTITY, quantity);
            return this;
        }

        public Builder description(String description) {
            values.put(SHORT_DESCRIPTION, description);
            return this;
        }

        public Builder categoryId(String id) {
            values.put(CATEGORY_ID, id);
            return this;
        }

        public Builder unitId(String id) {
            values.put(UNIT_ID, id);
            return this;
        }

        public Builder currencyId(String id) {
            values.put(CURRENCY_ID, id);
            return this;
        }

        public ContentValues build() {
            return values;
        }
    }
}
