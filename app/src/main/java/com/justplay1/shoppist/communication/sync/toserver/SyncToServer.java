package com.justplay1.shoppist.communication.sync.toserver;

import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.util.Log;

import com.justplay1.shoppist.database.BaseTable;
import com.justplay1.shoppist.models.BaseModel;
import com.parse.ParseException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Mkhytar on 27.03.2016.
 */
public abstract class SyncToServer<T extends BaseModel> {

    protected void filter(Collection<T> data, Set<T> toUpdate, Set<T> toAdd) {
        for (T item : data) {
            if (item.getServerId() == null) {
                toAdd.add(item);
            } else {
                toUpdate.add(item);
            }
        }
    }

    public boolean sync(Collection<T> data) throws ParseException, RemoteException, OperationApplicationException {
        if (data.size() == 0) return false;
        Set<T> toUpdate = new HashSet<>();
        Set<T> toAdd = new HashSet<>();

        filter(data, toUpdate, toAdd);
        addItemsToServer(toAdd);

        if (toUpdate.size() > 0) {
            updateItemsToServer(toUpdate);
            for (T item : toUpdate) {
                item.setDirty(false);
            }
        }

        toUpdate.addAll(toAdd);
        Set<T> toDelete = new HashSet<>();
        for (T item : toUpdate) {
            if (item.isDelete() && !item.isDirty()) {
                toDelete.add(item);
            }
        }
        toUpdate.removeAll(toDelete);
        getTable().delete(toDelete);
        getTable().update(toUpdate);

        Log.d(getTag(), "Add: " + toAdd.size() + " Delete: " + toDelete.size() + " Update: " + (toUpdate.size() - toAdd.size()));
        return toUpdate.size() > 0 || toDelete.size() > 0;
    }

    protected abstract BaseTable<T> getTable();

    protected abstract String getTag();

    protected abstract void addItemsToServer(Set<T> toAdd) throws ParseException;

    protected abstract void updateItemsToServer(Set<T> toUpdate) throws ParseException;
}
