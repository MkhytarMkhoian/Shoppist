package com.justplay1.shoppist.communication.sync;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParseInstallation;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mkhytar on 20.10.2015.
 */
public class SyncPushBroadcastReceiver extends ParsePushBroadcastReceiver {

    public static final String SYNC_FLAG = "sync_flag";
    public static final String NOT_UPDATE_INSTALLATION = "not_update_installation";

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        try {
            JSONObject pushData = new JSONObject(intent.getStringExtra("com.parse.Data"));
            String flag = pushData.optString(SYNC_FLAG);
            String notUpdateInstallation = pushData.optString(NOT_UPDATE_INSTALLATION);

            if (flag != null && !ParseInstallation.getCurrentInstallation().getInstallationId().equals(notUpdateInstallation)) {
                ShoppistSyncAdapter.requestSync(false);
            }
        } catch (JSONException e) {
            Log.d("onPushReceive", e.getMessage());
        }
    }

    @Override
    protected void onPushDismiss(Context context, Intent intent) {
        super.onPushDismiss(context, intent);
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        super.onPushOpen(context, intent);
    }


}
