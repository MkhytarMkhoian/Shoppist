//package com.justplay1.shoppist.repository.sync.fromserver;
//
//import android.util.Log;
//
//import com.justplay1.shoppist.entity.BaseDAO;
//import com.justplay1.shoppist.repository.datasource.local.LocalDataStore;
//import com.justplay1.shoppist.repository.datasource.remote.RemoteGetData;
//import com.justplay1.shoppist.repository.sync.alarm.builders.NotificationBuilder;
//import com.parse.ParseUser;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by Mkhytar on 27.03.2016.
// */
//public abstract class SyncFromServer<T extends BaseDAO> {
//
//    protected LocalDataStore<T> mLocalDataStore;
//    protected RemoteGetData<T> mRemoteDataStore;
//    protected NotificationBuilder<T> mNotificationBuilder;
//
//    public SyncFromServer(LocalDataStore<T> localDataStore, RemoteGetData<T> remoteGetData, NotificationBuilder<T> builder) {
//        this.mLocalDataStore = localDataStore;
//        this.mRemoteDataStore = remoteGetData;
//        this.mNotificationBuilder = builder;
//    }
//
//    protected abstract String getTag();
//
//    public void sync(boolean deleteDataBefore, final long timestamp, boolean notifyUser) {
//        if (ParseUser.getCurrentUser() == null) return;
//        try {
//            Collection<T> result = mRemoteDataStore.getItems(timestamp);
//            if (deleteDataBefore) {
//                if (deleteDataIfNeeded(result)) return;
//            } else {
//                if (result.size() == 0) return;
//                Map<T, T> toUpdate = new HashMap<>();
//                List<T> toAdd = new ArrayList<>();
//                List<T> toDelete = new ArrayList<>();
//                Map<String, T> cache = mLocalDataStore.getItems();
//
//                for (T item : result) {
//                    T tmp = cache.get(item.getId());
//                    if (tmp != null) {
//                        if (!tmp.isDirty() && item.isDelete()) {
//                            toDelete.add(tmp);
//                        } else {
//                            toUpdate.put(tmp, item);
//                        }
//                    } else if (!item.isDelete()) {
//                        toAdd.add(item);
//                    }
//                    item.setDirty(false);
//                }
//
//                updateDB(toUpdate, toAdd, toDelete);
//
//                if (notifyUser) {
//                    mNotificationBuilder.addDeletedItems(toDelete);
//                    mNotificationBuilder.addNewItems(toAdd);
//                    mNotificationBuilder.addUpdatedItems(toUpdate);
//                    mNotificationBuilder.save();
//                    mNotificationBuilder.notifyUser();
//                }
//                Log.d(getTag(), "Add: " + toAdd.size() + " Delete: " + toDelete.size() + " Update: " + toUpdate.size());
//            }
////            ShoppistWidgetProvider.updateAllWidget(); TODO
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    protected void updateDB(Map<T, T> toUpdate, List<T> toAdd, List<T> toDelete) throws Exception {
//        mLocalDataStore.update(toUpdate.values());
//        mLocalDataStore.save(toAdd);
//        mLocalDataStore.delete(toDelete);
//    }
//
//    protected boolean deleteDataIfNeeded(Collection<T> result) throws Exception {
//        mLocalDataStore.clear();
//        if (result.size() == 0) return true;
//        List<T> toUpdate = new ArrayList<>();
//        for (T item : result) {
//            if (item.isDelete()) continue;
//            toUpdate.add(item);
//        }
//        mLocalDataStore.save(toUpdate);
//        return false;
//    }
//}
