package com.justplay1.shoppist.communication.alarm.builders;

import android.content.Context;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.Currency;
import com.justplay1.shoppist.models.NotificationTypeConstants;

import java.util.List;
import java.util.Map;

/**
 * Created by Mkhytar on 13.11.2015.
 */
public class CurrenciesNotificationBuilder extends NotificationBuilder<Currency> {

    public CurrenciesNotificationBuilder(Context context) {
        super(context);
    }

    @Override
    protected int getNotificationType() {
        return NotificationTypeConstants.NOTIFICATION_SYNC_CURRENCIES_DATA_ID;
    }

    @Override
    protected String getTitleForNewObjects(List<Currency> items) {
        return mContext.getResources().getQuantityString(R.plurals.added_currencies, items.size(), items.size());
    }

    @Override
    protected String getTitleForUpdatedObjects(Map<Currency, Currency> items) {
        return mContext.getResources().getQuantityString(R.plurals.update_currencies, items.size(), items.size());
    }

    @Override
    protected String getTitleForDeletedObjects(List<Currency> items) {
        return mContext.getResources().getQuantityString(R.plurals.delete_currencies, items.size(), items.size());
    }

    @Override
    protected String getName(Currency item) {
        return item.getName();
    }
}
