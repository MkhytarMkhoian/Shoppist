package com.justplay1.shoppist.communication.sync.fromserver;

import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.util.Log;

import com.justplay1.shoppist.communication.alarm.builders.NotificationBuilder;
import com.justplay1.shoppist.communication.network.ParseErrorHandler;
import com.justplay1.shoppist.database.BaseTable;
import com.justplay1.shoppist.database.TablesHolder;
import com.justplay1.shoppist.App;
import com.justplay1.shoppist.models.BaseModel;
import com.justplay1.shoppist.widget.ShoppistWidgetProvider;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mkhytar on 27.03.2016.
 */
public abstract class SyncFromServer<T extends BaseModel> {

    protected TablesHolder tablesHolder;

    public SyncFromServer() {
        tablesHolder = App.get().getTablesHolder();
    }

    protected abstract Collection<T> getDataFromServer(final long refreshTime) throws ParseException;

    protected abstract Map<String, T> getCache();

    protected abstract NotificationBuilder<T> getNotificationBuilder();

    protected abstract BaseTable<T> getTable();

    protected abstract String getTag();

    public void sync(boolean deleteDataBefore, final long refreshTime, boolean notifyUser) {
        if (ParseUser.getCurrentUser() == null) return;
        try {
            Collection<T> result = getDataFromServer(refreshTime);
            if (deleteDataBefore) {
                if (deleteDataIfNeeded(result)) return;
            } else {
                if (result.size() == 0) return;
                Map<T, T> toUpdate = new HashMap<>();
                List<T> toAdd = new ArrayList<>();
                List<T> toDelete = new ArrayList<>();
                Map<String, T> cache = getCache();

                for (T item : result) {
                    T tmp = cache.get(item.getId());
                    if (tmp != null) {
                        if (!tmp.isDirty() && item.isDelete()) {
                            toDelete.add(tmp);
                        } else {
                            toUpdate.put(tmp, item);
                        }
                    } else if (!item.isDelete()) {
                        toAdd.add(item);
                    }
                    item.setDirty(false);
                }

                updateDB(toUpdate, toAdd, toDelete);

                if (notifyUser) {
                    NotificationBuilder<T> builder = getNotificationBuilder();
                    builder.addDeletedItems(toDelete);
                    builder.addNewItems(toAdd);
                    builder.addUpdatedItems(toUpdate);
                    builder.save();
                    builder.notifyUser();
                }
                Log.d(getTag(), "Add: " + toAdd.size() + " Delete: " + toDelete.size() + " Update: " + toUpdate.size());
            }
            ShoppistWidgetProvider.updateAllWidget(tablesHolder.getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void updateDB(Map<T, T> toUpdate, List<T> toAdd, List<T> toDelete) throws RemoteException, OperationApplicationException {
        getTable().update(toUpdate.values());
        getTable().put(toAdd);
        getTable().delete(toDelete);
    }

    protected boolean deleteDataIfNeeded(Collection<T> result) throws RemoteException, OperationApplicationException {
        getTable().clear();
        if (result.size() == 0) return true;
        List<T> toUpdate = new ArrayList<>();
        for (T item : result) {
            if (item.isDelete()) continue;
            toUpdate.add(item);
        }
        getTable().put(toUpdate);
        return false;
    }
}
