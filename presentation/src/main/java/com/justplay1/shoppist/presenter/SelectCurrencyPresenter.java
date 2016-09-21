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

import android.os.Bundle;

import com.justplay1.shoppist.interactor.DefaultSubscriber;
import com.justplay1.shoppist.interactor.currency.GetCurrencyList;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.CurrencyViewModel;
import com.justplay1.shoppist.models.mappers.CurrencyModelDataMapper;
import com.justplay1.shoppist.navigation.Router;
import com.justplay1.shoppist.presenter.base.BaseRxPresenter;
import com.justplay1.shoppist.view.SelectCurrencyView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar Mkhoian.
 */
public class SelectCurrencyPresenter extends BaseRxPresenter<SelectCurrencyView, Router> {

    private final CurrencyModelDataMapper mDataMapper;
    private final GetCurrencyList mGetCurrencyList;

    private CurrencyViewModel mItem;

    @Inject
    public SelectCurrencyPresenter(CurrencyModelDataMapper dataMapper, GetCurrencyList getCurrencyList) {
        this.mDataMapper = dataMapper;
        this.mGetCurrencyList = getCurrencyList;
    }

    @Override
    public void onCreate(Bundle arguments, Bundle savedInstanceState) {
        super.onCreate(arguments, savedInstanceState);
        if (arguments != null) {
            mItem = arguments.getParcelable(CurrencyViewModel.class.getName());
        } else if (savedInstanceState != null) {
            mItem = savedInstanceState.getParcelable(CurrencyViewModel.class.getName());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putParcelable(CurrencyViewModel.class.getName(), mItem);
    }

    public void init() {
        loadCurrencies();
    }

    public void onPositiveButtonClick(CurrencyViewModel currency) {
        onComplete(currency, false);
        closeDialog();
    }

    public void onNegativeButtonClick() {
        closeDialog();
    }

    public void onAddCurrencyClick() {
        showCurrencyDialog(null);
    }

    public void onEditCurrencyClick(CurrencyViewModel currency) {
        showCurrencyDialog(currency);
    }

    public void loadCurrencies() {
        mSubscriptions.add(mGetCurrencyList.get()
                .map(mDataMapper::transformToViewModel)
                .subscribe(new DefaultSubscriber<List<CurrencyViewModel>>() {

                    @Override
                    public void onNext(List<CurrencyViewModel> currencies) {
                        setCurrencies(currencies);
                        if (mItem != null) {
                            selectCurrency(mItem.getCategory().getId());
                        } else {
                            selectCurrency(CategoryViewModel.NO_CATEGORY_ID);
                        }
                    }
                }));
    }

    private void setCurrencies(List<CurrencyViewModel> currencies) {
        if (isViewAttached()) {
            getView().setCurrencies(currencies);
        }
    }

    private void selectCurrency(String id) {
        if (isViewAttached()) {
            getView().selectCurrency(id);
        }
    }

    private void onComplete(CurrencyViewModel currency, boolean isUpdate) {
        if (isViewAttached()) {
            getView().onComplete(currency, isUpdate);
        }
    }

    private void closeDialog() {
        if (isViewAttached()) {
            getView().closeDialog();
        }
    }

    private void showCurrencyDialog(CurrencyViewModel currency) {
        if (isViewAttached()) {
            getView().showCurrencyDialog(currency);
        }
    }
}
