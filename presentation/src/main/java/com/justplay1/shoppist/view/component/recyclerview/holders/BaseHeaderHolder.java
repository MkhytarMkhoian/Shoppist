package com.justplay1.shoppist.view.component.recyclerview.holders;

import android.view.View;

import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemViewHolder;
import com.justplay1.shoppist.view.component.ExpandIndicator;
import com.justplay1.shoppist.view.component.recyclerview.ShoppistRecyclerView;

/**
 * Created by Mkhytar on 04.09.2015.
 */
public abstract class BaseHeaderHolder extends BaseViewHolder implements ExpandableItemViewHolder {

    public ExpandIndicator indicator;
    protected ShoppistRecyclerView.OnHeaderClickListener mClickListener;
    private int mExpandStateFlags;

    public BaseHeaderHolder(View itemView) {
        super(itemView);
    }

    public BaseHeaderHolder(View itemView, ShoppistRecyclerView.OnHeaderClickListener clickListener) {
        super(itemView);
        mClickListener = clickListener;
    }

    @Override
    public void onClick(View v) {
        if (mClickListener != null) {
            mClickListener.onHeaderClick(this, getLayoutPosition(), getItemId());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mClickListener != null) {
            mClickListener.onHeaderLongClick(this, getLayoutPosition(), getItemId());
            return true;
        }
        return false;
    }

    @Override
    public void setExpandStateFlags(int flags) {
        mExpandStateFlags = flags;
    }

    @Override
    public int getExpandStateFlags() {
        return mExpandStateFlags;
    }
}
