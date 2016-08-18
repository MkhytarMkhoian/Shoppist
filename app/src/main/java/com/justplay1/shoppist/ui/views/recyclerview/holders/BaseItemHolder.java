package com.justplay1.shoppist.ui.views.recyclerview.holders;

import android.view.View;

import com.justplay1.shoppist.ui.views.animboxes.SelectBoxView;
import com.justplay1.shoppist.ui.views.recyclerview.ShoppistRecyclerView;

/**
 * Created by Mkhytar on 04.09.2015.
 */
public abstract class BaseItemHolder extends BaseViewHolder {

    protected ShoppistRecyclerView.OnItemClickListener mClickListener;

    public SelectBoxView selectBox;
    public View container;
    public int groupPosition = -1;
    public int childPosition = -1;

    public BaseItemHolder(View itemView) {
        super(itemView);
    }

    public BaseItemHolder(View itemView, ShoppistRecyclerView.OnItemClickListener clickListener) {
        super(itemView);
        mClickListener = clickListener;
    }

    @Override
    public void onClick(View v) {
        if (mClickListener != null) {
            mClickListener.onItemClick(this, getLayoutPosition(), getItemId());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mClickListener != null) {
            mClickListener.onItemLongClick(this, getLayoutPosition(), getItemId());
            return true;
        }
        return false;
    }
}
