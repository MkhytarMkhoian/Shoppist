//package com.justplay1.shoppist.repository.sync.toserver;
//
//import com.justplay1.shoppist.di.scopes.PerSync;
//import com.justplay1.shoppist.entity.ListDAO;
//import com.justplay1.shoppist.repository.datasource.local.LocalDataStore;
//import com.justplay1.shoppist.repository.datasource.remote.RemoteSetData;
//
//import javax.inject.Inject;
//
///**
// * Created by Mkhytar on 27.03.2016.
// */
//@PerSync
//public class ListSyncToServer extends SyncToServer<ListDAO> {
//
//    @Inject
//    public ListSyncToServer(LocalDataStore<ListDAO> localDataStore, RemoteSetData<ListDAO> remoteSetData) {
//        super(localDataStore, remoteSetData);
//    }
//
//    @Override
//    protected String getTag() {
//        return "Shopping Lists to";
//    }
//}
