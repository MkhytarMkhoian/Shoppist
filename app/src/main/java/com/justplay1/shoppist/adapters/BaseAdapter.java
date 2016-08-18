package com.justplay1.shoppist.adapters;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;

import com.justplay1.shoppist.ui.views.recyclerview.ShoppistRecyclerView;


/**
 * Created by Mkhytar on 02.09.2015.
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int SORT_BY_NAME = 1;
    public static final int SORT_BY_PRIORITY = 2;
    public static final int SORT_BY_CATEGORIES = 3;
    public static final int SORT_BY_TIME_CREATED = 4;

    protected Cursor mCursor;
    protected Context mContext;
    protected int mDefaultSort;
    protected ContentObserver mChangeObserver;
    protected DataSetObserver mDataSetObserver;
    protected boolean isManualSortModeEnable;
    protected ShoppistRecyclerView.OnItemClickListener mItemClickListener;

    public BaseAdapter(Context context, Cursor cursor, ContentObserver changeObserver) {
        mContext = context;
        mCursor = cursor;
        mChangeObserver = changeObserver;
        mDataSetObserver = new MyDataSetObserver();

        if (cursor != null) {
            if (mChangeObserver != null) cursor.registerContentObserver(mChangeObserver);
            if (mDataSetObserver != null) cursor.registerDataSetObserver(mDataSetObserver);
        }
    }

    protected abstract void getDataFromCursor();

    protected abstract T getModelItem(Cursor cursor);

    public interface SortableAdapter {

        void sortByTimeCreated();

        void sortByName();

        void sortByPriority();

        void sortByCategory();
    }

    public void changeCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return;
        }
        Cursor oldCursor = mCursor;
        if (oldCursor != null) {
            if (mChangeObserver != null) oldCursor.unregisterContentObserver(mChangeObserver);
            if (mDataSetObserver != null) oldCursor.unregisterDataSetObserver(mDataSetObserver);
        }
        mCursor = newCursor;
        if (newCursor != null) {
            if (mChangeObserver != null) newCursor.registerContentObserver(mChangeObserver);
            if (mDataSetObserver != null) newCursor.registerDataSetObserver(mDataSetObserver);

            getDataFromCursor();
            notifyDataSetChanged();
        } else {
            notifyItemRangeRemoved(0, getItemCount());
        }
        if (oldCursor != null) {
            oldCursor.close();
        }
    }

    private class MyDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            notifyItemRangeRemoved(0, getItemCount());
        }
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
