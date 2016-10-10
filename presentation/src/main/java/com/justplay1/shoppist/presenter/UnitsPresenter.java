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
import com.justplay1.shoppist.interactor.units.DeleteUnits;
import com.justplay1.shoppist.interactor.units.GetUnitsList;
import com.justplay1.shoppist.models.UnitViewModel;
import com.justplay1.shoppist.models.mappers.UnitsDataModelMapper;
import com.justplay1.shoppist.navigation.UnitRouter;
import com.justplay1.shoppist.presenter.base.BaseRouterPresenter;
import com.justplay1.shoppist.view.UnitsView;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by Mkhytar Mkhoian.
 */
@NonConfigurationScope
public class UnitsPresenter extends BaseRouterPresenter<UnitsView, UnitRouter> {

    private final BehaviorSubject<List<UnitViewModel>> cache = BehaviorSubject.create();

    private final UnitsDataModelMapper dataMapper;
    private final GetUnitsList getUnitsList;
    private final DeleteUnits deleteUnits;

    @Inject
    UnitsPresenter(UnitsDataModelMapper unitsDataModelMapper,
                   GetUnitsList getUnitsList,
                   DeleteUnits deleteUnits) {
        this.dataMapper = unitsDataModelMapper;
        this.getUnitsList = getUnitsList;
        this.deleteUnits = deleteUnits;

        loadData();
    }

    @Override
    public void attachView(UnitsView view) {
        super.attachView(view);
        addSubscription(cache.subscribe(new DefaultSubscriber<List<UnitViewModel>>() {

            @Override
            public void onNext(List<UnitViewModel> currencyViewModels) {
                hideLoading();
                showData(currencyViewModels);
            }

            @Override
            public void onError(Throwable e) {
                hideLoading();
                e.printStackTrace();
            }
        }));
    }

    private void loadData() {
        getUnitsList.get()
                .map(dataMapper::transformToViewModel)
                .subscribe(cache);
    }

    public void deleteItems(Collection<UnitViewModel> data) {
        addSubscription(Observable.fromCallable(() -> dataMapper.transform(data))
                .flatMap(items -> {
                    deleteUnits.setData(items);
                    return deleteUnits.get();
                }).subscribe(new DefaultSubscriber<>()));
    }

    public void onAddButtonClick() {
        if (hasRouter()) {
            getRouter().openUnitEditDialog(null);
        }
    }

    public void onListItemClick(UnitViewModel unit) {
        if (hasRouter()) {
            getRouter().openUnitEditDialog(unit);
        }
    }

    private void showData(List<UnitViewModel> data) {
        if (isViewAttached()) {
            getView().showData(data);
        }
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
}
