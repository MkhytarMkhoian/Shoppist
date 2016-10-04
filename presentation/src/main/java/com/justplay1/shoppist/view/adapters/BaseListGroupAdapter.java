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
import android.support.v7.widget.RecyclerView;

import com.justplay1.shoppist.models.BaseViewModel;
import com.justplay1.shoppist.preferences.AppPreferences;
import com.justplay1.shoppist.view.component.actionmode.ActionModeInteractionListener;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseHeaderHolder;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseItemHolder;

/**
 * Created by Mkhytar Mkhoian.
 */
public abstract class BaseListGroupAdapter<T extends BaseViewModel, GVH extends BaseHeaderHolder, CVH extends BaseItemHolder>
        extends BaseExpandableAdapter<T, GVH, CVH> {

    protected AppPreferences preferences;

    public BaseListGroupAdapter(Context context, ActionModeInteractionListener listener,
                                RecyclerView recyclerView, AppPreferences preferences) {
        super(context, listener, recyclerView);
        this.preferences = preferences;
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
    public int getGroupItemViewType(int groupPosition) {
        return 0;
    }
}
