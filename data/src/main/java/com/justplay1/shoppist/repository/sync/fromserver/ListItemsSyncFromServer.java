//package com.justplay1.shoppist.repository.sync.fromserver;
//
//import com.justplay1.shoppist.di.scopes.PerSync;
//import com.justplay1.shoppist.entity.ListItemDAO;
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
//public class ListItemsSyncFromServer extends SyncFromServer<ListItemDAO> {
//
//    @Inject
//    public ListItemsSyncFromServer(LocalDataStore<ListItemDAO> localDataStore,
//                                   RemoteGetData<ListItemDAO> remoteGetData,
//                                   NotificationBuilder<ListItemDAO> builder) {
//        super(localDataStore, remoteGetData, builder);
//    }
//
//    @Override
//    protected String getTag() {
//        return "ShoppingListItems from";
//    }
//}
