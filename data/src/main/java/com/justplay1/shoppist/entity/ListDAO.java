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
public class ListDAO extends BaseDAO {

    public static final String TABLE = "shopping_lists";

    public static final String COL_ID = "shopping_list_id";
    public static final String COL_NAME = "shopping_list_name";
    public static final String COL_COLOR = "shopping_list_color";
    public static final String COL_PRIORITY = "shopping_list_priority";
    public static final String COL_TIME_CREATED = "shopping_list_time_created";
    public static final String COL_BOUGHT_COUNT = "shopping_list_bought_count";
    public static final String COL_SIZE = "shopping_list_size";

    public static final String WHERE_STRING = COL_ID + " IN(?)";

    private int boughtCount;
    private long timeCreated;
    private int priority = PriorityDAO.NO_PRIORITY;
    private int color = Color.DKGRAY;
    private int size;

    public ListDAO(String id, String name, int boughtCount, long timeCreated,
                   int priority, int color, int size) {
        super(id, name);
        this.boughtCount = boughtCount;
        this.timeCreated = timeCreated;
        this.priority = priority;
        this.color = color;
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public int getBoughtCount() {
        return boughtCount;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    @PriorityDAO
    public int getPriority() {
        return priority;
    }

    public int getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof ListDAO)) return false;
        ListDAO item = (ListDAO) o;

        return item.getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }


    public static final Func1<Cursor, ListDAO> MAPPER = (Func1<Cursor, ListDAO>) cursor -> {
        String id = DbUtil.getString(cursor, COL_ID);
        String name = DbUtil.getString(cursor, COL_NAME);
        int color = DbUtil.getInt(cursor, COL_COLOR);
        long timeCreated = DbUtil.getLong(cursor, COL_TIME_CREATED);
        int size = DbUtil.getInt(cursor, COL_SIZE);
        int boughtCount = DbUtil.getInt(cursor, COL_BOUGHT_COUNT);
        int priority = DbUtil.getInt(cursor, COL_PRIORITY);
        return new ListDAO(id, name, boughtCount, timeCreated, priority, color, size);
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

        public Builder color(int color) {
            values.put(COL_COLOR, color);
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

        public ContentValues build() {
            return values;
        }
    }
}
