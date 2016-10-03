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

import com.justplay1.shoppist.di.scope.NonConfigurationScope;
import com.justplay1.shoppist.interactor.DefaultSubscriber;
import com.justplay1.shoppist.interactor.units.GetUnitsList;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.UnitViewModel;
import com.justplay1.shoppist.models.mappers.UnitsDataModelMapper;
import com.justplay1.shoppist.navigation.Router;
import com.justplay1.shoppist.presenter.base.BaseRxPresenter;
import com.justplay1.shoppist.view.SelectUnitView;

import java.util.List;

import javax.inject.Inject;

import rx.subjects.BehaviorSubject;

/**
 * Created by Mkhytar Mkhoian.
 */
@NonConfigurationScope
public class SelectUnitPresenter extends BaseRxPresenter<SelectUnitView, Router> {

    private final BehaviorSubject<List<UnitViewModel>> cache = BehaviorSubject.create();

    private final UnitsDataModelMapper mDataMapper;
    private final GetUnitsList mGetUnitsList;

    private UnitViewModel mItem;

    @Inject
    SelectUnitPresenter(UnitsDataModelMapper dataMapper, GetUnitsList getUnitsList) {
        this.mDataMapper = dataMapper;
        this.mGetUnitsList = getUnitsList;

        loadUnits();
    }

    @Override
    public void onCreate(Bundle arguments, Bundle savedInstanceState) {
        super.onCreate(arguments, savedInstanceState);
        if (arguments != null) {
            mItem = arguments.getParcelable(UnitViewModel.class.getName());
        }
    }

    @Override
    public void attachView(SelectUnitView view) {
        super.attachView(view);
        mSubscriptions.add(cache.subscribe(new DefaultSubscriber<List<UnitViewModel>>() {

            @Override
            public void onNext(List<UnitViewModel> units) {
                setUnits(units);
                if (mItem != null) {
                    selectUnit(mItem.getCategory().getId());
                } else {
                    selectUnit(CategoryViewModel.NO_CATEGORY_ID);
                }
            }
        }));
    }

    public void onPositiveButtonClick(UnitViewModel unit) {
        onComplete(unit, false);
        closeDialog();
    }

    public void onNegativeButtonClick() {
        closeDialog();
    }

    public void onAddUnitClick() {
        showUnitDialog(null);
    }

    public void onEditUnitClick(UnitViewModel editUnit) {
        showUnitDialog(editUnit);
    }

    public void loadUnits() {
        mGetUnitsList.get()
                .map(mDataMapper::transformToViewModel)
                .subscribe(cache);
    }

    private void setUnits(List<UnitViewModel> units) {
        if (isViewAttached()) {
            getView().setUnits(units);
        }
    }

    private void selectUnit(String id) {
        if (isViewAttached()) {
            getView().selectUnit(id);
        }
    }

    private void onComplete(UnitViewModel unit, boolean isUpdate) {
        if (isViewAttached()) {
            getView().onComplete(unit, isUpdate);
        }
    }

    private void closeDialog() {
        if (isViewAttached()) {
            getView().closeDialog();
        }
    }

    private void showUnitDialog(UnitViewModel unit) {
        if (isViewAttached()) {
            getView().showUnitDialog(unit);
        }
    }
}
