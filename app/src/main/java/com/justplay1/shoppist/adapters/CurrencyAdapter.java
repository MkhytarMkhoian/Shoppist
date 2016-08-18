package com.justplay1.shoppist.adapters;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.App;
import com.justplay1.shoppist.models.Currency;
import com.justplay1.shoppist.models.ItemType;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.ui.views.animboxes.SelectBoxView;
import com.justplay1.shoppist.ui.views.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.ui.views.recyclerview.holders.BaseItemHolder;

import java.util.Locale;

/**
 * Created by Mkhytar on 30.01.2016.
 */
public class CurrencyAdapter extends BaseWithoutHeaderAdapter<Currency> {

    public CurrencyAdapter(Context context, Cursor cursor, ContentObserver changeObserver) {
        super(context, cursor, changeObserver);
        setHasStableIds(true);
    }

    @Override
    public boolean isManualSortEnable() {
        return false;
    }

    @Override
    protected Currency getModelItem(Cursor cursor) {
        return new Currency(cursor);
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
                Currency currency = getItem(position);
                currency.setChecked(mSelectItemsManager.isItemChecked(currency.getId()));

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
