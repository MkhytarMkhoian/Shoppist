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

    Context mContext;
    ShoppistRecyclerView.OnItemClickListener<BaseViewHolder> mItemClickListener;
    private ActionModeInteractionListener mActionModeInteractionListener;

    protected RecyclerView mRecyclerView;
    private boolean deleteState = false;
    private int mCheckedCount = 0;
    LinearLayoutManager mLinearLayoutManager;
    private Map<String, Boolean> mCheckedItems;

    public BaseAdapter(Context context, ActionModeInteractionListener listener,
                       RecyclerView recyclerView) {
        setHasStableIds(true);
        this.mContext = context;
        this.mActionModeInteractionListener = listener;
        this.mRecyclerView = recyclerView;
        mLinearLayoutManager = ((LinearLayoutManager) mRecyclerView.getLayoutManager());
        mCheckedItems = new HashMap<>();
    }

    protected abstract List<T> getCheckedItems();

    protected abstract List<T> getItems();

    protected abstract void refreshInvisibleItems();

    public boolean isAllItemsChecked() {
        return getItemCount() == mCheckedCount;
    }

    public int getCheckedItemsCount() {
        return mCheckedCount;
    }

    public void setCheckedItemsCount(int checkedCount) {
        this.mCheckedCount = checkedCount;
    }

    private List<Checkable> findVisibleItems() {
        List<Checkable> visibleItems = new ArrayList<>();
        final int firstPosition = mLinearLayoutManager.findFirstVisibleItemPosition();
        final int lastPosition = mLinearLayoutManager.findLastVisibleItemPosition();

        RecyclerView.ViewHolder holder;
        for (int i = firstPosition; i <= lastPosition; i++) {
            View item = mLinearLayoutManager.findViewByPosition(i);
            if (item == null) continue;

            holder = mRecyclerView.getChildViewHolder(item);
            if (holder instanceof Checkable) {
                visibleItems.add((Checkable) holder);
            }
        }
        return visibleItems;
    }

    public void unCheckAllItems() {
        if (deleteState) return;

        if (mActionModeInteractionListener.isActionModeShowing()) {
            mActionModeInteractionListener.closeActionMode();
        }
        mCheckedItems.clear();
        List<Checkable> visibleItems = findVisibleItems();
        for (Checkable checkable : visibleItems) {
            if (checkable.isChecked()) {
                checkable.setChecked(false);
            }
        }
        checkInvisibleItems(false);
        refreshInvisibleItems();
        mCheckedCount = 0;
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
        mCheckedCount = getCheckedItems().size();
        startActionMode();
        refreshInvisibleItems();
    }

    private void checkInvisibleItems(boolean check) {
        for (T item : getItems()) {
            if (item instanceof HeaderViewModel) continue;
            if (item.isChecked() != check) {
                item.setChecked(check);
                mCheckedItems.put(item.getId(), check);
            }
        }
    }

    protected boolean isItemChecked(String id) {
        Boolean isChecked = mCheckedItems.get(id);
        if (isChecked == null) return false;
        return isChecked;
    }

    protected void onCheckItem(T item, boolean isChecked) {
        item.setChecked(isChecked);
        updateCount(item.isChecked());
        if (isChecked) {
            mCheckedItems.put(item.getId(), true);
        } else {
            mCheckedItems.remove(item.getId());
        }
    }

    public void updateCount(final boolean isChecked) {
        if (isChecked) {
            if (mCheckedCount < getItemCount()) {
                mCheckedCount++;
            }
        } else {
            if (mCheckedCount != 0) {
                mCheckedCount--;
                if (mCheckedCount == 0 && mActionModeInteractionListener.isActionModeShowing()) {
                    mActionModeInteractionListener.closeActionMode();
                    return;
                }
            }
        }
        startActionMode();
    }

    private void startActionMode() {
        if (!mActionModeInteractionListener.isActionModeShowing()) {
            mActionModeInteractionListener.openActionMode(mCheckedCount);
        } else {
            mActionModeInteractionListener.updateActionMode(mCheckedCount);
        }
    }

    private void finishDelete() {
        mActionModeInteractionListener.closeActionMode();
        mRecyclerView.setEnabled(true);
        deleteState = false;
    }

    public void deleteCheckedView(final AnimationResultListener<T> resultListener) {
        mRecyclerView.setEnabled(false);
        deleteState = true;
        resultListener.onAnimationEnd(getCheckedItems());
        finishDelete();
    }

    public void setClickListener(ShoppistRecyclerView.OnItemClickListener clickListener) {
        this.mItemClickListener = clickListener;
    }
}
