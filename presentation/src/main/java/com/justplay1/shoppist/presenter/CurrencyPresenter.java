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
import com.justplay1.shoppist.interactor.currency.DeleteCurrency;
import com.justplay1.shoppist.interactor.currency.GetCurrencyList;
import com.justplay1.shoppist.models.CurrencyViewModel;
import com.justplay1.shoppist.models.mappers.CurrencyViewModelMapper;
import com.justplay1.shoppist.navigation.CurrencyRouter;
import com.justplay1.shoppist.presenter.base.BaseRouterPresenter;
import com.justplay1.shoppist.view.CurrencyView;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by Mkhytar Mkhoian.
 */
@NonConfigurationScope
public class CurrencyPresenter extends BaseRouterPresenter<CurrencyView, CurrencyRouter> {

    private final BehaviorSubject<List<CurrencyViewModel>> cache = BehaviorSubject.create();

    private final CurrencyViewModelMapper dataMapper;
    private final GetCurrencyList getCurrencyList;
    private final DeleteCurrency deleteCurrency;

    @Inject
    CurrencyPresenter(GetCurrencyList getCurrencyList,
                      DeleteCurrency deleteCurrency,
                      CurrencyViewModelMapper dataMapper) {
        this.getCurrencyList = getCurrencyList;
        this.deleteCurrency = deleteCurrency;
        this.dataMapper = dataMapper;

        loadData();
    }

    private void loadData() {
        getCurrencyList.get()
                .map(dataMapper::transformToViewModel)
                .subscribe(cache);
    }

    @Override
    public void attachView(CurrencyView view) {
        super.attachView(view);
        addSubscription(cache.subscribe(new DefaultSubscriber<List<CurrencyViewModel>>() {

            @Override
            public void onNext(List<CurrencyViewModel> currencyViewModels) {
                showData(currencyViewModels);
            }
        }));
    }

    public void onAddButtonClick() {
        if (hasRouter()) {
            getRouter().showCurrencyEditDialog(null);
        }
    }

    public void onListItemClick(CurrencyViewModel currency) {
        if (hasRouter()) {
            getRouter().showCurrencyEditDialog(currency);
        }
    }

    private void showData(List<CurrencyViewModel> data) {
        if (isViewAttached()) {
            getView().showData(data);
        }
    }

    public void deleteItems(Collection<CurrencyViewModel> data) {
        addSubscription(Observable.fromCallable(() -> dataMapper.transform(data))
                .flatMap(items -> deleteCurrency.init(items).get())
                .subscribe(new DefaultSubscriber<>()));
    }
}
