package com.justplay1.shoppist.view;

import com.justplay1.shoppist.models.CurrencyViewModel;

/**
 * Created by Mkhytar on 03.07.2016.
 */
public interface AccountSettingView extends LoadDataView {

    void disableAccountButtons();

    void setAccountName(String name);

}
