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

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.BaseViewModel;
import com.justplay1.shoppist.models.HeaderViewModel;
import com.justplay1.shoppist.utils.AnimationResultListener;
import com.justplay1.shoppist.view.component.actionmode.ActionModeInteractionListener;
import com.justplay1.shoppist.view.component.animboxes.SelectBoxView;
import com.justplay1.shoppist.view.component.recyclerview.ShoppistRecyclerView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Mkhytar Mkhoian.
 */
public abstract class BaseAdapter<T extends BaseViewModel> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected Context mContext;
    protected boolean isManualSortModeEnable;
    protected ShoppistRecyclerView.OnItemClickListener mItemClickListener;
    protected ActionModeInteractionListener mActionModeInteractionListener;

    protected RecyclerView mRecyclerView;
    protected boolean deleteState = false;
    protected int mCheckedCount = 0;
    protected LinearLayoutManager mLinearLayoutManager;

    public BaseAdapter(Context context, ActionModeInteractionListener listener,
                       RecyclerView recyclerView) {
        setHasStableIds(true);
        this.mContext = context;
        this.mActionModeInteractionListener = listener;
        this.mRecyclerView = recyclerView;
        mLinearLayoutManager = ((LinearLayoutManager) mRecyclerView.getLayoutManager());
    }

    protected abstract List<T> getCheckedItems();

    protected abstract List<T> getItems();

    protected abstract void refreshInvisibleItems();

    public boolean isAllItemsChecked() {
        return getItemCount() == mCheckedCount;
    }

    public int getCheckedCount() {
        return mCheckedCount;
    }

    protected List<Checkable> findVisibleItems() {
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

    public List<T> findInvisibleCheckedItems() {
        findVisibleItems();
        List<T> boxViews = new ArrayList<>();
        for (int i = 0; i < getCheckedItems().size(); i++) {
            if (i < mLinearLayoutManager.findFirstVisibleItemPosition()) {
                boxViews.add(getCheckedItems().get(i));
            } else if (i > mLinearLayoutManager.findLastVisibleItemPosition()) {
                boxViews.add(getCheckedItems().get(i));
            }
        }
        return boxViews;
    }

    public void unCheckAllItems() {
        if (deleteState) return;

        if (mActionModeInteractionListener.isActionModeShowing()) {
            mActionModeInteractionListener.closeActionMode();
        }
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

    protected void checkInvisibleItems(boolean check) {
        for (T item : getItems()) {
            if (item instanceof HeaderViewModel) continue;
            if (item.isChecked() != check) {
                item.setChecked(check);
            }
        }
    }

    protected void onCheckItem(T item, boolean isChecked) {
        item.setChecked(isChecked);
        updateCount(item.isChecked());
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

    protected void finishDelete() {
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

    public boolean isManualSortModeEnable() {
        return isManualSortModeEnable;
    }

    public void setManualSortModeEnable(boolean isManualSortEnable) {
        this.isManualSortModeEnable = isManualSortEnable;
    }

    public ShoppistRecyclerView.OnItemClickListener getClickListener() {
        return mItemClickListener;
    }

    public void setClickListener(ShoppistRecyclerView.OnItemClickListener clickListener) {
        this.mItemClickListener = clickListener;
    }
}
