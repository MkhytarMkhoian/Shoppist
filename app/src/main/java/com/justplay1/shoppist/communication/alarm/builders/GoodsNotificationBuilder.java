package com.justplay1.shoppist.communication.alarm.builders;

import android.content.Context;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.NotificationTypeConstants;
import com.justplay1.shoppist.models.Product;

import java.util.List;
import java.util.Map;

/**
 * Created by Mkhytar on 13.11.2015.
 */
public class GoodsNotificationBuilder extends NotificationBuilder<Product> {

    public GoodsNotificationBuilder(Context context) {
        super(context);
    }

    @Override
    protected int getNotificationType() {
        return NotificationTypeConstants.NOTIFICATION_SYNC_GOODS_DATA_ID;
    }

    @Override
    protected String getTitleForUpdatedObjects(Map<Product, Product> items) {
        return mContext.getResources().getQuantityString(R.plurals.update_goods, items.size(), items.size());
    }

    @Override
    protected String getTitleForDeletedObjects(List<Product> items) {
        return mContext.getResources().getQuantityString(R.plurals.delete_goods, items.size(), items.size());
    }

    @Override
    protected String getTitleForNewObjects(List<Product> items) {
        return mContext.getResources().getQuantityString(R.plurals.added_goods, items.size(), items.size());
    }

    @Override
    protected String getName(Product item) {
        return item.getName();
    }
}
