//package com.justplay1.shoppist.repository.sync.fromserver;
//
//import com.justplay1.shoppist.di.scopes.PerSync;
//import com.justplay1.shoppist.entity.CategoryDAO;
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
//public class CategorySyncFromServer extends SyncFromServer<CategoryDAO> {
//
//    @Inject
//    public CategorySyncFromServer(LocalDataStore<CategoryDAO> localDataStore, RemoteGetData<CategoryDAO> remoteGetData,
//                                  NotificationBuilder<CategoryDAO> builder) {
//        super(localDataStore, remoteGetData, builder);
//    }
//
//    @Override
//    protected boolean deleteDataIfNeeded(Collection<CategoryDAO> result) throws Exception {
//        mLocalDataStore.clear();
//        Map<String, CategoryDAO> categories = mLocalDataStore.getDefaultData();
//        if (result.size() > 0) {
//            for (CategoryDAO itemFromServer : result) {
//                if (itemFromServer.isDelete()) continue;
//                CategoryDAO category = categories.get(itemFromServer.getId());
//                if (category != null) {
//                    categories.remove(category.getId());
//                    categories.put(category.getId(), itemFromServer);
//                } else {
//                    categories.put(itemFromServer.getId(), itemFromServer);
//                }
//            }
//        }
//        mLocalDataStore.save(categories.values());
//        return false;
//    }
//
//    @Override
//    protected String getTag() {
//        return "Category from";
//    }
//}
