package com.justplay1.shoppist.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.database.tables.ShoppingListItemTable;
import com.justplay1.shoppist.models.ShoppingListItem;
import com.justplay1.shoppist.models.Status;

import java.util.List;

/**
 * Created by Mkhytar on 14.10.2015.
 */
public class ShoppistFactory extends BaseFactory<ShoppingListItem> {

    public ShoppistFactory(Context ctx, Intent intent) {
        super(ctx, intent);
    }

    @Override
    public void onDataSetChanged() {
        final long token = Binder.clearCallingIdentity();
        try {
            mData.clear();
            ShoppingListItemTable shoppingListTable = new ShoppingListItemTable(mContext);
            List<ShoppingListItem> shoppingListItems = shoppingListTable.getShoppingListItems(mParentListId);
            for (ShoppingListItem item : shoppingListItems) {
                mData.add(item);
            }
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

        final ShoppingListItem item = mData.get(position);

        remoteViews.setTextViewText(R.id.item_name, item.getName());
        remoteViews.setInt(R.id.priority, "setBackgroundColor", mContext.getResources().getColor(item.getPriority().mColorRes));
        clickIntent.putExtra(ShoppistWidgetProvider.LIST_ITEM, item);
        checkIntent.putExtra(ShoppistWidgetProvider.ITEM_STATUS, item.getStatus() == Status.DONE);
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
        setItemChecked(remoteViews, item.getStatus() == Status.DONE);

        remoteViews.setOnClickFillInIntent(R.id.item_container, clickIntent);
        remoteViews.setOnClickFillInIntent(R.id.check_btn, checkIntent);
        return remoteViews;
    }
}
