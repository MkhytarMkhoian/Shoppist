package com.justplay1.shoppist.view;

import com.justplay1.shoppist.models.CurrencyViewModel;

import java.util.List;

/**
 * Created by Mkhytar on 03.07.2016.
 */
public interface CurrencyView extends LoadDataView {

    void showData(List<CurrencyViewModel> data);

    void showCurrencyAddDialog(CurrencyViewModel currency);
}
