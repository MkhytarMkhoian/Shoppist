package com.justplay1.shoppist.utils;

import android.view.View;

import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.justplay1.shoppist.R;

/**
 * Created by Mkhytar on 14.09.2015.
 */
public class DraggableUtils {

    public static void clearSelector(DraggableItemViewHolder holder, View container){
        final int dragState = holder.getDragStateFlags();
        if (((dragState & RecyclerViewDragDropManager.STATE_FLAG_IS_UPDATED) != 0)) {
            int bgResId;
            if ((dragState & RecyclerViewDragDropManager.STATE_FLAG_IS_ACTIVE) != 0) {
                bgResId = R.drawable.bg_item_normal_state;
            } else {
                bgResId = R.drawable.list_view_selector;
            }
            container.setBackgroundResource(bgResId);
        }
    }
}
