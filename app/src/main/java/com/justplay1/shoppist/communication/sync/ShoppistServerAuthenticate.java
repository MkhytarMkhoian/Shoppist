package com.justplay1.shoppist.communication.sync;

import android.util.Log;

import com.justplay1.shoppist.communication.network.ServerConstants;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;

import bolts.Task;

/**
 * Created by Mkhytar on 03.11.2015.
 */
public class ShoppistServerAuthenticate {

    public static ParseUser userSignUp(String name, String pass) throws Exception {
        final ParseUser user = new ParseUser();
        user.setUsername(name);
        user.setPassword(pass);
        user.signUp();

        ParseInstallation.getCurrentInstallation().save();
        ParsePush.subscribeInBackground(ServerConstants.USER_CHANEL_NAME_FIRST_PART + user.getObjectId()).waitForCompletion();
        Log.d("Shoppist", "userSignUp");
        return user;
    }

    public static ParseUser userSignIn(String email, String pass) throws Exception {
        ParseUser user = ParseUser.logIn(email, pass);
        ParseInstallation.getCurrentInstallation().save();
        ParsePush.subscribeInBackground(ServerConstants.USER_CHANEL_NAME_FIRST_PART + user.getObjectId()).waitForCompletion();
        Log.d("Shoppist", "userSignIn");
        return user;
    }

    public static void logout() throws Exception {
        Task<Void> task = ParsePush.unsubscribeInBackground(ServerConstants.USER_CHANEL_NAME_FIRST_PART + ParseUser.getCurrentUser().getObjectId());
        task.waitForCompletion();
        ParseUser.logOut();
        Log.d("Shoppist", "logout");
    }
}
