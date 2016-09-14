//package com.justplay1.shoppist.repository.sync.toserver;
//
//import android.util.Log;
//
//import com.justplay1.shoppist.entity.BaseDAO;
//import com.justplay1.shoppist.repository.datasource.local.LocalDataStore;
//import com.justplay1.shoppist.repository.datasource.remote.RemoteSetData;
//
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.Set;
//
///**
// * Created by Mkhytar on 27.03.2016.
// */
//public abstract class SyncToServer<T extends BaseDAO> {
//
//    protected LocalDataStore<T> mLocalDataStore;
//    protected RemoteSetData<T> mRemoteDataStore;
//
//    public SyncToServer(LocalDataStore<T> localDataStore, RemoteSetData<T> remoteDataStore) {
//        this.mLocalDataStore = localDataStore;
//        this.mRemoteDataStore = remoteDataStore;
//    }
//
//    protected void filter(Collection<T> data, Set<T> toUpdate, Set<T> toAdd) {
//        for (T item : data) {
//            if (item.getServerId() == null) {
//                toAdd.add(item);
//            } else {
//                toUpdate.add(item);
//            }
//        }
//    }
//
//    public boolean sync() throws Exception {
////        Collection<T> data = mLocalDataStore.getDirtyItems();
//        Collection<T> data = null;
//        if (data.size() == 0) return false;
//        Set<T> toUpdate = new HashSet<>();
//        Set<T> toAdd = new HashSet<>();
//
//        filter(data, toUpdate, toAdd);
//        mRemoteDataStore.save(toAdd);
//
//        if (toUpdate.size() > 0) {
//            mRemoteDataStore.update(toUpdate);
//            for (T item : toUpdate) {
//                item.setDirty(false);
//            }
//        }
//
//        toUpdate.addAll(toAdd);
//        Set<T> toDelete = new HashSet<>();
//        for (T item : toUpdate) {
//            if (item.isDelete() && !item.isDirty()) {
//                toDelete.add(item);
//            }
//        }
//        toUpdate.removeAll(toDelete);
//        mLocalDataStore.delete(toDelete);
//        mLocalDataStore.update(toUpdate);
//
//        Log.d(getTag(), "Add: " + toAdd.size() + " Delete: " + toDelete.size() + " Update: " + (toUpdate.size() - toAdd.size()));
//        return toUpdate.size() > 0 || toDelete.size() > 0;
//    }
//
//    protected abstract String getTag();
//}
