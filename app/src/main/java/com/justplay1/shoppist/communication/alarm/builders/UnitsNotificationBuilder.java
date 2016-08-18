package com.justplay1.shoppist.communication.alarm.builders;

import android.content.Context;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.NotificationTypeConstants;
import com.justplay1.shoppist.models.Unit;

import java.util.List;
import java.util.Map;

/**
 * Created by Mkhytar on 13.11.2015.
 */
public class UnitsNotificationBuilder extends NotificationBuilder<Unit> {

    public UnitsNotificationBuilder(Context context) {
        super(context);
    }

    @Override
    protected int getNotificationType() {
        return NotificationTypeConstants.NOTIFICATION_SYNC_UNITS_DATA_ID;
    }

    @Override
    protected String getTitleForNewObjects(List<Unit> items) {
        return mContext.getResources().getQuantityString(R.plurals.added_units, items.size(), items.size());
    }

    @Override
    protected String getTitleForUpdatedObjects(Map<Unit, Unit> items) {
        return mContext.getResources().getQuantityString(R.plurals.update_units, items.size(), items.size());
    }

    @Override
    protected String getTitleForDeletedObjects(List<Unit> items) {
        return mContext.getResources().getQuantityString(R.plurals.delete_units, items.size(), items.size());
    }

    @Override
    protected String getName(Unit item) {
        return item.getName() + " (" + item.getShortName() + ")";
    }
}
