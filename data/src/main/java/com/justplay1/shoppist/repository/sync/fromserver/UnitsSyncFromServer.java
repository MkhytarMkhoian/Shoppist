//package com.justplay1.shoppist.repository.sync.fromserver;
//
//import com.justplay1.shoppist.di.scopes.PerSync;
//import com.justplay1.shoppist.entity.UnitDAO;
//import com.justplay1.shoppist.repository.sync.alarm.builders.NotificationBuilder;
//import com.justplay1.shoppist.repository.datasource.local.LocalDataStore;
//import com.justplay1.shoppist.repository.datasource.remote.RemoteGetData;
//
//import java.util.Collection;
//import java.util.Map;
//
//import javax.inject.Inject;
//
///**
// * Created by Mkhytar on 27.03.2016.
// */
//@PerSync
//public class UnitsSyncFromServer extends SyncFromServer<UnitDAO> {
//
//    @Inject
//    public UnitsSyncFromServer(LocalDataStore<UnitDAO> localDataStore, RemoteGetData<UnitDAO> remoteGetData,
//                               NotificationBuilder<UnitDAO> builder) {
//        super(localDataStore, remoteGetData, builder);
//    }
//
//    @Override
//    protected boolean deleteDataIfNeeded(Collection<UnitDAO> result) throws Exception {
//        mLocalDataStore.clear();
//        Map<String, UnitDAO> units = mLocalDataStore.getDefaultData();
//        if (result.size() > 0) {
//            for (UnitDAO itemFromServer : result) {
//                if (itemFromServer.isDelete()) continue;
//                UnitDAO unit = units.get(itemFromServer.getId());
//                if (unit != null) {
//                    units.remove(unit.getId());
//                    units.put(unit.getId(), itemFromServer);
//                } else {
//                    units.put(itemFromServer.getId(), itemFromServer);
//                }
//            }
//        }
//        mLocalDataStore.save(units.values());
//        return false;
//    }
//
//    @Override
//    protected String getTag() {
//        return "Units from";
//    }
//}
