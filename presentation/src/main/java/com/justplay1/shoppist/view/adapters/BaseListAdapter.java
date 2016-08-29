package com.justplay1.shoppist.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.justplay1.shoppist.models.BaseViewModel;
import com.justplay1.shoppist.models.HeaderViewModel;
import com.justplay1.shoppist.view.component.actionmode.ActionModeOpenCloseListener;
import com.justplay1.shoppist.view.component.animboxes.SelectBoxEventListener;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseItemHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mkhitar on 06.08.2015.
 */
public abstract class BaseListAdapter<T extends BaseViewModel>
        extends BaseAdapter<T> implements SelectBoxEventListener {

    protected List<T> mData;

    public BaseListAdapter(Context context, ActionModeOpenCloseListener listener,
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
        return mData.size();
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


    @Override
    public void onCheck(BaseItemHolder holder) {
//        T item = getItem(holder.childPosition);
//        item.setChecked(holder.selectBox.isChecked());
//        addToChecked(item.getId(), item.isChecked());
//        setCount(item.isChecked());
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
