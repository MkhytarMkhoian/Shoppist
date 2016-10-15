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

package com.justplay1.shoppist.models;

/**
 * Created by Mkhytar Mkhoian.
 */
public class CategoryModel extends BaseModel {

    public static final String NO_CATEGORY_ID = "1";

    private int color;
    private boolean isCreateByUser;

    public CategoryModel(String id, String name, int color, boolean isCreateByUser) {
        super(id, name);
        this.color = color;
        this.isCreateByUser = isCreateByUser;
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
        if (o == null || !(o instanceof CategoryModel)) return false;
        CategoryModel item = (CategoryModel) o;

        return item.getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
