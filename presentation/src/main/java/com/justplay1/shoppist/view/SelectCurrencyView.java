package com.justplay1.shoppist.view;

import com.justplay1.shoppist.models.CurrencyViewModel;

import java.util.List;

/**
 * Created by Mkhytar on 03.07.2016.
 */
public interface SelectCurrencyView extends LoadDataView {

    void setCurrencies(List<CurrencyViewModel> data);

    void selectCurrency(String id);

    void onComplete(CurrencyViewModel currency, boolean isUpdate);

    void closeDialog();

    void showCurrencyDialog(CurrencyViewModel currency);
}
