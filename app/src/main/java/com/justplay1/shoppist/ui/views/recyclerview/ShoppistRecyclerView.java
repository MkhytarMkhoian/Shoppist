package com.justplay1.shoppist.ui.views.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.justplay1.shoppist.ui.views.recyclerview.holders.BaseHeaderHolder;
import com.justplay1.shoppist.ui.views.recyclerview.holders.BaseItemHolder;

/**
 * Created by Mkhitar on 23.08.2015.
 */
public class ShoppistRecyclerView extends RecyclerView {

    private View mEmptyView;

    public ShoppistRecyclerView(Context context) {
        super(context);
    }

    public ShoppistRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShoppistRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public interface OnItemClickListener {
        void onItemClick(BaseItemHolder holder, int position, long id);

        boolean onItemLongClick(BaseItemHolder holder, int position, long id);
    }

    public interface OnHeaderClickListener {
        void onHeaderClick(BaseHeaderHolder holder, int position, long id);

        boolean onHeaderLongClick(BaseHeaderHolder holder, int position, long id);
    }

    final private AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmpty();
        }
    };

    void checkIfEmpty() {
        if (mEmptyView != null && getAdapter() != null) {
            final boolean emptyViewVisible = getAdapter().getItemCount() == 0;
            mEmptyView.setVisibility(emptyViewVisible ? VISIBLE : GONE);
            setVisibility(emptyViewVisible ? GONE : VISIBLE);
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }
        checkIfEmpty();
    }

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
        checkIfEmpty();
    }

    public View getEmptyView() {
        return mEmptyView;
    }
}
