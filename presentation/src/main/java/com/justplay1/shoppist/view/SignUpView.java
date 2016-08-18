package com.justplay1.shoppist.view;

/**
 * Created by Mkhytar on 13.08.2016.
 */
public interface SignUpView extends LoadDataView {

    String USERNAME = "USERNAME";
    String PASSWORD = "PASSWORD";
    int DEFAULT_MIN_PASSWORD_LENGTH = 6;

    void setUserName(String name);

    void setPassword(String password);

    void showNoEmailMessage();

    void showNoPasswordMessage();

    void showPasswordTooShortMessage();

    void showReenterPasswordMessage();

    void showConfirmPasswordMessage();

    void showInvalidEmailMessage();

    void showSignUpFailedMessage();
}
