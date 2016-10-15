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
public class UnitDAO extends BaseDAO {

    public static final String NO_UNIT_ID = "no_unit";

    public static final String TABLE = "units";

    public static final String COL_ID = "main_unit_id";
    public static final String COL_FULL_NAME = "unit_full_name";
    public static final String COL_SHORT_NAME = "unit_short_name";

    public static final String WHERE_STRING = COL_ID + " IN(?)";

    private String shortName;

    public UnitDAO(String id) {
        super(id, null);
    }

    public UnitDAO(String id, String name, String shortName) {
        super(id, name);
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof UnitDAO)) return false;
        UnitDAO item = (UnitDAO) o;

        return item.getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public static final Func1<Cursor, UnitDAO> MAPPER = (Func1<Cursor, UnitDAO>) cursor -> {
        String id = DbUtil.getString(cursor, COL_ID);
        String fullName = DbUtil.getString(cursor, COL_FULL_NAME);
        String shortName = DbUtil.getString(cursor, COL_SHORT_NAME);
        return new UnitDAO(id, fullName, shortName);
    };

    public static final class Builder {
        private final ContentValues values = new ContentValues();

        public Builder id(String id) {
            values.put(COL_ID, id);
            return this;
        }

        public Builder fullName(String fullName) {
            values.put(COL_FULL_NAME, fullName);
            return this;
        }

        public Builder shortName(String shortName) {
            values.put(COL_SHORT_NAME, shortName);
            return this;
        }

        public ContentValues build() {
            return values;
        }
    }
}
