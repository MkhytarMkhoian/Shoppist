//package com.justplay1.shoppist.repository.sync.fromserver;
//
//import com.justplay1.shoppist.di.scopes.PerSync;
//import com.justplay1.shoppist.entity.CurrencyDAO;
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
//public class CurrencySyncFromServer extends SyncFromServer<CurrencyDAO> {
//
//    @Inject
//    public CurrencySyncFromServer(LocalDataStore<CurrencyDAO> localDataStore, RemoteGetData<CurrencyDAO> remoteGetData,
//                                  NotificationBuilder<CurrencyDAO> builder) {
//        super(localDataStore, remoteGetData, builder);
//    }
//
//    @Override
//    protected boolean deleteDataIfNeeded(Collection<CurrencyDAO> result) throws Exception {
//        mLocalDataStore.clear();
//        Map<String, CurrencyDAO> currencies = mLocalDataStore.getDefaultData();
//        if (result.size() > 0) {
//            for (CurrencyDAO itemFromServer : result) {
//                if (itemFromServer.isDelete()) continue;
//                CurrencyDAO currency = currencies.get(itemFromServer.getId());
//                if (currency != null) {
//                    currencies.remove(currency.getId());
//                    currencies.put(currency.getId(), itemFromServer);
//                } else {
//                    currencies.put(itemFromServer.getId(), itemFromServer);
//                }
//            }
//        }
//        mLocalDataStore.save(currencies.values());
//        return false;
//    }
//
//    @Override
//    protected String getTag() {
//        return "Currency from";
//    }
//}
