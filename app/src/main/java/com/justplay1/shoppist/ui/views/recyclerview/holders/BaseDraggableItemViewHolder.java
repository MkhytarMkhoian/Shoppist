package com.justplay1.shoppist.ui.views.recyclerview.holders;

import android.view.View;

import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.draggable.annotation.DraggableItemStateFlags;
import com.justplay1.shoppist.ui.views.recyclerview.ShoppistRecyclerView;

/**
 * Created by Mkhytar on 10.10.2015.
 */
public abstract class BaseDraggableItemViewHolder extends BaseItemHolder implements DraggableItemViewHolder {

    @DraggableItemStateFlags
    private int mDragStateFlags;

    public View dragHandle;

    public BaseDraggableItemViewHolder(View itemView) {
        super(itemView);
    }

    public BaseDraggableItemViewHolder(View itemView, ShoppistRecyclerView.OnItemClickListener clickListener) {
        super(itemView, clickListener);
    }

    @Override
    public void setDragStateFlags(@DraggableItemStateFlags int flags) {
        mDragStateFlags = flags;
    }

    @Override
    @DraggableItemStateFlags
    public int getDragStateFlags() {
        return mDragStateFlags;
    }
}