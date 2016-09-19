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

package com.justplay1.shoppist.view.component.recyclerview.holders;

import android.view.View;

import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemViewHolder;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.view.component.ExpandIndicator;
import com.justplay1.shoppist.view.component.recyclerview.ShoppistRecyclerView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mkhytar Mkhoian.
 */
public abstract class BaseHeaderHolder extends BaseViewHolder implements ExpandableItemViewHolder {

    @Bind(R.id.indicator)
    public ExpandIndicator indicator;
    protected ShoppistRecyclerView.OnHeaderClickListener mClickListener;
    private int mExpandStateFlags;

    public BaseHeaderHolder(View itemView, ShoppistRecyclerView.OnHeaderClickListener clickListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
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
