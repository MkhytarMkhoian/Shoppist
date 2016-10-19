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
public class ProductModel extends BaseModel {

    private final CategoryModel category;
    private final boolean isCreateByUser;
    private final long timeCreated;
    private final UnitModel unit;

    public ProductModel(String id, String name, CategoryModel category, boolean isCreateByUser, long timeCreated, UnitModel unit) {
        super(id, name);
        this.category = category;
        this.isCreateByUser = isCreateByUser;
        this.timeCreated = timeCreated;
        this.unit = unit;
    }

    public UnitModel getUnit() {
        return unit;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public boolean isCreateByUser() {
        return isCreateByUser;
    }

    public CategoryModel getCategory() {
        return category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof ProductModel)) return false;
        ProductModel item = (ProductModel) o;

        return item.getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
