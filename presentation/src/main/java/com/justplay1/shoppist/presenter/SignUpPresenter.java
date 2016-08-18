package com.justplay1.shoppist.presenter;

import android.os.Bundle;
import android.util.Patterns;

import com.justplay1.shoppist.interactor.DefaultSubscriber;
import com.justplay1.shoppist.interactor.account.SignUp;
import com.justplay1.shoppist.models.Account;
import com.justplay1.shoppist.models.AuthCredentials;
import com.justplay1.shoppist.presenter.base.BaseRxPresenter;
import com.justplay1.shoppist.view.SignUpView;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 13.08.2016.
 */
public class SignUpPresenter extends BaseRxPresenter<SignUpView> {

    private final SignUp mSignUp;

    private String mUserName = "";
    private String mPassword = "";

    @Inject
    public SignUpPresenter(SignUp signUp) {
        this.mSignUp = signUp;
    }

    @Override
    public void onCreate(Bundle arguments, Bundle savedInstanceState) {
        super.onCreate(arguments, savedInstanceState);
        if (arguments != null) {
            mUserName = arguments.getString(SignUpView.USERNAME);
            mPassword = arguments.getString(SignUpView.PASSWORD);
        } else if (savedInstanceState != null) {
            mUserName = savedInstanceState.getString(SignUpView.USERNAME);
            mPassword = savedInstanceState.getString(SignUpView.PASSWORD);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putString(SignUpView.USERNAME, mUserName);
        bundle.putString(SignUpView.PASSWORD, mPassword);
    }

    public void init() {
        setUserName(mUserName);
        setPassword(mPassword);
    }

    private void setPassword(String password) {
        if (isViewAttached()) {
            getView().setPassword(password);
        }
    }

    private void setUserName(String name) {
        if (isViewAttached()) {
            getView().setUserName(name);
        }
    }

    private void showLoading() {
        if (isViewAttached()) {
            getView().showLoading();
        }
    }

    private void hideLoading() {
        if (isViewAttached()) {
            getView().hideLoading();
        }
    }

    private void showSignUpFailedMessage() {
        if (isViewAttached()) {
            getView().showSignUpFailedMessage();
        }
    }

    public void onSignUpClick(String email, String password, String passwordAgain) {
        mPassword = password;
        mUserName = email;
        if (isViewAttached()) {
            if (email.length() == 0) {
                getView().showNoEmailMessage();
            } else if (password.length() == 0) {
                getView().showNoPasswordMessage();
            } else if (password.length() < SignUpView.DEFAULT_MIN_PASSWORD_LENGTH) {
                getView().showPasswordTooShortMessage();
            } else if (passwordAgain.length() == 0) {
                getView().showReenterPasswordMessage();
            } else if (!password.equals(passwordAgain)) {
                getView().showConfirmPasswordMessage();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                getView().showInvalidEmailMessage();
            } else {
                showLoading();
                mSignUp.setAuthCredentials(new AuthCredentials(email, password));
                mSubscriptions.add(mSignUp.get().subscribe(new DefaultSubscriber<Account>() {

                    @Override
                    public void onError(Throwable e) {
                        hideLoading();
                        showSignUpFailedMessage();
                    }

                    @Override
                    public void onNext(Account account) {
                        hideLoading();
                    }

                }));
            }
        }
    }
}
