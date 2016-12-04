/*
 * Copyright (C) 2016 Mkhytar Mkhoian
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.justplay1.shoppist.shared.widget.recyclerview.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.justplay1.shoppist.shared.widget.recyclerview.ShoppistRecyclerView;

/**
 * Created by Mkhytar Mkhoian.
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnLongClickListener {

    private ShoppistRecyclerView.OnItemClickListener<BaseViewHolder> clickListener;

    public BaseViewHolder(View itemView) {
        super(itemView);
        itemView.setClickable(true);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void setClickListener(ShoppistRecyclerView.OnItemClickListener<BaseViewHolder> mClickListener) {
        this.clickListener = mClickListener;
    }

    @Override
    public void onClick(View v) {
        if (clickListener != null) {
            clickListener.onItemClick(this, getLayoutPosition(), getItemId());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (clickListener != null) {
            clickListener.onItemLongClick(this, getLayoutPosition(), getItemId());
            return true;
        }
        return false;
    }
}
