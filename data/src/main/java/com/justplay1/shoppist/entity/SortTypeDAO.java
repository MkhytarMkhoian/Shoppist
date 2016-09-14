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

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Mkhytar Mkhoian.
 */
@IntDef({SortTypeDAO.SORT_BY_NAME, SortTypeDAO.SORT_BY_CATEGORIES, SortTypeDAO.SORT_BY_PRIORITY, SortTypeDAO.SORT_BY_TIME_CREATED})
@Retention(RetentionPolicy.SOURCE)
public @interface SortTypeDAO {
    int SORT_BY_NAME = 1;
    int SORT_BY_PRIORITY = 2;
    int SORT_BY_CATEGORIES = 3;
    int SORT_BY_TIME_CREATED = 4;
}
