package com.justplay1.shoppist.ui.views.animboxes;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.BaseModel;
import com.justplay1.shoppist.models.Header;
import com.justplay1.shoppist.utils.AnimationResultListener;
import com.justplay1.shoppist.ui.views.recyclerview.holders.BaseItemHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mkhytar on 06.09.2015.
 */
public abstract class BaseSelectItemsManager<T extends BaseModel> {

    protected Context mContext;
    protected ActionMode.Callback mCallback;
    protected ActionMode mActionMode;
    protected RecyclerView mRecyclerView;
    protected int mCounter;
    protected boolean deleteState = false;
    protected boolean isActionModeShowing;
    protected int mCheckedCount = 0;
    protected int mPreviousScrollY = 0;
    protected LinearLayoutManager mLinearLayoutManager;
    protected List<BaseItemHolder> mVisibleItems = new ArrayList<>();
    protected Map<String, Boolean> mCheckedItems = new HashMap<>();

    public BaseSelectItemsManager(Context context, ActionMode.Callback callback, RecyclerView recyclerView) {
        mContext = context;
        mCallback = callback;
        mRecyclerView = recyclerView;
        mLinearLayoutManager = ((LinearLayoutManager) mRecyclerView.getLayoutManager());
    }

    protected abstract int getItemCount();

    protected abstract List<T> getCheckedItems();

    protected abstract List<T> getItems();

    protected abstract void refreshInvisibleItems();

    public abstract void deleteCheckedView(final AnimationResultListener<T> resultListener);

    public boolean isAllItemsChecked() {
        return getItemCount() == getCheckedCount();
    }

    protected void findVisibleItems() {
        final int firstPosition = mLinearLayoutManager.findFirstVisibleItemPosition();
        final int lastPosition = mLinearLayoutManager.findLastVisibleItemPosition();
        release();

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

        closeActionMode();
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
        setActionMode();
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
            if (item instanceof Header) continue;
            if (item.isChecked() != check) {
                item.setChecked(check);
                mCheckedItems.put(item.getId(), check);
            }
        }
    }

    public void deleteFromVisibleBoxes(BaseItemHolder holder) {
        mVisibleItems.remove(holder);
    }

    public void updateActionMode(boolean isChecked) {
        setCount(isChecked);
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
        setActionMode();
    }

    protected void finishDelete() {
        closeActionMode();
        mRecyclerView.setEnabled(true);
        deleteState = false;
    }

    // Show/Hide action mode
    protected void setActionMode() {
        if (mCheckedCount > 0) {
            if (!isActionModeShowing) {
                mActionMode = ((AppCompatActivity) mContext).startSupportActionMode(mCallback);
                isActionModeShowing = true;
            }

        } else if (mActionMode != null) {
            mActionMode.finish();
            mActionMode = null;
            isActionModeShowing = false;
        }

        // Set action mode title
        if (mActionMode != null) {
            mActionMode.setTitle(String.valueOf(mCheckedCount));
            mActionMode.invalidate();
        }
    }

    public void closeActionMode() {
        if (mActionMode != null) {
            mActionMode.finish();
            mActionMode = null;
            isActionModeShowing = false;
            mCheckedCount = 0;
        }
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

    public boolean isActionModeShowing() {
        return isActionModeShowing;
    }

    public int getCheckedCount() {
        return mCheckedCount;
    }

    public void closeActionMode(boolean deleteState) {
        this.deleteState = deleteState;
        closeActionMode();
        this.deleteState = false;
    }

    public void release() {
        mVisibleItems.clear();
        mCheckedItems.clear();
        mCounter = 0;
    }

    public boolean isScrolling(int newScrollY) {
        if (newScrollY > mPreviousScrollY) {
            mPreviousScrollY = newScrollY;
        }
        return true;
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
