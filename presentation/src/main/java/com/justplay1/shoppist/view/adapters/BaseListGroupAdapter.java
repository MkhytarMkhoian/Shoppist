/*
 * Copyright (C) 2016 Mkhytar Mkhoian
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.justplay1.shoppist.view.adapters;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.expandable.GroupPositionItemDraggableRange;
import com.justplay1.shoppist.models.BaseViewModel;
import com.justplay1.shoppist.preferences.AppPreferences;
import com.justplay1.shoppist.utils.ViewUtils;
import com.justplay1.shoppist.view.component.actionmode.ActionModeInteractionListener;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseDraggableItemViewHolder;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseHeaderHolder;

/**
 * Created by Mkhytar Mkhoian.
 */
public abstract class BaseListGroupAdapter<T extends BaseViewModel, GVH extends BaseHeaderHolder, CVH extends BaseDraggableItemViewHolder>
        extends BaseExpandableDraggableAdapter<T, GVH, CVH> {

    protected AppPreferences mPreferences;

    public BaseListGroupAdapter(Context context, ActionModeInteractionListener listener,
                                RecyclerView recyclerView, AppPreferences preferences) {
        super(context, listener, recyclerView);
        mPreferences = preferences;
    }

    @Override
    public int getChildItemViewType(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(GVH holder, int groupPosition, int x, int y, boolean expand) {
        return holder.itemView.isEnabled();
    }

    @Override
    public boolean onCheckGroupCanStartDrag(GVH holder, int groupPosition, int x, int y) {
        return false;
    }

    @Override
    public boolean onCheckChildCanStartDrag(CVH holder, int groupPosition, int childPosition, int x, int y) {
        if (holder.dragHandle == null || holder.dragHandle.getVisibility() == View.GONE)
            return false;

        // x, y --- relative from the itemView's top-left
        final View containerView = holder.container;
        final View dragHandleView = holder.dragHandle;

        final int offsetX = containerView.getLeft() + (int) (ViewCompat.getTranslationX(containerView) + 0.5f);
        final int offsetY = containerView.getTop() + (int) (ViewCompat.getTranslationY(containerView) + 0.5f);

        return ViewUtils.hitTest(dragHandleView, x - offsetX, y - offsetY);
    }

    @Override
    public ItemDraggableRange onGetGroupItemDraggableRange(GVH holder, int groupPosition) {
        // sort within the same section
        final int start = findFirstSectionItem(groupPosition);
        final int end = findLastSectionItem(groupPosition);

        return new GroupPositionItemDraggableRange(start, end);
    }

    @Override
    public ItemDraggableRange onGetChildItemDraggableRange(CVH holder, int groupPosition, int childPosition) {
        // sort within the same group
        return new GroupPositionItemDraggableRange(groupPosition, groupPosition);

        // sort within the same section
//        final int start = findFirstSectionItem(groupPosition);
//        final int end = findLastSectionItem(groupPosition);
//
//        return new GroupPositionItemDraggableRange(start, end);

        // sort within the specified child range
//        final int start = 0;
//        final int end = 2;
//
//        return new ChildPositionItemDraggableRange(start, end);
    }

    @Override
    public int getGroupItemViewType(int groupPosition) {
        return 0;
    }
}
