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
import android.support.annotation.UiThread;

import com.justplay1.shoppist.view.BaseMvpView;

import rx.Observable;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Mkhytar Mkhoian.
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
