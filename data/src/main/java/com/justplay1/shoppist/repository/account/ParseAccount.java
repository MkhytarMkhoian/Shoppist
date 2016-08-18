package com.justplay1.shoppist.repository.account;

import android.support.annotation.Nullable;

import com.justplay1.shoppist.models.AccountManager;
import com.justplay1.shoppist.models.Account;
import com.parse.ParseUser;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Mkhytar on 13.08.2016.
 */
@Singleton
public class ParseAccount implements AccountManager {

    private Account mAccount;

    @Inject
    public ParseAccount() {
    }

    @Nullable
    @Override
    public Account getCurrentAccount() {
        if (isUserAuthenticated() && mAccount == null) {
            return mAccount = new Account(ParseUser.getCurrentUser().getUsername(), ParseUser.getCurrentUser().getEmail());
        } else {
            return mAccount;
        }
    }

    @Override
    public boolean isUserAuthenticated() {
        return ParseUser.getCurrentUser() != null;
    }
}
