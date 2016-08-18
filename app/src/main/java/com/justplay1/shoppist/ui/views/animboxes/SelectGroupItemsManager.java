package com.justplay1.shoppist.ui.views.animboxes;

import android.content.Context;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;

import com.justplay1.shoppist.adapters.BaseExpandableAdapter;
import com.justplay1.shoppist.models.BaseModel;
import com.justplay1.shoppist.utils.AnimationResultListener;

import java.util.List;

/**
 * Created by Mkhytar on 07.09.2015.
 */
public class SelectGroupItemsManager<T extends BaseModel, GVH extends RecyclerView.ViewHolder, CVH extends RecyclerView.ViewHolder> extends BaseSelectItemsManager<T> {

    private BaseExpandableAdapter<T, GVH, CVH> mAdapter;

    public SelectGroupItemsManager(Context context, ActionMode.Callback callback, RecyclerView recyclerView,
                                   BaseExpandableAdapter<T, GVH, CVH> adapter) {
        super(context, callback, recyclerView);
        mAdapter = adapter;
        mAdapter.setSelectItemsManager(this);
    }

    @Override
    protected int getItemCount() {
        return mAdapter.getChildItemsCount();
    }

    @Override
    protected List<T> getCheckedItems() {
        return mAdapter.getCheckedItems();
    }

    @Override
    protected List<T> getItems() {
        return mAdapter.getItems();
    }

    @Override
    protected void refreshInvisibleItems() {
        if (mLinearLayoutManager.findFirstVisibleItemPosition() > 0) {
            mAdapter.notifyItemRangeChanged(0, mLinearLayoutManager.findFirstVisibleItemPosition());
        }
        mAdapter.notifyItemRangeChanged(mLinearLayoutManager.findLastVisibleItemPosition() + 1,
                (mAdapter.getGroupCount() + mAdapter.getChildItemsCount()) - mLinearLayoutManager.findLastVisibleItemPosition());
    }

    @Override
    public void deleteCheckedView(final AnimationResultListener<T> resultListener) {
        mRecyclerView.setEnabled(false);
        deleteState = true;

        resultListener.onAnimationEnd(mAdapter.getCheckedItems());
        finishDelete();
    }
}
