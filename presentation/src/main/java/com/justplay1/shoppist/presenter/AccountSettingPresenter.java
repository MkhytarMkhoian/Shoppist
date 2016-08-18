package com.justplay1.shoppist.presenter;

import android.os.Bundle;

import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.models.AccountManager;
import com.justplay1.shoppist.interactor.DefaultSubscriber;
import com.justplay1.shoppist.interactor.account.DeleteAccount;
import com.justplay1.shoppist.interactor.account.LogOut;
import com.justplay1.shoppist.preferences.ShoppistPreferences;
import com.justplay1.shoppist.presenter.base.BaseRxPresenter;
import com.justplay1.shoppist.view.AccountSettingView;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 11.08.2016.
 */
@PerActivity
public class AccountSettingPresenter extends BaseRxPresenter<AccountSettingView> {

    private final DeleteAccount mDeleteAccount;
    private final LogOut mLogOut;
    private final ShoppistPreferences mPreferences;
    private final AccountManager mAccountManager;

    @Inject
    public AccountSettingPresenter(DeleteAccount deleteAccount,
                                   LogOut logOut,
                                   ShoppistPreferences preferences,
                                   AccountManager accountManager) {
        this.mDeleteAccount = deleteAccount;
        this.mLogOut = logOut;
        this.mPreferences = preferences;
        this.mAccountManager = accountManager;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {

    }

    public void init() {
        if (mAccountManager.isUserAuthenticated()) {
            setAccountName(mAccountManager.getCurrentAccount().getName());
        } else {
            disableAccountButtons();
        }
    }

    public void logOut() {
        showLoading();
        mSubscriptions.add(mLogOut.get()
                .subscribe(new DefaultSubscriber<Boolean>() {

                    @Override
                    public void onError(Throwable e) {
                        hideLoading();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        setAccountName("");
                        disableAccountButtons();
                        hideLoading();
                    }
                }));

    }

    public void deleteAccount() {
        showLoading();
        mSubscriptions.add(mDeleteAccount.get()
                .subscribe(new DefaultSubscriber<Boolean>() {

                    @Override
                    public void onError(Throwable e) {
                        hideLoading();
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        setAccountName("");
                        disableAccountButtons();
                        hideLoading();
                    }
                }));
    }

    private void hideLoading() {
        if (isViewAttached()) {
            getView().hideLoading();
        }
    }

    private void showLoading() {
        if (isViewAttached()) {
            getView().showLoading();
        }
    }

    private void disableAccountButtons() {
        if (isViewAttached()) {
            getView().disableAccountButtons();
        }
    }

    private void setAccountName(String name) {
        if (isViewAttached()) {
            getView().setAccountName(name);
        }
    }
}
