package com.justplay1.shoppist.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.CurrencyViewModel;
import com.justplay1.shoppist.models.ItemType;
import com.justplay1.shoppist.preferences.ShoppistPreferences;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.view.component.actionmode.ActionModeOpenCloseListener;
import com.justplay1.shoppist.view.component.animboxes.SelectBoxView;
import com.justplay1.shoppist.view.component.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseItemHolder;

import java.util.Locale;

/**
 * Created by Mkhytar on 30.01.2016.
 */
public class CurrencyAdapter extends BaseListAdapter<CurrencyViewModel> {

    public CurrencyAdapter(Context context, ActionModeOpenCloseListener listener,
                           RecyclerView recyclerView) {
        super(context, listener, recyclerView);
        setHasStableIds(true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case ItemType.LIST_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_currency_item, parent, false);
                return new CurrencyItemViewHolder(view, mItemClickListener);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case ItemType.LIST_ITEM:
                CurrencyItemViewHolder holder = (CurrencyItemViewHolder) viewHolder;
                CurrencyViewModel currency = getItem(position);
                currency.setChecked(isItemChecked(currency.getId()));

                holder.name.setText(currency.getName());

                int normalStateColor = mContext.getResources().getColor(R.color.blue_grey_500);
                holder.selectBox.setNormalStateColor(normalStateColor);
                holder.selectBox.setHolder(holder);
                holder.selectBox.setInnerText(ShoppistUtils.getFirstCharacter(currency.getName()).toUpperCase(Locale.getDefault()));
                holder.selectBox.setEventListener(this);
                holder.selectBox.refresh(currency.isChecked());
                break;
        }
    }

    public static class CurrencyItemViewHolder extends BaseItemHolder {
        protected TextView name;

        public CurrencyItemViewHolder(View itemView, ShoppistRecyclerView.OnItemClickListener clickListener) {
            super(itemView, clickListener);
        }

        public CurrencyItemViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void init(View itemView) {
            container = itemView.findViewById(R.id.swipe_container);
            selectBox = (SelectBoxView) itemView.findViewById(R.id.select_box);
            name = (TextView) itemView.findViewById(R.id.item_name);
            selectBox.setInnerTextTypeface(App.fontRobotoLight);
        }
    }
}
