package com.justplay1.shoppist.presenter;

import android.os.Bundle;

import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.interactor.DefaultSubscriber;
import com.justplay1.shoppist.interactor.units.AddUnits;
import com.justplay1.shoppist.interactor.units.UpdateUnits;
import com.justplay1.shoppist.models.UnitViewModel;
import com.justplay1.shoppist.models.mappers.UnitsDataModelMapper;
import com.justplay1.shoppist.presenter.base.BaseRxPresenter;
import com.justplay1.shoppist.view.AddUnitView;

import java.util.Collections;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar on 07.08.2016.
 */
@PerActivity
public class AddUnitPresenter extends BaseRxPresenter<AddUnitView> {

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
                unit.setServerId(mItem.getServerId());
                unit.setName(fullName);
                unit.setShortName(shortName);
                unit.setTimestamp(mItem.getTimestamp());
                if (!mItem.getShortName().equals(unit.getShortName()) || !mItem.getName().equals(unit.getName())) {
                    unit.setDirty(true);
                }
                updateUnit(unit);
            } else {
                unit.setName(fullName);
                unit.setShortName(shortName);
                unit.setDirty(true);
                addUnit(unit);
            }
        }
    }

    private void addUnit(UnitViewModel data) {
        showLoading();
        mSubscriptions.add(
                Observable.fromCallable(() -> mDataMapper.transform(data))
                        .flatMap(unit -> {
                            mAddUnits.setData(unit);
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
