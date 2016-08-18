//package com.justplay1.shoppist.repository.sync.fromserver;
//
//import com.justplay1.shoppist.di.scopes.PerSync;
//import com.justplay1.shoppist.entity.ListDAO;
//import com.justplay1.shoppist.repository.sync.alarm.builders.NotificationBuilder;
//import com.justplay1.shoppist.repository.datasource.local.LocalDataStore;
//import com.justplay1.shoppist.repository.datasource.remote.RemoteGetData;
//
//import javax.inject.Inject;
//
///**
// * Created by Mkhytar on 27.03.2016.
// */
//@PerSync
//public class ListSyncFromServer extends SyncFromServer<ListDAO> {
//
//    @Inject
//    public ListSyncFromServer(LocalDataStore<ListDAO> localDataStore,
//                              RemoteGetData<ListDAO> remoteGetData,
//                              NotificationBuilder<ListDAO> builder) {
//        super(localDataStore, remoteGetData, builder);
//    }
//
//    @Override
//    protected String getTag() {
//        return "Shopping Lists from";
//    }
//}
