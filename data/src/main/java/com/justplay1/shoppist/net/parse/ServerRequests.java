package com.justplay1.shoppist.net.parse;

import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.os.Build;
import android.util.Log;


import com.justplay1.shoppist.net.ServerConstants;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import bolts.Task;

/**
 * Created by Mkhitar on 13.06.2015.
 */
public class ServerRequests implements ServerConstants {

//    public static void logout(final Activity activity, ExecutorListener<Boolean> listener) {
//        ThreadExecutor.doNetworkTaskAsync(new Callable<Boolean>() {
//            @Override
//            public Boolean call() throws Exception {
//                deleteSyncAccount(activity);
//                ShoppistServerAuthenticate.logout();
//                return true;
//            }
//        }, listener);
//    }
//
//    private static void deleteSyncAccount(Activity activity) throws OperationCanceledException, IOException, AuthenticatorException {
//        if (App.get().mAccount != null) {
//            AccountManager accountManager = AccountManager.get(activity);
//            String authToken = accountManager.blockingGetAuthToken(App.get().mAccount, ShoppistAuthenticator.AUTHTOKEN_TYPE_FULL_ACCESS, false);
//            accountManager.invalidateAuthToken(ShoppistAuthenticator.ACCOUNT_TYPE, authToken);
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
//                accountManager.removeAccount(App.get().mAccount, activity, null, null);
//            } else {
//                accountManager.removeAccount(App.get().mAccount, null, null);
//            }
//        }
//    }
//
//    public static void deleteAccount(final Activity activity, ExecutorListener<Boolean> listener) {
//        ThreadExecutor.doNetworkTaskAsync(new Callable<Boolean>() {
//            @Override
//            public Boolean call() throws Exception {
//                List<ParseObject> toDelete = new ArrayList<>();
//                toDelete.addAll(ParseUser.getCurrentUser().getRelation(CATEGORIES).getQuery().find());
//                if (toDelete.size() > 0) {
//                    ParseObject.deleteAll(toDelete);
//                }
//                toDelete.clear();
//                toDelete.addAll(ParseUser.getCurrentUser().getRelation(UNITS).getQuery().find());
//                if (toDelete.size() > 0) {
//                    ParseObject.deleteAll(toDelete);
//                }
//                toDelete.clear();
//                toDelete.addAll(ParseUser.getCurrentUser().getRelation(CURRENCIES).getQuery().find());
//                if (toDelete.size() > 0) {
//                    ParseObject.deleteAll(toDelete);
//                }
//                toDelete.clear();
//                toDelete.addAll(ParseUser.getCurrentUser().getRelation(SHOPPING_LISTS_TABLE).getQuery().find());
//                if (toDelete.size() > 0) {
//                    ParseObject.deleteAll(toDelete);
//                }
//                toDelete.clear();
//                toDelete.addAll(ParseUser.getCurrentUser().getRelation(SHOPPING_LIST_ITEMS_TABLE).getQuery().find());
//                if (toDelete.size() > 0) {
//                    ParseObject.deleteAll(toDelete);
//                }
//                toDelete.clear();
//                toDelete.addAll(ParseUser.getCurrentUser().getRelation(GOODS_TABLE).getQuery().find());
//                if (toDelete.size() > 0) {
//                    ParseObject.deleteAll(toDelete);
//                }
//
//                deleteSyncAccount(activity);
//                Task<Void> task = ParsePush.unsubscribeInBackground(ServerConstants.USER_CHANEL_NAME_FIRST_PART + ParseUser.getCurrentUser().getObjectId());
//                task.waitForCompletion();
//                ParseUser.getCurrentUser().delete();
//                ParseUser.logOut();
//                ShoppistUtils.restoreData();
//                Log.d("Shoppist", "account delete and data restored");
//                return true;
//            }
//        }, listener);
//    }
//
//    public static void fullSync() {
//        App.get().getSyncLimitFilter().requestSync(false);
//    }
//
//    public static void singUpSync(ExecutorListener<Boolean> listener) {
//        ThreadExecutor.doNetworkTaskAsync(new Callable<Boolean>() {
//            @Override
//            public Boolean call() throws Exception {
//                long userStoredHash = ShoppistPreferences.getParseUserIdHash();
//                long currentUserHash = ParseUser.getCurrentUser().getObjectId().hashCode();
//
//                if (userStoredHash != 0 && currentUserHash != userStoredHash) {
//                    ShoppistUtils.restoreData();
//                    ShoppistPreferences.setParseUserIdHash(currentUserHash);
//                }
//                App.get().getSyncLimitFilter().requestSync(false);
//                return true;
//            }
//        }, listener);
//    }
//
//
//    public static void loadAllData() {
//        long userStoredHash = ShoppistPreferences.getParseUserIdHash();
//        long currentUserHash = ParseUser.getCurrentUser().getObjectId().hashCode();
//
//        if (currentUserHash != 0 && currentUserHash != userStoredHash) {
//            ShoppistPreferences.setParseUserIdHash(currentUserHash);
//            App.get().getSyncLimitFilter().requestSync(true, false);
//        } else {
//            App.get().getSyncLimitFilter().requestSync(false);
//        }
//    }
//
//    public static ListModel<ParseObject> getAllProducts(long updateTime) throws ParseException {
//        ParseQuery<ParseObject> query = ParseQuery.getQuery(GOODS_TABLE);
//        query.whereEqualTo(CREATED_BY, ParseUser.getCurrentUser());
//        query.whereGreaterThan(TIMESTAMP, updateTime);
//        return query.find();
//    }

}
