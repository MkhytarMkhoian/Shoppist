package com.justplay1.shoppist.view.component.recyclerview.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * Created by Mkhytar on 25.08.2015.
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    public BaseViewHolder(View itemView) {
        super(itemView);
        itemView.setClickable(true);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        init(itemView);
    }

    protected abstract void init(View itemView);
}
