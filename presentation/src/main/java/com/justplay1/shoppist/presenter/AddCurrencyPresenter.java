package com.justplay1.shoppist.presenter;

import android.os.Bundle;

import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.interactor.DefaultSubscriber;
import com.justplay1.shoppist.interactor.currency.AddCurrency;
import com.justplay1.shoppist.interactor.currency.UpdateCurrency;
import com.justplay1.shoppist.models.CurrencyViewModel;
import com.justplay1.shoppist.models.mappers.CurrencyModelDataMapper;
import com.justplay1.shoppist.presenter.base.BaseRxPresenter;
import com.justplay1.shoppist.view.AddCurrencyView;

import java.util.Collections;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar on 07.08.2016.
 */
@PerActivity
public class AddCurrencyPresenter extends BaseRxPresenter<AddCurrencyView> {

    private final CurrencyModelDataMapper mDataMapper;
    private final UpdateCurrency mUpdateCurrency;
    private final AddCurrency mAddCurrency;

    private CurrencyViewModel mItem;

    @Inject
    public AddCurrencyPresenter(CurrencyModelDataMapper dataMapper,
                                UpdateCurrency updateCurrency,
                                AddCurrency addCurrency) {
        this.mDataMapper = dataMapper;
        this.mUpdateCurrency = updateCurrency;
        this.mAddCurrency = addCurrency;
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
        if (mItem != null) {
            setName(mItem.getName());
            setDefaultUpdateTitle();
        } else {
            setDefaultNewTitle();
        }
    }

    public void onPositiveButtonClick(String name) {
        saveCurrency(name);
    }

    public void onNegativeButtonClick() {
        closeDialog();
    }

    private void saveCurrency(String name) {
        if (checkDataForErrors(name)) {
            CurrencyViewModel currency = new CurrencyViewModel();
            if (mItem != null) {
                currency.setId(mItem.getId());
                currency.setServerId(mItem.getServerId());
                currency.setName(name);
                currency.setTimestamp(mItem.getTimestamp());
                if (!mItem.getName().equals(currency.getName())) {
                    currency.setDirty(true);
                }
                updateCurrency(currency);
            } else {
                currency.setName(name);
                currency.setDirty(true);
                addCurrency(currency);
            }
        }
    }

    private void addCurrency(CurrencyViewModel data) {
        showLoading();
        mSubscriptions.add(
                Observable.fromCallable(() -> mDataMapper.transform(data))
                        .flatMap(currency -> {
                            mAddCurrency.setData(currency);
                            return mAddCurrency.get();
                        }).subscribe(new SaveCurrencySubscriber(true)));
    }

    private void updateCurrency(CurrencyViewModel data) {
        showLoading();
        mSubscriptions.add(
                Observable.fromCallable(() -> mDataMapper.transform(data))
                        .flatMap(currency -> {
                            mUpdateCurrency.setData(Collections.singletonList(currency));
                            return mUpdateCurrency.get();
                        }).subscribe(new SaveCurrencySubscriber(false)));
    }

    private boolean checkDataForErrors(String name) {
        if (name.isEmpty()) {
            showNameIsRequiredError();
            return false;
        } else if (name.length() > 3) {
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

    private void showNameIsRequiredError() {
        if (isViewAttached()) {
            getView().showNameIsRequiredError();
        }
    }

    private void setName(String name) {
        if (isViewAttached()) {
            getView().setName(name);
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

    private final class SaveCurrencySubscriber extends DefaultSubscriber<Boolean> {

        private boolean isAddAction;

        public SaveCurrencySubscriber(boolean isAddAction) {
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
