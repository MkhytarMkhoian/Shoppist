package com.justplay1.shoppist.view.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.App;
import com.justplay1.shoppist.models.ItemType;
import com.justplay1.shoppist.models.UnitViewModel;
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
public class UnitsAdapter extends BaseListAdapter<UnitViewModel> {

    public UnitsAdapter(Context context, ActionModeOpenCloseListener listener,
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
                return new UnitItemViewHolder(view, mItemClickListener);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case ItemType.LIST_ITEM:
                UnitItemViewHolder holder = (UnitItemViewHolder) viewHolder;
                UnitViewModel unit = getItem(position);
                unit.setChecked(isItemChecked(unit.getId()));

                holder.name.setText(String.format("%s (%s)", unit.getName(), unit.getShortName()));
                int normalStateColor = ContextCompat.getColor(mContext, R.color.blue_grey_500);

                holder.selectBox.setNormalStateColor(normalStateColor);
                holder.selectBox.setHolder(holder);
                holder.selectBox.setInnerText(ShoppistUtils.getFirstCharacter(unit.getName()).toUpperCase(Locale.getDefault()));
                holder.selectBox.setEventListener(this);
                holder.selectBox.refresh(unit.isChecked());
                break;
        }
    }

    public static class UnitItemViewHolder extends BaseItemHolder {
        protected TextView name;

        public UnitItemViewHolder(View itemView, ShoppistRecyclerView.OnItemClickListener clickListener) {
            super(itemView, clickListener);
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
