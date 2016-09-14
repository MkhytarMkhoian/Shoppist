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
public class CurrencyDAO extends BaseDAO {

    public static final String NO_CURRENCY_ID = "no_currency";

    public static final String TABLE = "currency";

    public static final String CURRENCY_ID = "main_currency_id";
    public static final String NAME = "currency_name";

    public static final String WHERE_STRING = CURRENCY_ID + " IN(?)";

    public CurrencyDAO(String id, String name) {
        super(id, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof CurrencyDAO)) return false;
        CurrencyDAO item = (CurrencyDAO) o;

        return item.getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return 31 * id.hashCode();
    }

    public static final Func1<Cursor, CurrencyDAO> MAPPER = (Func1<Cursor, CurrencyDAO>) cursor -> {
        String id = DbUtil.getString(cursor, CURRENCY_ID);
        String name = DbUtil.getString(cursor, NAME);
        return new CurrencyDAO(id, name);
    };

    public static final class Builder {
        private final ContentValues values = new ContentValues();

        public Builder id(String id) {
            values.put(CURRENCY_ID, id);
            return this;
        }

        public Builder name(String name) {
            values.put(NAME, name);
            return this;
        }

        public ContentValues build() {
            return values;
        }
    }
}
