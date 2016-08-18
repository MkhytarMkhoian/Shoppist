package com.justplay1.shoppist.view.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.BaseViewModel;
import com.justplay1.shoppist.models.HeaderViewModel;
import com.justplay1.shoppist.utils.AnimationResultListener;
import com.justplay1.shoppist.view.component.actionmode.ActionModeOpenCloseListener;
import com.justplay1.shoppist.view.component.animboxes.SelectBoxView;
import com.justplay1.shoppist.view.component.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseItemHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Mkhytar on 02.09.2015.
 */
public abstract class BaseAdapter<T extends BaseViewModel> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected Context mContext;
    protected boolean isManualSortModeEnable;
    protected ShoppistRecyclerView.OnItemClickListener mItemClickListener;
    protected ActionModeOpenCloseListener mActionModeOpenCloseListener;

    protected RecyclerView mRecyclerView;
    protected boolean deleteState = false;
    protected int mCheckedCount = 0;
    protected int mPreviousScrollY = 0;
    protected LinearLayoutManager mLinearLayoutManager;
    protected List<BaseItemHolder> mVisibleItems = new ArrayList<>();
    protected Map<String, Boolean> mCheckedItems = new HashMap<>();

    public BaseAdapter(Context context, ActionModeOpenCloseListener listener,
                       RecyclerView recyclerView) {
        this.mContext = context;
        this.mActionModeOpenCloseListener = listener;
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

    protected void findVisibleItems() {
        final int firstPosition = mLinearLayoutManager.findFirstVisibleItemPosition();
        final int lastPosition = mLinearLayoutManager.findLastVisibleItemPosition();
        clearCheckedItems();

        RecyclerView.ViewHolder holder;
        for (int i = firstPosition; i <= lastPosition; i++) {
            View item = mLinearLayoutManager.findViewByPosition(i);
            if (item == null) continue;

            holder = mRecyclerView.getChildViewHolder(item);
            if (holder instanceof BaseItemHolder) {
                mVisibleItems.add((BaseItemHolder) holder);
            }
        }
    }

    public List<SelectBoxView> findVisibleCheckedBoxes() {
        List<SelectBoxView> boxViews = new ArrayList<>();
        for (int i = 0; i < mRecyclerView.getChildCount(); ++i) {
            SelectBoxView boxView = (SelectBoxView) mRecyclerView.getChildAt(i).findViewById(R.id.select_box);
            if (boxView == null) continue;
            if (boxView.isChecked()) {
                boxViews.add(boxView);
            }
        }
        return boxViews;
    }

    public List<T> findInvisibleCheckedBoxes() {
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

    public void unCheckAllItems(boolean animation) {
        if (deleteState) return;

        mActionModeOpenCloseListener.closeActionMode();
        findVisibleItems();
        for (int i = 0; i < mVisibleItems.size(); i++) {
            if (mVisibleItems.get(i).selectBox.isChecked()) {
                mVisibleItems.get(i).selectBox.setChecked(false, animation);
            }
        }
        checkInvisibleItems(false);
        refreshInvisibleItems();
    }

    public void checkAllItems() {
        findVisibleItems();
        for (BaseItemHolder holder : mVisibleItems) {
            if (!holder.selectBox.isChecked()) {
                holder.selectBox.setChecked(true, true);
            }
        }
        checkInvisibleItems(true);
        mCheckedCount = getCheckedItems().size();
        mActionModeOpenCloseListener.openActionMode(mCheckedCount);
        refreshInvisibleItems();
    }

    public void addToChecked(String id, boolean isChecked) {
        if (isChecked) {
            mCheckedItems.put(id, true);
        } else {
            deleteItemFromChecked(id);
        }
    }

    public void deleteItemFromChecked(String id) {
        mCheckedItems.remove(id);
    }

    public boolean isItemChecked(String id) {
        Boolean isChecked = mCheckedItems.get(id);
        if (isChecked == null) return false;
        return isChecked;
    }

    protected void checkInvisibleItems(boolean check) {
        for (T item : getItems()) {
            if (item instanceof HeaderViewModel) continue;
            if (item.isChecked() != check) {
                item.setChecked(check);
                mCheckedItems.put(item.getId(), check);
            }
        }
    }

    public void clearCheckedItems() {
        mVisibleItems.clear();
        mCheckedItems.clear();
    }

    public boolean isScrolling(int newScrollY) {
        if (newScrollY > mPreviousScrollY) {
            mPreviousScrollY = newScrollY;
        }
        return true;
    }

    // Set selected count
    public void setCount(final boolean isChecked) {
        if (isChecked) {
            if (mCheckedCount < getItemCount()) {
                mCheckedCount++;
            }
        } else {
            if (mCheckedCount != 0) {
                mCheckedCount--;
            }
        }
        mActionModeOpenCloseListener.updateActionMode(mCheckedCount);
    }

    protected void finishDelete() {
        mActionModeOpenCloseListener.closeActionMode();
        mRecyclerView.setEnabled(true);
        deleteState = false;
    }

    public void deleteCheckedView(final AnimationResultListener<T> resultListener) {
        mRecyclerView.setEnabled(false);
        deleteState = true;
        resultListener.onAnimationEnd(getCheckedItems());
        finishDelete();
    }

    public void deleteFromVisibleBoxes(BaseItemHolder holder) {
        mVisibleItems.remove(holder);
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

    public void onLongClick(View listItem) {
        SelectBoxView selectBox = (SelectBoxView) listItem.findViewById(R.id.select_box);
        if (selectBox == null) return;

        if (selectBox.isChecked()) {
            selectBox.setChecked(false, true);
        } else {
            selectBox.setChecked(true, true);
        }
    }

    public void onMovedToScrapHeap(RecyclerView.ViewHolder holder) {
        SelectBoxView selectBox = (SelectBoxView) holder.itemView.findViewById(R.id.select_box);
        if (selectBox == null) return;

        if (isScrolling(mLinearLayoutManager.findFirstVisibleItemPosition())) {
            deleteFromVisibleBoxes((BaseItemHolder) holder);
            selectBox.onMovedToScrapHeap();
        }
    }
}
