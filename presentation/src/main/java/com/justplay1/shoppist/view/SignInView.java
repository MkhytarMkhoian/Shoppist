package com.justplay1.shoppist.view;

/**
 * Created by Mkhytar on 13.08.2016.
 */
public interface SignInView extends LoadDataView{

    void showNoEmailMessage();

    void showNoPasswordMessage();

    void showInvalidEmailMessage();

    void showSignInFailedMessage();
}
