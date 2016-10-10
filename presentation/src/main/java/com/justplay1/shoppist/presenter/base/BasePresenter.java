/*
 * Copyright (C) 2016 Mkhytar Mkhoian
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.justplay1.shoppist.presenter.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

import com.justplay1.shoppist.view.BaseMvpView;

import java.lang.ref.WeakReference;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Mkhytar Mkhoian.
 */
abstract class BasePresenter<V extends BaseMvpView> implements Presenter<V> {

    private WeakReference<V> view;
    private CompositeSubscription subscriptions = new CompositeSubscription();

    @Override
    public void onCreate(Bundle arguments, Bundle savedInstanceState) {

    }

    @UiThread
    @Override
    public void attachView(V view) {
        this.view = new WeakReference<V>(view);
    }

    @UiThread
    @Override
    public void detachView() {
        subscriptions.clear();
        if (view != null) {
            view.clear();
            view = null;
        }
    }

    @UiThread
    public boolean isViewAttached() {
        return view != null && view.get() != null;
    }

    @UiThread
    @Nullable
    public V getView() {
        return view == null ? null : view.get();
    }

    protected void addSubscription(Subscription subscription) {
        this.subscriptions.add(subscription);
    }
}
