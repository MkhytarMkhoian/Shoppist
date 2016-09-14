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

    private String parentListId;
    private String note;
    private boolean status;
    private CategoryModel category;
    private int priority;
    private double price;
    private double quantity;
    private UnitModel unit;
    private long timeCreated;
    private CurrencyModel currency;
    private int position;

    public ListItemModel() {
        note = "";
        quantity = 1;
        position = -1;
    }

    public ListItemModel(ListItemModel item) {
        this();
        setParentListId(item.getParentListId());
        setId(item.getId());
        setName(item.getName());
        setNote(item.getNote());
        setUnit(item.getUnit());
        setCurrency(item.getCurrency());
        setPrice(item.getPrice());
        setTimeCreated(item.getTimeCreated());
        setQuantity(item.getQuantity());
        setStatus(item.getStatus());
        setPriority(item.getPriority());
        setCategory(item.getCategory());
        setPosition(item.getPosition());
    }

    public String getParentListId() {
        return parentListId;
    }

    public void setParentListId(String parentListId) {
        this.parentListId = parentListId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String shortMessage) {
        this.note = shortMessage;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public CategoryModel getCategory() {
        return category;
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

    public void setCategory(CategoryModel category) {
        this.category = category;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public UnitModel getUnit() {
        return unit;
    }

    public void setUnit(UnitModel unit) {
        this.unit = unit;
    }

    public CurrencyModel getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyModel currency) {
        this.currency = currency;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
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
