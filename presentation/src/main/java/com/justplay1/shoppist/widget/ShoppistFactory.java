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

package com.justplay1.shoppist.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.ListItemViewModel;

/**
 * Created by Mkhytar Mkhoian.
 */
public class ShoppistFactory extends BaseFactory<ListItemViewModel> {

    public ShoppistFactory(Context ctx, Intent intent) {
        super(ctx, intent);
    }

    @Override
    public void onDataSetChanged() {
        final long token = Binder.clearCallingIdentity();
        try {
            mData.clear();
//            ShoppingListItemTable shoppingListTable = new ShoppingListItemTable(mContext);
//            List<ListItemViewModel> shoppingListItems = shoppingListTable.getShoppingListItems(mParentListId);
//            for (ListItemViewModel item : shoppingListItems) {
//                mData.add(item);
//            }
            sort();
        } finally {
            Binder.restoreCallingIdentity(token);
        }
    }

    @NonNull
    @Override
    protected RemoteViews getRemoteViewsForItem(int position) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);

        Intent clickIntent = new Intent();
        clickIntent.putExtra(ShoppistWidgetProvider.ACTION, ShoppistWidgetProvider.ACTION_ON_LIST_ITEM_CLICK);
        clickIntent.putExtra(ShoppistWidgetProvider.ITEM_POSITION, position);

        Intent checkIntent = new Intent(mContext, ShoppistWidgetProvider.class);
        checkIntent.putExtra(ShoppistWidgetProvider.ACTION, ShoppistWidgetProvider.ACTION_ON_CHECK_ITEM_CLICK);
        checkIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mWidgetId);

        final ListItemViewModel item = mData.get(position);

        remoteViews.setTextViewText(R.id.item_name, item.getName());
//        remoteViews.setInt(R.id.priority, "setBackgroundColor", mContext.getResources().getColor(item.getPriority().mColorRes));
        clickIntent.putExtra(ShoppistWidgetProvider.LIST_ITEM, item);
        checkIntent.putExtra(ShoppistWidgetProvider.ITEM_STATUS, item.getStatus());
        checkIntent.putExtra(ShoppistWidgetProvider.LIST_ITEM, item);

        String price = item.getCategory().getName();
        if (item.getQuantity() > 0) {
            price += " (" + item.getQuantity() + " " + item.getUnit().getShortName();
            if (item.getPrice() > 0) {
                price += " / ";
            } else {
                price += ")";
            }
        }
        if (item.getPrice() > 0) {
            if (item.getQuantity() == 0) {
                price += " (";
            }
            price += item.getPrice() + " " + item.getCurrency().getName() + ")";
        }
        remoteViews.setTextViewText(R.id.category_name, price);
        setNote(remoteViews, item.getNote(), item.getNote().isEmpty());
        setItemChecked(remoteViews, item.getStatus());

        remoteViews.setOnClickFillInIntent(R.id.item_container, clickIntent);
        remoteViews.setOnClickFillInIntent(R.id.check_btn, checkIntent);
        return remoteViews;
    }
}
