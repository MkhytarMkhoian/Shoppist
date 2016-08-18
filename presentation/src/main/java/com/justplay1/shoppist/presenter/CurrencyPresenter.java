package com.justplay1.shoppist.presenter;

import android.os.Bundle;

import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.interactor.DefaultSubscriber;
import com.justplay1.shoppist.interactor.currency.GetCurrencies;
import com.justplay1.shoppist.interactor.currency.SoftDeleteCurrency;
import com.justplay1.shoppist.models.CurrencyViewModel;
import com.justplay1.shoppist.models.mappers.CurrencyModelDataMapper;
import com.justplay1.shoppist.preferences.ShoppistPreferences;
import com.justplay1.shoppist.presenter.base.BaseRxPresenter;
import com.justplay1.shoppist.view.CurrencyView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 01.07.2016.
 */
@PerActivity
public class CurrencyPresenter extends BaseRxPresenter<CurrencyView> {

    private final CurrencyModelDataMapper mDataMapper;
    private final ShoppistPreferences mPreferences;
    private final GetCurrencies mGetCurrencies;
    private final SoftDeleteCurrency mSoftDeleteCurrency;

    @Inject
    public CurrencyPresenter(ShoppistPreferences preferences,
                             GetCurrencies getCurrencies,
                             SoftDeleteCurrency softDeleteCurrency,
                             CurrencyModelDataMapper dataMapper) {
        this.mPreferences = preferences;
        this.mGetCurrencies = getCurrencies;
        this.mSoftDeleteCurrency = softDeleteCurrency;
        this.mDataMapper = dataMapper;
    }

    public void init() {
        loadData();
    }

    public void loadData() {
        mSubscriptions.add(mGetCurrencies.get()
                .map(mDataMapper::transformToViewModel)
                .subscribe(new DefaultSubscriber<List<CurrencyViewModel>>() {
                    @Override
                    public void onNext(List<CurrencyViewModel> currencyViewModels) {
                        showData(currencyViewModels);
                    }
                }));
    }

    public void onAddButtonClick() {
        showCurrencyAddDialog(null);
    }

    public void onListItemClick(CurrencyViewModel currency) {
        showCurrencyAddDialog(currency);
    }

    public void onListItemLongClick(CurrencyViewModel currency) {

    }

    private void showCurrencyAddDialog(CurrencyViewModel currency) {
        if (isViewAttached()) {
            getView().showCurrencyAddDialog(currency);
        }
    }

    private void showData(List<CurrencyViewModel> data) {
        if (isViewAttached()) {
            getView().showData(data);
        }
    }

    public void deleteItems() {

    }

    public void checkAll() {

    }

    public void unCheckAll() {

    }
}
