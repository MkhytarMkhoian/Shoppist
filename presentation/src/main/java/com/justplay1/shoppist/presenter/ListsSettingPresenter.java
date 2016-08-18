package com.justplay1.shoppist.presenter;

import android.os.Bundle;

import com.justplay1.shoppist.interactor.DefaultSubscriber;
import com.justplay1.shoppist.interactor.currency.GetCurrency;
import com.justplay1.shoppist.models.CurrencyViewModel;
import com.justplay1.shoppist.models.mappers.CurrencyModelDataMapper;
import com.justplay1.shoppist.preferences.ShoppistPreferences;
import com.justplay1.shoppist.presenter.base.BaseRxPresenter;
import com.justplay1.shoppist.view.ListsSettingView;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar on 11.08.2016.
 */
public class ListsSettingPresenter extends BaseRxPresenter<ListsSettingView> {

    private final CurrencyModelDataMapper mCurrencyModelDataMapper;
    private final GetCurrency mGetCurrency;
    private final ShoppistPreferences mPreferences;

    @Inject
    public ListsSettingPresenter(CurrencyModelDataMapper currencyModelDataMapper,
                                 GetCurrency getCurrency,
                                 ShoppistPreferences preferences) {
        this.mCurrencyModelDataMapper = currencyModelDataMapper;
        this.mGetCurrency = getCurrency;
        this.mPreferences = preferences;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {

    }

    public void init() {
        if (!mPreferences.getDefaultCurrency().isEmpty()) {
            loadDefaultCurrency();
        }
    }

    private void loadDefaultCurrency() {
        mGetCurrency.setId(mPreferences.getDefaultCurrency());
        mSubscriptions.add(mGetCurrency.get()
                .flatMap(currencyModel -> {
                    if (currencyModel == null) {
                        mGetCurrency.setId(CurrencyViewModel.NO_CURRENCY_ID);
                        return mGetCurrency.get();
                    }
                    return Observable.fromCallable(() -> currencyModel);
                })
                .map(mCurrencyModelDataMapper::transformToViewModel)
                .subscribe(new DefaultSubscriber<CurrencyViewModel>() {
                    @Override
                    public void onNext(CurrencyViewModel result) {
                        if (result == null) return;
                        showData(result);
                    }
                }));
    }

    private void showData(CurrencyViewModel data) {
        if (isViewAttached()) {
            getView().showDefaultCurrency(data);
        }
    }
}
