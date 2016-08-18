package com.justplay1.shoppist.presenter;

import android.os.Bundle;

import com.justplay1.shoppist.interactor.DefaultSubscriber;
import com.justplay1.shoppist.interactor.currency.GetCurrencies;
import com.justplay1.shoppist.interactor.units.GetUnits;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.CurrencyViewModel;
import com.justplay1.shoppist.models.UnitViewModel;
import com.justplay1.shoppist.models.mappers.CurrencyModelDataMapper;
import com.justplay1.shoppist.presenter.base.BaseRxPresenter;
import com.justplay1.shoppist.view.SelectCurrencyView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 04.07.2016.
 */
public class SelectCurrencyPresenter extends BaseRxPresenter<SelectCurrencyView> {

    private final CurrencyModelDataMapper mDataMapper;
    private final GetCurrencies mGetCurrencies;

    private CurrencyViewModel mItem;

    @Inject
    public SelectCurrencyPresenter(CurrencyModelDataMapper dataMapper, GetCurrencies getCurrencies) {
        this.mDataMapper = dataMapper;
        this.mGetCurrencies = getCurrencies;
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
        mSubscriptions.add(mGetCurrencies.get()
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
