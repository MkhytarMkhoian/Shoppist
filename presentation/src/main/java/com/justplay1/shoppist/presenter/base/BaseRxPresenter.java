package com.justplay1.shoppist.presenter.base;

import android.os.Bundle;
import android.support.annotation.UiThread;

import com.justplay1.shoppist.view.BaseMvpView;

import rx.Observable;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Mkhytar on 02.07.2016.
 */
public abstract class BaseRxPresenter<V extends BaseMvpView> extends BasePresenter<V> {

    protected CompositeSubscription mSubscriptions;

    public BaseRxPresenter() {
        mSubscriptions = new CompositeSubscription();
    }

    @UiThread
    @Override
    public void onCreate(Bundle arguments, Bundle savedInstanceState) {

    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {

    }

    @UiThread
    @Override
    public void onDestroy() {
        mSubscriptions.clear();
    }

    public static <T> Observable.Transformer<T, T> applyOpBeforeAndAfter(Runnable before, Runnable after) {
        return tObservable -> tObservable
                .doOnSubscribe(before::run)
                .doOnTerminate(after::run);
    }
}
