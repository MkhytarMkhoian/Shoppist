package com.justplay1.shoppist.presenter;

import android.os.Bundle;

import com.justplay1.shoppist.interactor.DefaultSubscriber;
import com.justplay1.shoppist.interactor.units.GetUnits;
import com.justplay1.shoppist.interactor.units.SoftDeleteUnits;
import com.justplay1.shoppist.models.UnitViewModel;
import com.justplay1.shoppist.models.mappers.UnitsDataModelMapper;
import com.justplay1.shoppist.preferences.ShoppistPreferences;
import com.justplay1.shoppist.presenter.base.BaseRxPresenter;
import com.justplay1.shoppist.view.UnitsView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 01.07.2016.
 */
public class UnitsPresenter extends BaseRxPresenter<UnitsView> {

    private final UnitsDataModelMapper mDataMapper;
    private final ShoppistPreferences mPreferences;
    private final GetUnits mGetUnits;
    private final SoftDeleteUnits mSoftDeleteUnits;

    @Inject
    public UnitsPresenter(UnitsDataModelMapper unitsDataModelMapper,
                          ShoppistPreferences preferences,
                          GetUnits getUnits,
                          SoftDeleteUnits softDeleteUnits) {
        this.mDataMapper = unitsDataModelMapper;
        this.mPreferences = preferences;
        this.mGetUnits = getUnits;
        this.mSoftDeleteUnits = softDeleteUnits;
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
