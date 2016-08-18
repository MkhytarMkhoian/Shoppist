package com.justplay1.shoppist.ui.views.animboxes;

import android.content.Context;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;

import com.justplay1.shoppist.adapters.BaseWithoutHeaderAdapter;
import com.justplay1.shoppist.models.BaseModel;
import com.justplay1.shoppist.utils.AnimationResultListener;

import java.util.List;

/**
 * Created by Mkhitar on 23.12.2014.
 */
public class SelectItemsManager<T extends BaseModel> extends BaseSelectItemsManager<T> {

    private BaseWithoutHeaderAdapter<T> mAdapter;

    public SelectItemsManager(Context context, ActionMode.Callback callback, RecyclerView recyclerView,
                              BaseWithoutHeaderAdapter<T> adapter) {
        super(context, callback, recyclerView);
        mAdapter = adapter;
        mAdapter.setSelectItemsManager(this);
    }

    @Override
    public void deleteCheckedView(AnimationResultListener<T> resultListener) {
        mRecyclerView.setEnabled(false);
        deleteState = true;

        resultListener.onAnimationEnd(mAdapter.getCheckedItems());
        finishDelete();
    }


    @Override
    protected void refreshInvisibleItems() {
        if (mLinearLayoutManager.findFirstVisibleItemPosition() > 0) {
            mAdapter.notifyItemRangeChanged(0, mLinearLayoutManager.findFirstVisibleItemPosition());
        }
        mAdapter.notifyItemRangeChanged(mLinearLayoutManager.findLastVisibleItemPosition() + 1,
                mAdapter.getItems().size() - mLinearLayoutManager.findLastVisibleItemPosition());
    }

    @Override
    protected int getItemCount() {
        return mAdapter.getItemCount();
    }

    @Override
    protected List getCheckedItems() {
        return mAdapter.getCheckedItems();
    }

    @Override
    protected List getItems() {
        return mAdapter.getItems();
    }
}
