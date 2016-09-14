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

    public static final String LIST_ID = "shopping_list_id";
    public static final String LIST_NAME = "shopping_list_name";
    public static final String COLOR = "shopping_list_color";
    public static final String PRIORITY = "shopping_list_priority";
    public static final String TIME_CREATED = "shopping_list_time_created";
    public static final String BOUGHT_COUNT = "shopping_list_bought_count";
    public static final String SIZE = "shopping_list_size";
    public static final String MANUAL_SORT_POSITION = "shopping_list_manual_sort_position";

    public static final String WHERE_STRING = LIST_ID + " IN(?)";

    private int boughtCount;
    private long timeCreated;
    private int priority = PriorityDAO.NO_PRIORITY;
    private int color = Color.DKGRAY;
    private int size;
    private int position = -1;

    public ListDAO(String id, String name, int boughtCount, long timeCreated,
                   int priority, int color, int size, int position) {
        super(id, name);
        this.boughtCount = boughtCount;
        this.timeCreated = timeCreated;
        this.priority = priority;
        this.color = color;
        this.size = size;
        this.position = position;
    }

    public int getPosition() {
        return position;
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
        String id = DbUtil.getString(cursor, LIST_ID);
        String name = DbUtil.getString(cursor, LIST_NAME);
        int color = DbUtil.getInt(cursor, COLOR);
        int position = DbUtil.getInt(cursor, MANUAL_SORT_POSITION);
        long timeCreated = DbUtil.getLong(cursor, TIME_CREATED);
        int size = DbUtil.getInt(cursor, SIZE);
        int boughtCount = DbUtil.getInt(cursor, BOUGHT_COUNT);
        int priority = DbUtil.getInt(cursor, PRIORITY);
        return new ListDAO(id, name, boughtCount, timeCreated, priority, color, size, position);
    };

    public static final class Builder {
        private final ContentValues values = new ContentValues();

        public Builder id(String id) {
            values.put(LIST_ID, id);
            return this;
        }

        public Builder name(String name) {
            values.put(LIST_NAME, name);
            return this;
        }

        public Builder color(int color) {
            values.put(COLOR, color);
            return this;
        }

        public Builder priority(int priority) {
            values.put(PRIORITY, priority);
            return this;
        }

        public Builder position(int position) {
            values.put(MANUAL_SORT_POSITION, position);
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
