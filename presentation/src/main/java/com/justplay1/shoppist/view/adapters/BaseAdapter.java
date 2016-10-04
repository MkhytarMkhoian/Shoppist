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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Checkable;

import com.justplay1.shoppist.models.BaseViewModel;
import com.justplay1.shoppist.models.HeaderViewModel;
import com.justplay1.shoppist.utils.AnimationResultListener;
import com.justplay1.shoppist.view.component.actionmode.ActionModeInteractionListener;
import com.justplay1.shoppist.view.component.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Mkhytar Mkhoian.
 */
public abstract class BaseAdapter<T extends BaseViewModel> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ActionModeInteractionListener actionModeInteractionListener;
    private boolean deleteState = false;
    private int checkedCount = 0;
    private Map<String, Boolean> checkedItems;

    protected RecyclerView recyclerView;

    LinearLayoutManager linearLayoutManager;
    Context context;
    ShoppistRecyclerView.OnItemClickListener<BaseViewHolder> itemClickListener;

    public BaseAdapter(Context context, ActionModeInteractionListener listener,
                       RecyclerView recyclerView) {
        setHasStableIds(true);
        this.context = context;
        this.actionModeInteractionListener = listener;
        this.recyclerView = recyclerView;
        linearLayoutManager = ((LinearLayoutManager) this.recyclerView.getLayoutManager());
        checkedItems = new HashMap<>();
    }

    protected abstract List<T> getCheckedItems();

    protected abstract List<T> getItems();

    protected abstract void refreshInvisibleItems();

    public boolean isAllItemsChecked() {
        return getItemCount() == checkedCount;
    }

    public int getCheckedItemsCount() {
        return checkedCount;
    }

    public void setCheckedItemsCount(int checkedCount) {
        this.checkedCount = checkedCount;
    }

    private List<Checkable> findVisibleItems() {
        List<Checkable> visibleItems = new ArrayList<>();
        final int firstPosition = linearLayoutManager.findFirstVisibleItemPosition();
        final int lastPosition = linearLayoutManager.findLastVisibleItemPosition();

        RecyclerView.ViewHolder holder;
        for (int i = firstPosition; i <= lastPosition; i++) {
            View item = linearLayoutManager.findViewByPosition(i);
            if (item == null) continue;

            holder = recyclerView.getChildViewHolder(item);
            if (holder instanceof Checkable) {
                visibleItems.add((Checkable) holder);
            }
        }
        return visibleItems;
    }

    public void unCheckAllItems() {
        if (deleteState) return;

        if (actionModeInteractionListener.isActionModeShowing()) {
            actionModeInteractionListener.closeActionMode();
        }
        checkedItems.clear();
        List<Checkable> visibleItems = findVisibleItems();
        for (Checkable checkable : visibleItems) {
            if (checkable.isChecked()) {
                checkable.setChecked(false);
            }
        }
        checkInvisibleItems(false);
        refreshInvisibleItems();
        checkedCount = 0;
    }

    public void checkAllItems() {
        List<Checkable> visibleItems = findVisibleItems();
        if (visibleItems.size() == 0) return;

        for (Checkable checkable : visibleItems) {
            if (!checkable.isChecked()) {
                checkable.setChecked(true);
            }
        }
        checkInvisibleItems(true);
        checkedCount = getCheckedItems().size();
        startActionMode();
        refreshInvisibleItems();
    }

    private void checkInvisibleItems(boolean check) {
        for (T item : getItems()) {
            if (item instanceof HeaderViewModel) continue;
            if (item.isChecked() != check) {
                item.setChecked(check);
                checkedItems.put(item.getId(), check);
            }
        }
    }

    protected boolean isItemChecked(String id) {
        Boolean isChecked = checkedItems.get(id);
        if (isChecked == null) return false;
        return isChecked;
    }

    protected void onCheckItem(T item, boolean isChecked) {
        item.setChecked(isChecked);
        updateCount(item.isChecked());
        if (isChecked) {
            checkedItems.put(item.getId(), true);
        } else {
            checkedItems.remove(item.getId());
        }
    }

    public void updateCount(final boolean isChecked) {
        if (isChecked) {
            if (checkedCount < getItemCount()) {
                checkedCount++;
            }
        } else {
            if (checkedCount != 0) {
                checkedCount--;
                if (checkedCount == 0 && actionModeInteractionListener.isActionModeShowing()) {
                    actionModeInteractionListener.closeActionMode();
                    return;
                }
            }
        }
        startActionMode();
    }

    private void startActionMode() {
        if (!actionModeInteractionListener.isActionModeShowing()) {
            actionModeInteractionListener.openActionMode(checkedCount);
        } else {
            actionModeInteractionListener.updateActionMode(checkedCount);
        }
    }

    private void finishDelete() {
        actionModeInteractionListener.closeActionMode();
        recyclerView.setEnabled(true);
        deleteState = false;
    }

    public void deleteCheckedView(final AnimationResultListener<T> resultListener) {
        recyclerView.setEnabled(false);
        deleteState = true;
        resultListener.onAnimationEnd(getCheckedItems());
        finishDelete();
    }

    public void setClickListener(ShoppistRecyclerView.OnItemClickListener clickListener) {
        this.itemClickListener = clickListener;
    }
}
