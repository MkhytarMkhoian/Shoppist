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

package com.justplay1.shoppist.presenter;

import com.justplay1.shoppist.di.scope.NonConfigurationScope;
import com.justplay1.shoppist.interactor.DefaultSubscriber;
import com.justplay1.shoppist.interactor.currency.GetCurrency;
import com.justplay1.shoppist.models.CurrencyViewModel;
import com.justplay1.shoppist.models.mappers.CurrencyModelDataMapper;
import com.justplay1.shoppist.navigation.Router;
import com.justplay1.shoppist.preferences.AppPreferences;
import com.justplay1.shoppist.presenter.base.BaseRxPresenter;
import com.justplay1.shoppist.view.ListsSettingView;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar Mkhoian.
 */
@NonConfigurationScope
public class ListsSettingPresenter extends BaseRxPresenter<ListsSettingView, Router> {

    private final CurrencyModelDataMapper mCurrencyModelDataMapper;
    private final GetCurrency mGetCurrency;
    private final AppPreferences mPreferences;

    @Inject
    ListsSettingPresenter(CurrencyModelDataMapper currencyModelDataMapper,
                          GetCurrency getCurrency,
                          AppPreferences preferences) {
        this.mCurrencyModelDataMapper = currencyModelDataMapper;
        this.mGetCurrency = getCurrency;
        this.mPreferences = preferences;
    }

    public void init() {
        if (!mPreferences.getDefaultCurrency().isEmpty()) {
            loadDefaultCurrency();
        }
    }

    private void loadDefaultCurrency() {
        mGetCurrency.setId(mPreferences.getDefaultCurrency());
        mSubscriptions.add(mGetCurrency.get()
                .flatMap(currencyModel -> {
                    if (currencyModel == null) {
                        mGetCurrency.setId(CurrencyViewModel.NO_CURRENCY_ID);
                        return mGetCurrency.get();
                    }
                    return Observable.fromCallable(() -> currencyModel);
                })
                .map(mCurrencyModelDataMapper::transformToViewModel)
                .subscribe(new DefaultSubscriber<CurrencyViewModel>() {
                    @Override
                    public void onNext(CurrencyViewModel result) {
                        if (result == null) return;
                        showData(result);
                    }
                }));
    }

    private void showData(CurrencyViewModel data) {
        if (isViewAttached()) {
            getView().showDefaultCurrency(data);
        }
    }
}
