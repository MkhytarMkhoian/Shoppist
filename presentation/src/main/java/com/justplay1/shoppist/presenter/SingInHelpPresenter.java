package com.justplay1.shoppist.presenter;

import android.util.Patterns;

import com.justplay1.shoppist.interactor.DefaultSubscriber;
import com.justplay1.shoppist.interactor.account.ResetPassword;
import com.justplay1.shoppist.presenter.base.BaseRxPresenter;
import com.justplay1.shoppist.view.SingInHelpView;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 13.08.2016.
 */
public class SingInHelpPresenter extends BaseRxPresenter<SingInHelpView> {

    private final ResetPassword mResetPassword;

    @Inject
    public SingInHelpPresenter(ResetPassword resetPassword) {
        this.mResetPassword = resetPassword;
    }

    private void hideLoading() {
        if (isViewAttached()) {
            getView().hideLoading();
        }
    }

    private void showInstructions() {
        if (isViewAttached()) {
            getView().showInstructions();
        }
    }

    public void onSubmitClick(String email) {
        if (isViewAttached()) {
            if (email.length() == 0) {
                getView().showNoEmailMessage();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                getView().showInvalidEmailMessage();
            } else {
                getView().showLoading();
                mResetPassword.setEmail(email);
                mSubscriptions.add(mResetPassword.get()
                        .subscribe(new DefaultSubscriber<Boolean>() {

                            @Override
                            public void onError(Throwable e) {
                                hideLoading();
                            }

                            @Override
                            public void onNext(Boolean aBoolean) {
                                hideLoading();
                                showInstructions();
                            }
                        }));
            }
        }
    }
}
