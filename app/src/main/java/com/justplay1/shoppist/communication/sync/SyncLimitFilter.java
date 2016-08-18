package com.justplay1.shoppist.communication.sync;

import android.os.Handler;

/**
 * Created by Mkhytar on 21.02.2016.
 */
public class SyncLimitFilter {

    public static final long LIMIT = 30 * 1000;

    private Handler mHandler;
    private volatile boolean isNeedSync = false;
    private volatile boolean isSyncAvailable = true;

    public SyncLimitFilter() {
        mHandler = new Handler();
    }

    public void requestSync(final boolean deleteDataBefore){
        requestSync(deleteDataBefore, true);
    }

    public synchronized void requestSync(final boolean deleteDataBefore, final boolean notifyUser){
        if (deleteDataBefore){
            ShoppistSyncAdapter.requestSync(true, notifyUser);
            return;
        }

        if (isSyncAvailable){
            isSyncAvailable = false;
            ShoppistSyncAdapter.requestSync(false, notifyUser);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isSyncAvailable = true;
                    if (isNeedSync){
                        isNeedSync = false;
                        requestSync(false, notifyUser);
                    }
                    mHandler.removeCallbacks(this);
                }
            }, LIMIT);
        } else {
            isNeedSync = true;
        }
    }
}
