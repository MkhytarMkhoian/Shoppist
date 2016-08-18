package com.justplay1.shoppist.repository;

import javax.inject.Singleton;

/**
 * Created by Mkhytar on 30.05.2016.
 */
@Singleton
public class SyncDataRepository implements SyncRepository {

//    private final DataSync mDataSync;

//    @Inject
//    public SyncDataRepository(DataSync dataSync) {
//        mDataSync = dataSync;
//    }

    @Override
    public void doSync(boolean deleteDataBefore, boolean notifyUser) {
//        mDataSync.doSync(deleteDataBefore, notifyUser);
    }
}
