package com.justplay1.shoppist.adapters;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.BaseModel;
import com.justplay1.shoppist.models.Header;
import com.justplay1.shoppist.models.ItemType;
import com.justplay1.shoppist.models.Priority;
import com.justplay1.shoppist.models.Product;
import com.justplay1.shoppist.models.ShoppingList;
import com.justplay1.shoppist.models.ShoppingListItem;
import com.justplay1.shoppist.models.Status;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.ui.views.animboxes.BaseSelectItemsManager;
import com.justplay1.shoppist.ui.views.animboxes.SelectBoxEventListener;
import com.justplay1.shoppist.ui.views.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.ui.views.recyclerview.holders.BaseItemHolder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Created by Mkhytar on 10.09.2015.
 */
public abstract class BaseExpandableAdapter<T extends BaseModel, GVH extends RecyclerView.ViewHolder, CVH extends RecyclerView.ViewHolder> extends BaseAdapter<T>
        implements ExpandableItemAdapter<GVH, CVH>, SelectBoxEventListener, BaseAdapter.SortableAdapter {

    protected List<Pair<Header, List<T>>> mData;
    protected BaseSelectItemsManager<T> mSelectItemsManager;
    protected ShoppistRecyclerView.OnHeaderClickListener mHeaderClickListener;
    protected RecyclerViewExpandableItemManager mExpandableItemManager;

    public BaseExpandableAdapter(Context context, Cursor cursor, ContentObserver changeObserver) {
        super(context, cursor, changeObserver);
        mData = new ArrayList<>();
    }

    public abstract boolean isManualSortEnable();

    public void invalidData(){
        mData.clear();
        notifyItemRangeRemoved(0, getItemCount());
    }

    @Override
    protected void getDataFromCursor() {
        final List<T> data = new ArrayList<>();
        if (!mCursor.isClosed() && mCursor.moveToFirst()) {
            mData.clear();
            do {
                T item = getModelItem(mCursor);
                data.add(item);
            } while (mCursor.moveToNext());
        } else {
            mData.clear();
        }
        sort(data, mDefaultSort);
    }

    public void sort() {
        if (mData == null || mData.isEmpty()) return;

        final List<T> data = new ArrayList<>();
        for (Pair<Header, List<T>> pair : mData) {
            for (T item : pair.second) {
                data.add(item);
            }
        }
        mData.clear();
        sort(data, mDefaultSort);
    }

    protected void sort(final List<T> data, final int sortType) {
        if (data == null || data.isEmpty()) return;

        final Class<?> clazz = data.get(0).getClass();
        if (clazz.isAssignableFrom(ShoppingList.class)
                || clazz.isAssignableFrom(Product.class)) {
            sort(sortType, data, true);
            if (isManualSortEnable()) {
                sortByManually();
            }
            return;
        }

        final List<T> done = new ArrayList<>();
        final List<T> noDone = new ArrayList<>();
        for (T item : data) {
            if (item.getStatus() == Status.DONE) {
                done.add(item);
            } else {
                noDone.add(item);
            }
        }
        sort(sortType, noDone, true);
        if (isManualSortEnable()) {
            sortByManually();
        }
        sort(sortType, done, false);

        if (noDone.size() > 0) {
            Header header = new Header();
            if (clazz.isAssignableFrom(ShoppingListItem.class) && ShoppistPreferences.isShowGoodsHeader()) {
                header.setName(mContext.getString(R.string.goods).toUpperCase(Locale.getDefault()));
                header.setShowExpandIndicator(false);
                double totalPrice = 0;
                for (T item : noDone) {
                    ShoppingListItem listItem = ((ShoppingListItem) item);
                    totalPrice = totalPrice + (listItem.getPrice() * listItem.getQuantity());
                }
                header.setTotalPrice(ShoppistUtils.roundDouble(totalPrice, 2));
                header.setItemType(ItemType.CART_HEADER);
                mData.add(0, Pair.create(header, (List<T>) new ArrayList<T>()));
            }
        }

        if (done.size() > 0) {
            Header header = null;
            if (clazz.isAssignableFrom(ShoppingListItem.class)) {
                header = new Header();
                header.setName(mContext.getString(R.string.shopping_cart).toUpperCase(Locale.getDefault()));
                double totalPrice = 0;
                for (T item : done) {
                    ShoppingListItem listItem = ((ShoppingListItem) item);
                    totalPrice = totalPrice + (listItem.getPrice() * listItem.getQuantity());
                }
                header.setTotalPrice(ShoppistUtils.roundDouble(totalPrice, 2));
            }
            if (header != null) {
                header.setItemType(ItemType.CART_HEADER);
                mData.add(Pair.create(header, done));
            }
        }
    }

    private void sort(int sortType, List<T> data, boolean withHeaders) {
        switch (sortType) {
            case SORT_BY_NAME:
                sortByName(data, withHeaders);
                break;
            case SORT_BY_PRIORITY:
                sortByPriority(data, withHeaders);
                break;
            case SORT_BY_CATEGORIES:
                sortByCategory(data, withHeaders);
                break;
            case SORT_BY_TIME_CREATED:
                sortByTimeCreated(data, withHeaders);
                break;
        }
    }

    protected void sortByManually(List<Pair<Header, List<T>>> data) {
        for (Pair<Header, List<T>> pair : data) {
            Collections.sort(pair.second, new Comparator<T>() {
                @Override
                public int compare(T lhs, T rhs) {
                    return lhs.getPosition() < rhs.getPosition() ? -1 : (lhs.getPosition() == rhs.getPosition() ? 0 : 1);
                }
            });
        }
    }

    protected void sortByManually() {
        sortByManually(mData);
    }

    private void sortItemsByName() {
        for (Pair<Header, List<T>> pair : mData) {
            sortByName(pair.second, false);
        }
    }

    private void sortByTimeCreated(List<T> data, boolean withHeaders) {
        Collections.sort(data, new Comparator<T>() {
            @Override
            public int compare(T lhs, T rhs) {
                if (lhs.getTimeCreated() < rhs.getTimeCreated()) {
                    return 1;
                } else if (lhs.getTimeCreated() > rhs.getTimeCreated()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        if (!withHeaders) return;

        final Calendar currentTime = Calendar.getInstance();
        final Calendar itemTime = Calendar.getInstance();

        final int day = currentTime.get(Calendar.DAY_OF_YEAR);

        long flag = -1;
        List<T> items = null;
        for (int i = 0; i < data.size(); i++) {
            itemTime.setTimeInMillis(data.get(i).getTimeCreated());
            final int itemDay = itemTime.get(Calendar.DAY_OF_YEAR);
            final int diffDay = day - itemDay;

            if (flag != diffDay) {
                flag = diffDay;
                Header header = new Header();
                if (flag < 1) {
                    header.setName(mContext.getString(R.string.today));
                } else if (flag == 1) {
                    header.setName(mContext.getString(R.string.yesterday));
                } else {
                    header.setName(flag + mContext.getString(R.string.days_ago));
                }
                header.setItemType(ItemType.HEADER_ITEM);

                items = new ArrayList<>();
                mData.add(Pair.create(header, items));
            }
            if (items != null) {
                items.add(data.get(i));
            }
        }
        sortItemsByName();
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
        List<T> items = null;
        for (int i = 0; i < data.size(); i++) {
            String firstCharacter = ShoppistUtils.getFirstCharacter(data.get(i).getName()).toUpperCase();
            if (!headerName.equals(firstCharacter)) {
                headerName = firstCharacter;
                Header header = new Header();
                header.setName(headerName);
                header.setItemType(ItemType.HEADER_ITEM);

                items = new ArrayList<>();
                mData.add(Pair.create(header, items));
            }
            if (items != null) {
                items.add(data.get(i));
            }
        }
    }

    private void sortByPriority(List<T> data, boolean withHeaders) {
        Collections.sort(data, new Comparator<T>() {
            @Override
            public int compare(T lhs, T rhs) {
                if (lhs.getPriority().ordinal() < rhs.getPriority().ordinal()) {
                    return 1;
                } else if (lhs.getPriority().ordinal() > rhs.getPriority().ordinal()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        if (!withHeaders) return;

        final String noPriority = mContext.getResources().getString(R.string.no_priority);
        final String lowPriority = mContext.getResources().getString(R.string.low);
        final String mediumPriority = mContext.getResources().getString(R.string.medium_priority);
        final String highPriority = mContext.getResources().getString(R.string.high);
        List<T> items = null;
        int priority = -1;

        for (int i = 0; i < data.size(); i++) {
            final Priority p = data.get(i).getPriority();
            if (priority != p.ordinal()) {
                priority = p.ordinal();
                Header header = new Header();
                switch (priority) {
                    case 0:
                        header.setName(noPriority);
                        break;
                    case 1:
                        header.setName(lowPriority);
                        break;
                    case 2:
                        header.setName(mediumPriority);
                        break;
                    case 3:
                        header.setName(highPriority);
                        break;
                }
                header.setPriority(p);
                header.setItemType(ItemType.HEADER_ITEM);

                items = new ArrayList<>();
                mData.add(Pair.create(header, items));
            }
            if (items != null) {
                items.add(data.get(i));
            }
        }
        sortItemsByName();
    }

    private void sortByCategory(List<T> data, boolean withHeaders) {
        if (ShoppistPreferences.isManualSortEnableForCategories()) {
            Collections.sort(data, new Comparator<T>() {
                @Override
                public int compare(T lhs, T rhs) {
                    return lhs.getCategory().getPosition() < rhs.getCategory().getPosition() ? -1
                            : (lhs.getCategory().getPosition() == rhs.getCategory().getPosition() ? 0 : 1);
                }
            });
        } else {
            Collections.sort(data, new Comparator<T>() {
                @Override
                public int compare(T lhs, T rhs) {
                    return lhs.getCategory().getName().compareToIgnoreCase(rhs.getCategory().getName());
                }
            });
        }

        if (!withHeaders) return;

        String headerName = "";
        List<T> items = null;
        for (int i = 0; i < data.size(); i++) {
            String name = data.get(i).getCategory().getName();
            if (!headerName.equals(name)) {
                headerName = name;
                Header header = new Header();
                header.setName(headerName);
                header.setItemType(ItemType.HEADER_ITEM);

                items = new ArrayList<>();
                mData.add(Pair.create(header, items));
            }
            if (items != null) {
                items.add(data.get(i));
            }
        }
        sortItemsByName();
    }

    protected void expandAll() {
        if (mExpandableItemManager != null) {
            mExpandableItemManager.expandAll();
        }
    }

    @Override
    public void onCheck(BaseItemHolder holder) {
        T item = getChildItem(holder.groupPosition, holder.childPosition);
        item.setChecked(holder.selectBox.isChecked());
        if (mSelectItemsManager == null) return;
        mSelectItemsManager.addToChecked(item.getId(), item.isChecked());
        mSelectItemsManager.updateActionMode(item.isChecked());
    }

    private List<T> getItems(boolean checked) {
        List<T> items = new ArrayList<>();
        for (Pair<Header, List<T>> pair : mData) {
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
        for (Pair<Header, List<T>> pair : mData) {
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

    @Override
    public void sortByTimeCreated() {
        mDefaultSort = SORT_BY_TIME_CREATED;
        sort();
        notifyDataSetChanged();
        expandAll();
    }

    @Override
    public void sortByName() {
        mDefaultSort = SORT_BY_NAME;
        sort();
        notifyDataSetChanged();
        expandAll();
    }

    @Override
    public void sortByPriority() {
        mDefaultSort = SORT_BY_PRIORITY;
        sort();
        notifyDataSetChanged();
        expandAll();
    }

    @Override
    public void sortByCategory() {
        mDefaultSort = SORT_BY_CATEGORIES;
        sort();
        notifyDataSetChanged();
        expandAll();
    }

    public void setSelectItemsManager(BaseSelectItemsManager<T> manager) {
        this.mSelectItemsManager = manager;
    }

    public void setRecyclerViewExpandableItemManager(RecyclerViewExpandableItemManager manager) {
        this.mExpandableItemManager = manager;
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

    public Header getGroupItem(int groupPosition) {
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
        Header item = getGroupItem(position);

        if (item.getItemType() == ItemType.CART_HEADER) {
            throw new IllegalStateException("section item is expected");
        }

        while (position > 0) {
            Header prevItem = getGroupItem(position - 1);
            if (prevItem.getItemType() == ItemType.CART_HEADER) {
                break;
            }
            position -= 1;
        }
        return position;
    }

    protected int findLastSectionItem(int position) {
        Header item = getGroupItem(position);

        if (item.getItemType() == ItemType.CART_HEADER) {
            throw new IllegalStateException("section item is expected");
        }
        final int lastIndex = getGroupCount() - 1;

        while (position < lastIndex) {
            Header nextItem = getGroupItem(position + 1);

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
