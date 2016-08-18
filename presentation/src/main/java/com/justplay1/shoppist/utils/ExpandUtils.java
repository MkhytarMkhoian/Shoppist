package com.justplay1.shoppist.utils;

import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseHeaderHolder;

/**
 * Created by Mkhytar on 12.09.2015.
 */
public class ExpandUtils {

    public static void toggleIndicator(BaseHeaderHolder holder){
        final int expandState = holder.getExpandStateFlags();
        if (((expandState & RecyclerViewExpandableItemManager.STATE_FLAG_IS_UPDATED) != 0)) {
            if ((expandState & RecyclerViewExpandableItemManager.STATE_FLAG_IS_EXPANDED) != 0) {
                holder.indicator.setExpand(true);
            } else {
                holder.indicator.setExpand(false);
            }
        }
    }
}
