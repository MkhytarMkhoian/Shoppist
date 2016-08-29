package com.justplay1.shoppist.presenter;

import com.justplay1.shoppist.interactor.DefaultSubscriber;
import com.justplay1.shoppist.interactor.notification.GetNewNotificationCount;
import com.justplay1.shoppist.models.AccountManager;
import com.justplay1.shoppist.preferences.ShoppistPreferences;
import com.justplay1.shoppist.presenter.base.BaseRxPresenter;
import com.justplay1.shoppist.view.MenuView;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 09.07.2016.
 */
public class MenuPresenter extends BaseRxPresenter<MenuView> {

    private final AccountManager mAccountManager;
    private final ShoppistPreferences mPreferences;
    private final GetNewNotificationCount mGetNewNotificationCount;

    @Inject
    public MenuPresenter(GetNewNotificationCount getNewNotificationCount,
                         ShoppistPreferences preferences,
                         AccountManager accountManager) {
        this.mGetNewNotificationCount = getNewNotificationCount;
        this.mPreferences = preferences;
        this.mAccountManager = accountManager;
    }

    public void init() {
//        setAccount();
//        getNewNotificationCount();
    }

    private void getNewNotificationCount() {
        mGetNewNotificationCount.setTimestamp(mPreferences.getLastUserSeenNotificationsTime());
        mSubscriptions.add(mGetNewNotificationCount.get()
                .subscribe(new GetNewNotificationCountSubscriber()));
    }

    private void setAccount() {
        if (isViewAttached()) {
            if (mAccountManager.isUserAuthenticated()) {
                getView().setAccount(mAccountManager.getCurrentAccount().getName());
            } else {
                getView().setDefaultAccountTitle();
            }
        }
    }

    private void setNotificationBadge(int count) {
        if (isViewAttached()) {
            getView().setNotificationBadge(count);
        }
    }

    public void onHeaderClick() {
//        if (isViewAttached()) {
//            if (mAccountManager.isUserAuthenticated()) {
//                getView().openLogin();
//            } else {
//                getView().openAccountSetting();
//            }
//        }
    }

    private final class GetNewNotificationCountSubscriber extends DefaultSubscriber<Integer> {

        @Override
        public void onNext(Integer count) {
            MenuPresenter.this.setNotificationBadge(count);
        }
    }
}
