package com.justplay1.shoppist.presenter;

import android.os.Bundle;

import com.justplay1.shoppist.interactor.DefaultSubscriber;
import com.justplay1.shoppist.interactor.units.GetUnits;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.UnitViewModel;
import com.justplay1.shoppist.models.mappers.UnitsDataModelMapper;
import com.justplay1.shoppist.presenter.base.BaseRxPresenter;
import com.justplay1.shoppist.view.SelectUnitView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 04.07.2016.
 */
public class SelectUnitPresenter extends BaseRxPresenter<SelectUnitView> {

    private final UnitsDataModelMapper mDataMapper;
    private final GetUnits mGetUnits;

    private UnitViewModel mItem;

    @Inject
    public SelectUnitPresenter(UnitsDataModelMapper dataMapper, GetUnits getUnits) {
        this.mDataMapper = dataMapper;
        this.mGetUnits = getUnits;
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
        loadUnits();
    }

    public void onPositiveButtonClick(UnitViewModel unit) {
        onComplete(unit, false);
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
        mSubscriptions.add(mGetUnits.get()
                .map(mDataMapper::transformToViewModel)
                .subscribe(new DefaultSubscriber<List<UnitViewModel>>() {

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
