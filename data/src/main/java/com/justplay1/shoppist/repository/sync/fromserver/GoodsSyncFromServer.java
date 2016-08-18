//package com.justplay1.shoppist.repository.sync.fromserver;
//
//import com.justplay1.shoppist.di.scopes.PerSync;
//import com.justplay1.shoppist.entity.ProductDAO;
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
//public class GoodsSyncFromServer extends SyncFromServer<ProductDAO> {
//
//    @Inject
//    public GoodsSyncFromServer(LocalDataStore<ProductDAO> localDataStore, RemoteGetData<ProductDAO> remoteGetData,
//                               NotificationBuilder<ProductDAO> builder) {
//        super(localDataStore, remoteGetData, builder);
//    }
//
//    @Override
//    protected boolean deleteDataIfNeeded(Collection<ProductDAO> result) throws Exception {
//        mLocalDataStore.clear();
//        Map<String, ProductDAO> standardProducts = mLocalDataStore.getDefaultData();
//        if (result.size() > 0) {
//            for (ProductDAO itemFromServer : result) {
//                if (itemFromServer.isDelete()) continue;
//                ProductDAO product = standardProducts.get(itemFromServer.getId());
//                if (product != null) {
//                    standardProducts.remove(product.getId());
//                    standardProducts.put(product.getId(), itemFromServer);
//                } else {
//                    standardProducts.put(itemFromServer.getId(), itemFromServer);
//                }
//            }
//        }
//        mLocalDataStore.save(standardProducts.values());
//        return false;
//    }
//
//    @Override
//    protected String getTag() {
//        return "Goods from";
//    }
//}
