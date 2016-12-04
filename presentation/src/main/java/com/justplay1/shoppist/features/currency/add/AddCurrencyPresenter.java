/*
 * Copyright (C) 2016 Mkhytar Mkhoian
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.justplay1.shoppist.features.currency.add;

import android.os.Bundle;

import com.justplay1.shoppist.di.scope.NonConfigurationScope;
import com.justplay1.shoppist.interactor.DefaultSubscriber;
import com.justplay1.shoppist.interactor.currency.AddCurrency;
import com.justplay1.shoppist.interactor.currency.UpdateCurrency;
import com.justplay1.shoppist.models.CurrencyViewModel;
import com.justplay1.shoppist.models.mappers.CurrencyViewModelMapper;
import com.justplay1.shoppist.shared.base.presenters.BaseRouterPresenter;
import com.justplay1.shoppist.shared.navigation.Router;
import com.justplay1.shoppist.utils.ModelUtils;

import java.util.Collections;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar Mkhoian.
 */
@NonConfigurationScope
public class AddCurrencyPresenter extends BaseRouterPresenter<AddCurrencyView, Router> {

    private final CurrencyViewModelMapper dataMapper;
    private final UpdateCurrency updateCurrency;
    private final AddCurrency addCurrency;

    private CurrencyViewModel item;

    @Inject
    AddCurrencyPresenter(CurrencyViewModelMapper dataMapper,
                         UpdateCurrency updateCurrency,
                         AddCurrency addCurrency) {
        this.dataMapper = dataMapper;
        this.updateCurrency = updateCurrency;
        this.addCurrency = addCurrency;
    }

    @Override
    public void onCreate(Bundle arguments, Bundle savedInstanceState) {
        super.onCreate(arguments, savedInstanceState);
        if (arguments != null) {
            item = arguments.getParcelable(CurrencyViewModel.class.getName());
        }
    }

    @Override
    public void attachView(AddCurrencyView view) {
        super.attachView(view);
        if (item != null) {
            setName(item.getName());
            setDefaultUpdateTitle();
        } else {
            setDefaultNewTitle();
        }
    }

    public void onPositiveButtonClick(String name) {
        saveCurrency(name);
    }

    public void onNegativeButtonClick() {
        closeDialog();
    }

    private void saveCurrency(String name) {
        if (checkDataForErrors(name)) {
            CurrencyViewModel currency = new CurrencyViewModel();
            if (item != null) {
                currency.setId(item.getId());
                currency.setName(name);
                updateCurrency(currency);
            } else {
                currency.setId(ModelUtils.generateId());
                currency.setName(name);
                addCurrency(currency);
            }
        }
    }

    private void addCurrency(CurrencyViewModel data) {
        showLoading();
        addSubscription(Observable.fromCallable(() -> dataMapper.transform(data))
                .flatMap(currency -> addCurrency.init(Collections.singletonList(currency)).get())
                .subscribe(new SaveCurrencySubscriber(true)));
    }

    private void updateCurrency(CurrencyViewModel data) {
        showLoading();
        addSubscription(Observable.fromCallable(() -> dataMapper.transform(data))
                .flatMap(currency -> updateCurrency.init(Collections.singletonList(currency)).get())
                .subscribe(new SaveCurrencySubscriber(false)));
    }

    private boolean checkDataForErrors(String name) {
        if (name.isEmpty()) {
            showNameIsRequiredError();
            return false;
        } else if (name.length() > 3) {
            return false;
        }
        return true;
    }

    private void showLoading() {
        if (isViewAttached()) {
            getView().showLoading();
        }
    }

    private void hideLoading() {
        if (isViewAttached()) {
            getView().hideLoading();
        }
    }

    private void onComplete(boolean isUpdate) {
        if (isViewAttached()) {
            getView().onComplete(isUpdate);
        }
    }

    private void showNameIsRequiredError() {
        if (isViewAttached()) {
            getView().showNameIsRequiredError();
        }
    }

    private void setName(String name) {
        if (isViewAttached()) {
            getView().setName(name);
        }
    }

    private void setDefaultNewTitle() {
        if (isViewAttached()) {
            getView().setDefaultNewTitle();
        }
    }

    private void setDefaultUpdateTitle() {
        if (isViewAttached()) {
            getView().setDefaultUpdateTitle();
        }
    }

    private void closeDialog() {
        if (isViewAttached()) {
            getView().closeDialog();
        }
    }

    private final class SaveCurrencySubscriber extends DefaultSubscriber<Boolean> {

        private boolean isAddAction;

        public SaveCurrencySubscriber(boolean isAddAction) {
            this.isAddAction = isAddAction;
        }

        @Override
        public void onError(Throwable e) {
            hideLoading();
        }

        @Override
        public void onNext(Boolean result) {
            hideLoading();
            if (result) {
                if (isAddAction) {
                    onComplete(false);
                } else {
                    onComplete(true);
                }
                closeDialog();
            }
        }
    }
}
