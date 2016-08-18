package com.justplay1.shoppist.communication.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Mkhytar on 20.10.2015.
 */
public class AuthenticatorService extends Service {

    private ShoppistAuthenticator mShoppistAuthenticator;

    @Override
    public void onCreate() {
        super.onCreate();
        mShoppistAuthenticator = new ShoppistAuthenticator(getApplicationContext());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mShoppistAuthenticator.getIBinder();
    }

}
