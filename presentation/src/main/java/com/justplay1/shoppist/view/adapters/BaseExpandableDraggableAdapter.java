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
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;

import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableDraggableItemAdapter;
import com.justplay1.shoppist.models.BaseViewModel;
import com.justplay1.shoppist.models.HeaderViewModel;
import com.justplay1.shoppist.view.component.actionmode.ActionModeInteractionListener;

import java.util.List;

/**
 * Created by Mkhytar Mkhoian.
 */
public abstract class BaseExpandableDraggableAdapter<T extends BaseViewModel, GVH extends RecyclerView.ViewHolder, CVH extends RecyclerView.ViewHolder>
        extends BaseExpandableAdapter<T, GVH, CVH>
        implements ExpandableDraggableItemAdapter<GVH, CVH> {

    public BaseExpandableDraggableAdapter(Context context, ActionModeInteractionListener listener,
                                          RecyclerView recyclerView) {
        super(context, listener, recyclerView);
    }

    @Override
    public void onMoveGroupItem(int fromGroupPosition, int toGroupPosition) {
        if (fromGroupPosition == toGroupPosition) {
            return;
        }
        final Pair<HeaderViewModel, List<T>> item = mData.remove(fromGroupPosition);
        mData.add(toGroupPosition, item);
    }

    @Override
    public void onMoveChildItem(int fromGroupPosition, int fromChildPosition, int toGroupPosition, int toChildPosition) {
        if ((fromGroupPosition == toGroupPosition) && (fromChildPosition == toChildPosition)) {
            return;
        }
        final Pair<HeaderViewModel, List<T>> fromGroup = mData.get(fromGroupPosition);
        final Pair<HeaderViewModel, List<T>> toGroup = mData.get(toGroupPosition);

        final T item = fromGroup.second.remove(fromChildPosition);
        toGroup.second.add(toChildPosition, item);
    }
}
