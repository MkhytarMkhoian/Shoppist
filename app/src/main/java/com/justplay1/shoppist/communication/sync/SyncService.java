package com.justplay1.shoppist.communication.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Mkhytar on 28.09.2015.
 */
public class SyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();
    private static ShoppistSyncAdapter sSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new ShoppistSyncAdapter(getApplicationContext(), false);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
