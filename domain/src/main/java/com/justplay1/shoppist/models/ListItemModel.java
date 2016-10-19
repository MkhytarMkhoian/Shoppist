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
public class ListItemModel extends BaseModel {

    private final String parentListId;
    private final String note;
    private final boolean status;
    private final CategoryModel category;
    private final int priority;
    private final double price;
    private final double quantity;
    private final UnitModel unit;
    private final long timeCreated;
    private final CurrencyModel currency;

    public ListItemModel(String id, String name, String parentListId, String note, boolean status, CategoryModel category,
                         int priority, double price, double quantity, UnitModel unit, long timeCreated,
                         CurrencyModel currency) {
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

    public CategoryModel getCategory() {
        return category;
    }

    public int getPriority() {
        return priority;
    }

    public double getPrice() {
        return price;
    }

    public double getQuantity() {
        return quantity;
    }

    public UnitModel getUnit() {
        return unit;
    }


    public CurrencyModel getCurrency() {
        return currency;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof ListItemModel)) return false;

        ListItemModel item = (ListItemModel) o;
        return item.getId().equals(this.getId())
                && (item.getParentListId().equals(this.getParentListId()));
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        return result + parentListId.hashCode();
    }
}
