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

package com.justplay1.shoppist.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.justplay1.shoppist.models.BaseViewModel;
import com.justplay1.shoppist.models.HeaderViewModel;
import com.justplay1.shoppist.view.component.actionmode.ActionModeInteractionListener;
import com.justplay1.shoppist.view.component.animboxes.SelectBoxCheckListener;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseItemHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mkhytar Mkhoian.
 */
public abstract class BaseListAdapter<T extends BaseViewModel>
        extends BaseAdapter<T> {

    protected List<T> mData;

    public BaseListAdapter(Context context, ActionModeInteractionListener listener,
                           RecyclerView recyclerView) {
        super(context, listener, recyclerView);
        mData = new ArrayList<>(1);
    }

    @Override
    protected void refreshInvisibleItems() {
        if (mLinearLayoutManager.findFirstVisibleItemPosition() > 0) {
            notifyItemRangeChanged(0, mLinearLayoutManager.findFirstVisibleItemPosition());
        }
        notifyItemRangeChanged(mLinearLayoutManager.findLastVisibleItemPosition() + 1,
                getItems().size() - mLinearLayoutManager.findLastVisibleItemPosition());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.size() == 0) return super.getItemViewType(position);
        return getItem(position).getItemType();
    }

    public List<T> getItemsWithoutHeaders() {
        List<T> items = new ArrayList<>();
        for (T item : mData) {
            if (item instanceof HeaderViewModel) continue;
            items.add(item);
        }
        return items;
    }

    public T getItem(int position) {
        if (mData.size() == 0) return null;
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (mData.size() == 0) return 0;
        return mData.get(position).hashCode();
    }

    public List<T> getCheckedItems() {
        List<T> items = new ArrayList<>();
        for (T item : mData) {
            if (item instanceof HeaderViewModel) continue;
            if (item.isChecked()) {
                items.add(item);
            }
        }
        return items;
    }

    public List<T> getItems() {
        return mData;
    }

    public void setData(List<T> data) {
        mData = data;
    }
}
