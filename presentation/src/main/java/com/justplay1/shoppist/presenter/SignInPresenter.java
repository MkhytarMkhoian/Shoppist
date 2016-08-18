package com.justplay1.shoppist.presenter;

import android.util.Patterns;

import com.justplay1.shoppist.interactor.DefaultSubscriber;
import com.justplay1.shoppist.interactor.account.SignIn;
import com.justplay1.shoppist.models.Account;
import com.justplay1.shoppist.models.AuthCredentials;
import com.justplay1.shoppist.presenter.base.BaseRxPresenter;
import com.justplay1.shoppist.view.SignInView;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 13.08.2016.
 */
public class SignInPresenter extends BaseRxPresenter<SignInView> {

    private final SignIn mSignIn;

    @Inject
    public SignInPresenter(SignIn signIn) {
        this.mSignIn = signIn;
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

    private void showSignInFailedMessage() {
        if (isViewAttached()) {
            getView().showSignInFailedMessage();
        }
    }

    public void onSignInClick(String email, String password) {
        if (isViewAttached()) {
            if (email.length() == 0) {
                getView().showNoEmailMessage();
            } else if (password.length() == 0) {
                getView().showNoPasswordMessage();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                getView().showInvalidEmailMessage();
            } else {
                showLoading();
                mSignIn.setAuthCredentials(new AuthCredentials(email, password));
                mSubscriptions.add(mSignIn.get().subscribe(new DefaultSubscriber<Account>() {

                    @Override
                    public void onError(Throwable e) {
                        hideLoading();
                        showSignInFailedMessage();
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
