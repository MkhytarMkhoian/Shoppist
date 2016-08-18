package com.justplay1.shoppist.repository;

import com.justplay1.shoppist.models.Account;
import com.justplay1.shoppist.models.AuthCredentials;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Created by Mkhytar on 28.04.2016.
 */
@Singleton
public class AccountDataRepository implements AccountRepository {

    @Inject
    public AccountDataRepository() {
    }

    @Override
    public void deleteAccount() throws Exception {

    }

    @Override
    public void logout() throws Exception {

    }

    @Override
    public void resetPassword(String email) throws Exception {

    }

    @Override
    public Observable<Account> singIn(AuthCredentials credentials) {
        return null;
    }

    @Override
    public Observable<Account> singUp(AuthCredentials credentials) {
        return null;
    }
}
