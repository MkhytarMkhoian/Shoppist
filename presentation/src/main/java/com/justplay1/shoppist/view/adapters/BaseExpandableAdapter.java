package com.justplay1.shoppist.view.adapters;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemAdapter;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.BaseViewModel;
import com.justplay1.shoppist.models.HeaderViewModel;
import com.justplay1.shoppist.models.ItemType;
import com.justplay1.shoppist.models.Priority;
import com.justplay1.shoppist.models.SortType;
import com.justplay1.shoppist.preferences.ShoppistPreferences;
import com.justplay1.shoppist.view.component.actionmode.ActionModeOpenCloseListener;
import com.justplay1.shoppist.view.component.animboxes.SelectBoxEventListener;
import com.justplay1.shoppist.view.component.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseItemHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mkhytar on 10.09.2015.
 */
public abstract class BaseExpandableAdapter<T extends BaseViewModel, GVH extends RecyclerView.ViewHolder, CVH extends RecyclerView.ViewHolder>
        extends BaseAdapter<T>
        implements ExpandableItemAdapter<GVH, CVH>, SelectBoxEventListener {

    protected List<Pair<HeaderViewModel, List<T>>> mData;
    protected ShoppistRecyclerView.OnHeaderClickListener mHeaderClickListener;

    protected
    @ColorInt
    int mNoPriority;
    protected
    @ColorInt
    int mHighPriority;
    protected
    @ColorInt
    int mLowPriority;
    protected
    @ColorInt
    int mMediumPriority;

    protected
    @SortType
    int mSort = SortType.SORT_BY_NAME;

    public BaseExpandableAdapter(Context context, ActionModeOpenCloseListener listener,
                                 RecyclerView recyclerView) {
        super(context, listener, recyclerView);

        mNoPriority = context.getResources().getColor(android.R.color.transparent);
        mHighPriority = context.getResources().getColor(R.color.red_color);
        mLowPriority = context.getResources().getColor(R.color.green_500);
        mMediumPriority = context.getResources().getColor(R.color.orange_500);

        mData = new ArrayList<>(1);
    }

    public List<Pair<HeaderViewModel, List<T>>> getData() {
        return mData;
    }

    public void setData(List<Pair<HeaderViewModel, List<T>>> data) {
        this.mData = data;
    }

    public void invalidData() {
        mData.clear();
        notifyItemRangeRemoved(0, getItemCount());
    }

    public void setSort(@SortType int sort) {
        this.mSort = sort;
    }

    protected void setPriorityTextColor(@Priority int priority, TextView view) {
        switch (priority) {
            case Priority.HIGH:
                view.setTextColor(mHighPriority);
                break;
            case Priority.LOW:
                view.setTextColor(mLowPriority);
                break;
            case Priority.MEDIUM:
                view.setTextColor(mMediumPriority);
                break;
            case Priority.NO_PRIORITY:
                view.setTextColor(mNoPriority);
                break;
        }
    }

    protected void setPriorityBackgroundColor(@Priority int priority, View view) {
        switch (priority) {
            case Priority.HIGH:
                view.setBackgroundColor(mHighPriority);
                break;
            case Priority.LOW:
                view.setBackgroundColor(mLowPriority);
                break;
            case Priority.MEDIUM:
                view.setBackgroundColor(mMediumPriority);
                break;
            case Priority.NO_PRIORITY:
                view.setBackgroundColor(mNoPriority);
                break;
        }
    }

    @Override
    protected void refreshInvisibleItems() {
        if (mLinearLayoutManager.findFirstVisibleItemPosition() > 0) {
            notifyItemRangeChanged(0, mLinearLayoutManager.findFirstVisibleItemPosition());
        }
        notifyItemRangeChanged(mLinearLayoutManager.findLastVisibleItemPosition() + 1,
                (getGroupCount() + getChildItemsCount()) - mLinearLayoutManager.findLastVisibleItemPosition());
    }

    @Override
    public void onCheck(BaseItemHolder holder) {
        T item = getChildItem(holder.groupPosition, holder.childPosition);
        item.setChecked(holder.selectBox.isChecked());
        addToChecked(item.getId(), item.isChecked());
        setCount(item.isChecked());
    }

    private List<T> getItems(boolean checked) {
        List<T> items = new ArrayList<>();
        for (Pair<HeaderViewModel, List<T>> pair : mData) {
            for (T item : pair.second) {
                if (checked) {
                    if (item.isChecked()) {
                        items.add(item);
                    }
                } else {
                    items.add(item);
                }
            }
        }
        return items;
    }

    public int getChildItemsCount() {
        if (mData == null) return 0;
        int count = 0;
        for (Pair<HeaderViewModel, List<T>> pair : mData) {
            count += pair.second.size();
        }
        return count;
    }

    public List<T> getItems() {
        return getItems(false);
    }

    public List<T> getCheckedItems() {
        return getItems(true);
    }

    public ShoppistRecyclerView.OnHeaderClickListener getHeaderClickListener() {
        return mHeaderClickListener;
    }

    public void setHeaderClickListener(ShoppistRecyclerView.OnHeaderClickListener headerClickListener) {
        this.mHeaderClickListener = headerClickListener;
    }

    public int getGroupItemCount(int groupPosition) {
        if (groupPosition < 0 || groupPosition >= getGroupCount()) {
            throw new IndexOutOfBoundsException("groupPosition = " + groupPosition);
        }
        return mData.get(groupPosition).second.size();
    }

    public HeaderViewModel getGroupItem(int groupPosition) {
        if (groupPosition < 0 || groupPosition >= getGroupCount()) {
            throw new IndexOutOfBoundsException("groupPosition = " + groupPosition);
        }
        return mData.get(groupPosition).first;
    }

    public T getChildItem(int groupPosition, int childPosition) {
        if (groupPosition < 0 || groupPosition >= getGroupCount()) {
            throw new IndexOutOfBoundsException("groupPosition = " + groupPosition);
        }

        final List<T> children = mData.get(groupPosition).second;

        if (childPosition < 0 || childPosition >= children.size()) {
            throw new IndexOutOfBoundsException("childPosition = " + childPosition);
        }
        return children.get(childPosition);
    }

    @Override
    public int getGroupCount() {
        if (mData == null) return 0;
        return mData.size();
    }

    @Override
    public void onBindGroupViewHolder(GVH holder, int groupPosition, int viewType, List<Object> payloads) {
        onBindGroupViewHolder(holder, groupPosition, viewType);
    }

    @Override
    public void onBindChildViewHolder(CVH holder, int groupPosition, int childPosition, int viewType, List<Object> payloads) {
        onBindChildViewHolder(holder, groupPosition, childPosition, viewType);
    }

    @Override
    public int getChildCount(int groupPosition) {
        return mData.get(groupPosition).second.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return getGroupItem(groupPosition).hashCode();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return getChildItem(groupPosition, childPosition).hashCode();
    }

    protected int findFirstSectionItem(int position) {
        HeaderViewModel item = getGroupItem(position);

        if (item.getItemType() == ItemType.CART_HEADER) {
            throw new IllegalStateException("section item is expected");
        }

        while (position > 0) {
            HeaderViewModel prevItem = getGroupItem(position - 1);
            if (prevItem.getItemType() == ItemType.CART_HEADER) {
                break;
            }
            position -= 1;
        }
        return position;
    }

    protected int findLastSectionItem(int position) {
        HeaderViewModel item = getGroupItem(position);

        if (item.getItemType() == ItemType.CART_HEADER) {
            throw new IllegalStateException("section item is expected");
        }
        final int lastIndex = getGroupCount() - 1;

        while (position < lastIndex) {
            HeaderViewModel nextItem = getGroupItem(position + 1);

            if (nextItem.getItemType() == ItemType.CART_HEADER) {
                break;
            }
            position += 1;
        }
        return position;
    }


    /**
     * This method will not be called.
     * Override {@link #onCreateGroupViewHolder(android.view.ViewGroup, int)} and
     * {@link #onCreateChildViewHolder(android.view.ViewGroup, int)} instead.
     *
     * @param parent   not used
     * @param viewType not used
     * @return null
     */
    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    /**
     * This method will not be called.
     * Override {@link #getGroupId(int)} and {@link #getChildId(int, int)} instead.
     *
     * @param position not used
     * @return {@link RecyclerView#NO_ID}
     */
    @Override
    public final long getItemId(int position) {
        return RecyclerView.NO_ID;
    }

    /**
     * This method will not be called.
     * Override {@link #getGroupItemViewType(int)} and {@link #getChildItemViewType(int, int)} instead.
     *
     * @param position not used
     * @return 0
     */
    @Override
    public final int getItemViewType(int position) {
        return 0;
    }

    /**
     * This method will not be called.
     * Override {@link #getGroupCount()} and {@link #getChildCount(int)} instead.
     *
     * @return 0
     */
    @Override
    public final int getItemCount() {
        return 0;
    }

    /**
     * This method will not be called.
     * Override {@link #onBindGroupViewHolder(RecyclerView.ViewHolder, int, int)} ()} and
     * {@link #onBindChildViewHolder(RecyclerView.ViewHolder, int, int, int)} instead.
     *
     * @param holder   not used
     * @param position not used
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    /**
     * Override this method if need to customize the behavior.
     * {@inheritDoc}
     */
    @Override
    public boolean onHookGroupExpand(int groupPosition, boolean fromUser) {
        return true;
    }

    /**
     * Override this method if need to customize the behavior.
     * {@inheritDoc}
     */
    @Override
    public boolean onHookGroupCollapse(int groupPosition, boolean fromUser) {
        return true;
    }
}
