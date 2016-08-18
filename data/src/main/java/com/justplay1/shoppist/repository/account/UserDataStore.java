package com.justplay1.shoppist.repository.account;

/**
 * Created by Mkhytar on 02.06.2016.
 */
public interface UserDataStore {

    void deleteAccount() throws Exception;

    void logout() throws Exception;

    void login(String email, String password) throws Exception;

    void singUp(String email, String password) throws Exception;
}
