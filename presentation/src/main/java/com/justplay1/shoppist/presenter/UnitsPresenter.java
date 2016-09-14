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

import com.justplay1.shoppist.interactor.DefaultSubscriber;
import com.justplay1.shoppist.interactor.units.DeleteUnits;
import com.justplay1.shoppist.interactor.units.GetUnits;
import com.justplay1.shoppist.models.UnitViewModel;
import com.justplay1.shoppist.models.mappers.UnitsDataModelMapper;
import com.justplay1.shoppist.preferences.ShoppistPreferences;
import com.justplay1.shoppist.presenter.base.BaseRxPresenter;
import com.justplay1.shoppist.view.UnitsView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar Mkhoian.
 */
public class UnitsPresenter extends BaseRxPresenter<UnitsView> {

    private final UnitsDataModelMapper mDataMapper;
    private final ShoppistPreferences mPreferences;
    private final GetUnits mGetUnits;
    private final DeleteUnits mDeleteUnits;

    @Inject
    public UnitsPresenter(UnitsDataModelMapper unitsDataModelMapper,
                          ShoppistPreferences preferences,
                          GetUnits getUnits,
                          DeleteUnits deleteUnits) {
        this.mDataMapper = unitsDataModelMapper;
        this.mPreferences = preferences;
        this.mGetUnits = getUnits;
        this.mDeleteUnits = deleteUnits;
    }

    public void init() {
        loadData();
    }

    public void loadData() {
        showLoading();
        mSubscriptions.add(mGetUnits.get()
                .map(mDataMapper::transformToViewModel)
                .subscribe(new DefaultSubscriber<List<UnitViewModel>>() {
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

    public void onAddButtonClick() {
        openUnitAddDialog(null);
    }

    public void onListItemClick(UnitViewModel unit) {
        openUnitAddDialog(unit);
    }

    private void openUnitAddDialog(UnitViewModel unit) {
        if (isViewAttached()) {
            getView().openUnitAddDialog(unit);
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
