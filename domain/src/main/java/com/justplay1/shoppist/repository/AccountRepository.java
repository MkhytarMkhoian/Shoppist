package com.justplay1.shoppist.repository;

import com.justplay1.shoppist.models.Account;
import com.justplay1.shoppist.models.AuthCredentials;

import rx.Observable;

/**
 * Created by Mkhytar on 29.05.2016.
 */
public interface AccountRepository {

    void deleteAccount() throws Exception;

    void logout() throws Exception;

    void resetPassword(String email) throws Exception;

    Observable<Account> singIn(AuthCredentials credentials);

    Observable<Account> singUp(AuthCredentials credentials);
}
