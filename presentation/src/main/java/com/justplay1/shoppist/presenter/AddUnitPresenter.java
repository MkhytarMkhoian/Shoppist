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

import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.interactor.DefaultSubscriber;
import com.justplay1.shoppist.interactor.units.AddUnits;
import com.justplay1.shoppist.interactor.units.UpdateUnits;
import com.justplay1.shoppist.models.UnitViewModel;
import com.justplay1.shoppist.models.mappers.UnitsDataModelMapper;
import com.justplay1.shoppist.navigation.Router;
import com.justplay1.shoppist.presenter.base.BaseRxPresenter;
import com.justplay1.shoppist.utils.ModelUtils;
import com.justplay1.shoppist.view.AddUnitView;

import java.util.Collections;
import java.util.UUID;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar Mkhoian.
 */
@PerActivity
public class AddUnitPresenter extends BaseRxPresenter<AddUnitView, Router> {

    private final UnitsDataModelMapper mDataMapper;
    private final UpdateUnits mUpdateUnits;
    private final AddUnits mAddUnits;

    private UnitViewModel mItem;

    @Inject
    public AddUnitPresenter(UnitsDataModelMapper dataMapper,
                            UpdateUnits updateUnits,
                            AddUnits addUnits) {
        this.mDataMapper = dataMapper;
        this.mUpdateUnits = updateUnits;
        this.mAddUnits = addUnits;
    }

    @Override
    public void onCreate(Bundle arguments, Bundle savedInstanceState) {
        super.onCreate(arguments, savedInstanceState);
        if (arguments != null) {
            mItem = arguments.getParcelable(UnitViewModel.class.getName());
        } else if (savedInstanceState != null) {
            mItem = savedInstanceState.getParcelable(UnitViewModel.class.getName());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putParcelable(UnitViewModel.class.getName(), mItem);
    }

    public void init() {
        if (mItem != null) {
            setFullName(mItem.getName());
            setShortName(mItem.getShortName());
            setDefaultUpdateTitle();
        } else {
            setDefaultNewTitle();
        }
    }

    public void onPositiveButtonClick(String fullName, String shortName) {
        saveUnit(fullName, shortName);
    }

    public void onNegativeButtonClick() {
        closeDialog();
    }

    private void saveUnit(String fullName, String shortName) {
        if (checkDataForErrors(fullName, shortName)) {
            UnitViewModel unit = new UnitViewModel();
            if (mItem != null) {
                unit.setId(mItem.getId());
                unit.setName(fullName);
                unit.setShortName(shortName);
                updateUnit(unit);
            } else {
                unit.setId(UUID.nameUUIDFromBytes((ModelUtils.generateId()).getBytes()).toString());
                unit.setName(fullName);
                unit.setShortName(shortName);
                addUnit(unit);
            }
        }
    }

    private void addUnit(UnitViewModel data) {
        showLoading();
        mSubscriptions.add(
                Observable.fromCallable(() -> mDataMapper.transform(data))
                        .flatMap(unit -> {
                            mAddUnits.setData(Collections.singletonList(unit));
                            return mAddUnits.get();
                        }).subscribe(new SaveUnitSubscriber(true)));
    }

    private void updateUnit(UnitViewModel data) {
        showLoading();
        mSubscriptions.add(
                Observable.fromCallable(() -> mDataMapper.transform(data))
                        .flatMap(unit -> {
                            mUpdateUnits.setData(Collections.singletonList(unit));
                            return mUpdateUnits.get();
                        }).subscribe(new SaveUnitSubscriber(false)));
    }

    private boolean checkDataForErrors(String fullName, String shortName) {
        if (fullName.isEmpty()) {
            showFullNameIsRequiredError();
            return false;
        } else if (fullName.length() > 10) {
            return false;
        }
        if (shortName.isEmpty()) {
            showShortNameIsRequiredError();
            return false;
        } else if (shortName.length() > 3) {
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

    private void showFullNameIsRequiredError() {
        if (isViewAttached()) {
            getView().showFullNameIsRequiredError();
        }
    }

    private void showShortNameIsRequiredError() {
        if (isViewAttached()) {
            getView().showShortNameIsRequiredError();
        }
    }

    private void setFullName(String name) {
        if (isViewAttached()) {
            getView().setFullName(name);
        }
    }

    private void setShortName(String name) {
        if (isViewAttached()) {
            getView().setShortName(name);
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

    private final class SaveUnitSubscriber extends DefaultSubscriber<Boolean> {

        private boolean isAddAction;

        public SaveUnitSubscriber(boolean isAddAction) {
            this.isAddAction = isAddAction;
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
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
