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
import android.graphics.Color;

import com.justplay1.shoppist.repository.datasource.local.database.DbUtil;

import rx.functions.Func1;

/**
 * Created by Mkhytar Mkhoian.
 */
public class CategoryDAO extends BaseDAO {

    public static final String NO_CATEGORY_ID = "1";

    public static final String TABLE = "categories";

    public static final String NAME = "category_name";
    public static final String CATEGORY_ID = "main_category_id";
    public static final String COLOR = "category_color";
    public static final String CREATE_BY_USER = "category_create_by_user";
    public static final String MANUAL_SORT_POSITION = "category_manual_sort_position";

    public static final String WHERE_CATEGORY_ID = CATEGORY_ID + " IN(?)";

    private int color = Color.DKGRAY;
    private boolean isCreateByUser;
    private int position = -1;

    public CategoryDAO(String id) {
        super(id, null);
    }

    public CategoryDAO(String id, String name, int color, boolean isCreateByUser, int position) {
        super(id, name);
        this.color = color;
        this.isCreateByUser = isCreateByUser;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public int getColor() {
        return color;
    }

    public boolean isCreateByUser() {
        return isCreateByUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof CategoryDAO)) return false;
        CategoryDAO item = (CategoryDAO) o;

        return item.getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return 31 * id.hashCode();
    }

    public static final Func1<Cursor, CategoryDAO> MAPPER = (Func1<Cursor, CategoryDAO>) cursor -> map(cursor, CATEGORY_ID);

    public static CategoryDAO map(Cursor cursor, String categoryId) {
        String id = DbUtil.getString(cursor, categoryId);
        String name = DbUtil.getString(cursor, NAME);
        int color = DbUtil.getInt(cursor, COLOR);
        boolean createByUser = DbUtil.getBoolean(cursor, CREATE_BY_USER);
        int position = DbUtil.getInt(cursor, MANUAL_SORT_POSITION);
        return new CategoryDAO(id, name, color, createByUser, position);
    }

    public static final class Builder {
        private final ContentValues values = new ContentValues();

        public Builder id(String id) {
            values.put(CATEGORY_ID, id);
            return this;
        }

        public Builder name(String name) {
            values.put(NAME, name);
            return this;
        }

        public Builder color(int color) {
            values.put(COLOR, color);
            return this;
        }

        public Builder createByUser(boolean create) {
            values.put(CREATE_BY_USER, create ? 1 : 0);
            return this;
        }

        public Builder position(int position) {
            values.put(MANUAL_SORT_POSITION, position);
            return this;
        }

        public ContentValues build() {
            return values;
        }
    }
}
