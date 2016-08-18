package com.justplay1.shoppist.adapters;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;

import com.justplay1.shoppist.models.BaseModel;
import com.justplay1.shoppist.models.Header;
import com.justplay1.shoppist.models.ItemType;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.ui.views.animboxes.BaseSelectItemsManager;
import com.justplay1.shoppist.ui.views.animboxes.SelectBoxEventListener;
import com.justplay1.shoppist.ui.views.recyclerview.holders.BaseItemHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Mkhitar on 06.08.2015.
 */
public abstract class BaseWithoutHeaderAdapter<T extends BaseModel>
        extends BaseAdapter<T> implements SelectBoxEventListener, BaseAdapter.SortableAdapter {

    protected List<T> mData;
    protected BaseSelectItemsManager<T> mSelectItemsManager;

    public BaseWithoutHeaderAdapter(Context context, Cursor cursor, ContentObserver changeObserver) {
        super(context, cursor, changeObserver);
        mData = new ArrayList<>();
    }

    public abstract boolean isManualSortEnable();

    public void setSelectItemsManager(BaseSelectItemsManager<T> manager) {
        this.mSelectItemsManager = manager;
    }

    @Override
    public int getItemCount() {
        if (mData == null) return 0;
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getItemType();
    }

    public void remove(T item) {
        if (mData != null && mData.contains(item)) {
            mData.remove(item);
        }
    }

    public void removeAll(List<T> items) {
        if (mData != null) {
            mData.removeAll(items);
        }
    }

    public int getCountWithoutHeaders() {
        if (mData == null) return 0;
        return mData.size();
    }

    public T getItem(int position) {
        if (mData == null) return null;
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (mData == null) return 0;
        return mData.get(position).hashCode();
    }

    @Override
    public void onCheck(BaseItemHolder holder) {
        T item = getItem(holder.getLayoutPosition());
        item.setChecked(holder.selectBox.isChecked());
        if (mSelectItemsManager == null) return;
        mSelectItemsManager.addToChecked(item.getId(), item.isChecked());
        mSelectItemsManager.updateActionMode(item.isChecked());
    }

    public List<T> getCheckedItems() {
        List<T> items = new ArrayList<>();
        for (T item : mData) {
            if (item instanceof Header) continue;
            if (item.isChecked()) {
                items.add(item);
            }
        }
        return items;
    }

    public List<T> getItems() {
        return mData;
    }

    public void sort() {
        sort(mDefaultSort);
    }

    private void sort(int sortType) {
        if (mData == null || mData.isEmpty()) return;
        sort(sortType, mData, false);
    }

    @Override
    protected void getDataFromCursor() {
        if (!mCursor.isClosed() && mCursor.moveToFirst()) {
            mData.clear();
            do {
                T item = getModelItem(mCursor);
                if (!item.isDelete()) {
                    mData.add(item);
                }
            } while (mCursor.moveToNext());
        } else {
            mData.clear();
        }

        if (isManualSortEnable()) {
            sortByManually();
        } else {
            sort();
        }
    }

    protected void sortByManually(List<T> data) {
        Collections.sort(data, new Comparator<T>() {
            @Override
            public int compare(T lhs, T rhs) {
                return lhs.getPosition() < rhs.getPosition() ? -1 : (lhs.getPosition() == rhs.getPosition() ? 0 : 1);
            }
        });
    }

    protected void sortByManually() {
        sortByManually(mData);
    }

    private void sort(int sortType, List<T> data, boolean withHeaders) {
        switch (sortType) {
            case SORT_BY_NAME:
                sortByName(data, withHeaders);
                break;
        }
    }

    private void sortByName(List<T> data, boolean withHeaders) {
        Collections.sort(data, new Comparator<T>() {
            @Override
            public int compare(T lhs, T rhs) {
                return lhs.getName().compareToIgnoreCase(rhs.getName());
            }
        });
        if (!withHeaders) return;

        String headerName = "";
        for (int i = 0; i < data.size(); i++) {
            String firstCharacter = ShoppistUtils.getFirstCharacter(data.get(i).getName()).toUpperCase();
            if (!headerName.equals(firstCharacter)) {
                headerName = firstCharacter;
                Header header = new Header();
                header.setName(headerName);
                header.setItemType(ItemType.HEADER_ITEM);
                data.add(i, (T) header);
            }
        }
    }

    @Override
    public void sortByTimeCreated() {
        mDefaultSort = SORT_BY_TIME_CREATED;
        sort();
        notifyDataSetChanged();
    }

    @Override
    public void sortByName() {
        mDefaultSort = SORT_BY_NAME;
        sort();
        notifyDataSetChanged();
    }

    @Override
    public void sortByPriority() {
        mDefaultSort = SORT_BY_PRIORITY;
        sort();
        notifyDataSetChanged();
    }

    @Override
    public void sortByCategory() {
        mDefaultSort = SORT_BY_CATEGORIES;
        sort();
        notifyDataSetChanged();
    }
}
