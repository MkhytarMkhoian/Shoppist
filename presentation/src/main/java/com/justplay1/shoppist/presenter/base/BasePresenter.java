package com.justplay1.shoppist.presenter.base;

import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

import com.justplay1.shoppist.view.BaseMvpView;

import java.lang.ref.WeakReference;

/**
 * Created by Mkhytar on 26.05.2016.
 */
public abstract class BasePresenter<V extends BaseMvpView> implements Presenter<V> {

    private WeakReference<V> mView;

    @UiThread
    @Override
    public void attachView(V view) {
        mView = new WeakReference<V>(view);
    }

    @UiThread
    @Override
    public void detachView() {
        if (mView != null) {
            mView.clear();
            mView = null;
        }
    }

    @UiThread
    public boolean isViewAttached() {
        return mView != null && mView.get() != null;
    }

    @UiThread
    @Nullable
    public V getView() {
        return mView == null ? null : mView.get();
    }
}
