package com.justplay1.shoppist.repository.account;

import android.util.Log;

import com.justplay1.shoppist.net.ServerConstants;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import bolts.Task;

/**
 * Created by Mkhytar on 02.06.2016.
 */
@Singleton
public class UserDataStoreImpl implements UserDataStore {

    @Inject
    public UserDataStoreImpl() {
    }

    @Override
    public void deleteAccount() throws Exception {
        List<ParseObject> toDelete = new ArrayList<>();
        toDelete.addAll(ParseUser.getCurrentUser().getRelation(ServerConstants.CATEGORIES).getQuery().find());
        if (toDelete.size() > 0) {
            ParseObject.deleteAll(toDelete);
        }
        toDelete.clear();
        toDelete.addAll(ParseUser.getCurrentUser().getRelation(ServerConstants.UNITS).getQuery().find());
        if (toDelete.size() > 0) {
            ParseObject.deleteAll(toDelete);
        }
        toDelete.clear();
        toDelete.addAll(ParseUser.getCurrentUser().getRelation(ServerConstants.CURRENCIES).getQuery().find());
        if (toDelete.size() > 0) {
            ParseObject.deleteAll(toDelete);
        }
        toDelete.clear();
        toDelete.addAll(ParseUser.getCurrentUser().getRelation(ServerConstants.SHOPPING_LISTS_TABLE).getQuery().find());
        if (toDelete.size() > 0) {
            ParseObject.deleteAll(toDelete);
        }
        toDelete.clear();
        toDelete.addAll(ParseUser.getCurrentUser().getRelation(ServerConstants.SHOPPING_LIST_ITEMS_TABLE).getQuery().find());
        if (toDelete.size() > 0) {
            ParseObject.deleteAll(toDelete);
        }
        toDelete.clear();
        toDelete.addAll(ParseUser.getCurrentUser().getRelation(ServerConstants.GOODS_TABLE).getQuery().find());
        if (toDelete.size() > 0) {
            ParseObject.deleteAll(toDelete);
        }

        Task<Void> task = ParsePush.unsubscribeInBackground(ServerConstants.USER_CHANEL_NAME_FIRST_PART + ParseUser.getCurrentUser().getObjectId());
        task.waitForCompletion();
        ParseUser.getCurrentUser().delete();
        ParseUser.logOut();
    }

    @Override
    public void logout() throws Exception {
        Task<Void> task = ParsePush.unsubscribeInBackground(ServerConstants.USER_CHANEL_NAME_FIRST_PART + ParseUser.getCurrentUser().getObjectId());
        task.waitForCompletion();
        ParseUser.logOut();
        Log.d("Shoppist", "logout");
    }

    @Override
    public void login(String email, String password) throws Exception {
        ParseUser user = ParseUser.logIn(email, password);
        ParseInstallation.getCurrentInstallation().save();
        ParsePush.subscribeInBackground(ServerConstants.USER_CHANEL_NAME_FIRST_PART + user.getObjectId()).waitForCompletion();
        Log.d("Shoppist", "userSignIn");
    }

    @Override
    public void singUp(String email, String password) throws Exception {
        final ParseUser user = new ParseUser();
        user.setUsername(email);
        user.setPassword(password);
        user.signUp();

        ParseInstallation.getCurrentInstallation().save();
        ParsePush.subscribeInBackground(ServerConstants.USER_CHANEL_NAME_FIRST_PART + user.getObjectId()).waitForCompletion();
        Log.d("Shoppist", "userSignUp");
    }
}
